#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pcap.h>
#include <mysql/mysql.h>
#include <signal.h>
#include <stdbool.h>
#include <arpa/inet.h>

#define MYSQL_USER "root"
#define MYSQL_PASSWORD "password"
#define MYSQL_DB "firewall_db"
#define MYSQL_HOST "localhost"

typedef struct {
    bool whitelist_mode;
    char **allowed_protocols;
    int protocol_count;
} FirewallPolicy;

FirewallPolicy current_policy = {false, NULL, 0};
pcap_t *handle;
volatile sig_atomic_t stop_capture = 0;

void sigint_handler(int sig) {
    stop_capture = 1;
}

void load_policy_from_db(FirewallPolicy *policy) {
    MYSQL *conn = mysql_init(NULL);
    if (!mysql_real_connect(conn, MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DB, 0, NULL, 0)) {
        fprintf(stderr, "MySQL connection failed: %s\n", mysql_error(conn));
        return;
    }

    if (mysql_query(conn, "SELECT protocol FROM access_control")) {
        fprintf(stderr, "Query failed: %s\n", mysql_error(conn));
        mysql_close(conn);
        return;
    }

    MYSQL_RES *result = mysql_store_result(conn);
    if (result) {
        int num_rows = mysql_num_rows(result);
        policy->allowed_protocols = malloc(num_rows * sizeof(char*));
        policy->protocol_count = 0;

        MYSQL_ROW row;
        while ((row = mysql_fetch_row(result))) {
            policy->allowed_protocols[policy->protocol_count++] = strdup(row[0]);
        }
        mysql_free_result(result);
        policy->whitelist_mode = true;
    } else {
        policy->whitelist_mode = false;
    }

    mysql_close(conn);
}

bool is_protocol_allowed(const char *protocol) {
    if (!current_policy.whitelist_mode) return true;

    for (int i = 0; i < current_policy.protocol_count; i++) {
        if (strcmp(current_policy.allowed_protocols[i], protocol) == 0) {
            return true;
        }
    }
    return false;
}

bool check_policy_conflict(const FirewallPolicy *new_policy) {
    if (!current_policy.whitelist_mode) {
        // Current policy is default (allow all)
        return new_policy->whitelist_mode ? false : true;
    }

    // Check if new policy is a superset of current policy
    for (int i = 0; i < current_policy.protocol_count; i++) {
        bool found = false;
        for (int j = 0; j < new_policy->protocol_count; j++) {
            if (strcmp(current_policy.allowed_protocols[i], new_policy->allowed_protocols[j]) == 0) {
                found = true;
                break;
            }
        }
        if (!found) return true; // Conflict found
    }
    return false; // No conflict
}

void process_packet(u_char *user, const struct pcap_pkthdr *pkthdr, const u_char *packet) {
    struct ip *ip_header = (struct ip *)(packet + 14); // Skip Ethernet header
    char protocol[10];

    switch(ip_header->ip_p) {
        case IPPROTO_TCP: strcpy(protocol, "TCP"); break;
        case IPPROTO_UDP: strcpy(protocol, "UDP"); break;
        case IPPROTO_ICMP: strcpy(protocol, "ICMP"); break;
        default: sprintf(protocol, "%d", ip_header->ip_p); break;
    }

    if (!is_protocol_allowed(protocol)) {
        printf("Blocked packet using %s protocol\n", protocol);
    }
}

void set_firewall_policy() {
    FirewallPolicy new_policy = {0};
    load_policy_from_db(&new_policy);

    if (check_policy_conflict(&new_policy)) {
        printf("Policy change would affect current communications. Setting failed.\n");
        free(new_policy.allowed_protocols);
        return;
    }

    // Apply new policy
    for (int i = 0; i < current_policy.protocol_count; i++) {
        free(current_policy.allowed_protocols[i]);
    }
    free(current_policy.allowed_protocols);
    current_policy = new_policy;
    printf("Policy updated successfully.\n");
}

int main(int argc, char *argv[]) {
    char errbuf[PCAP_ERRBUF_SIZE];
    const char *dev = pcap_lookupdev(errbuf);

    handle = pcap_open_live(dev, BUFSIZ, 1, -1, errbuf);
    if (handle == NULL) {
        fprintf(stderr, "Couldn't open device %s: %s\n", dev, errbuf);
        return 1;
    }

    signal(SIGINT, sigint_handler);

    // Initial policy load
    load_policy_from_db(&current_policy);

    printf("Firewall started. Current policy: %s\n",
           current_policy.whitelist_mode ? "Whitelist mode" : "Default mode");

    while (!stop_capture) {
        printf("1. Set new policy\n2. Continue monitoring\nChoose: ");
        int choice;
        scanf("%d", &choice);

        if (choice == 1) {
            set_firewall_policy();
        } else if (choice == 2) {
            pcap_loop(handle, 0, process_packet, NULL);
        }
    }

    pcap_close(handle);
    return 0;
}