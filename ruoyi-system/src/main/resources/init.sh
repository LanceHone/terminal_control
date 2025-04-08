#!/bin/bash

# 设置日志文件路径
LOG_FILE="/var/access/blocked.log"

# 检查 /var/access 目录是否存在，不存在则创建
if [ ! -d "/var/access" ]; then
  echo "创建 /var/access 目录..."
  mkdir -p /var/access
fi

# 检查日志文件是否存在，不存在则创建
if [ ! -f "$LOG_FILE" ]; then
  echo "创建日志文件 $LOG_FILE ..."
  touch "$LOG_FILE"
  chmod 644 "$LOG_FILE"
  chown syslog:adm "$LOG_FILE"
fi

# 直接放行的
iptables -L NET_WHITE_LIST >/dev/null 2>&1 || iptables -N NET_WHITE_LIST
iptables -L PORT_BLACK_LIST >/dev/null 2>&1 || iptables -N PORT_BLACK_LIST
iptables -F INPUT
iptables -F NET_WHITE_LIST
iptables -F PORT_BLACK_LIST
iptables -A INPUT -s 127.0.0.1 -j ACCEPT
iptables -A INPUT -s 10.0.0.0/8 -j ACCEPT
iptables -A INPUT -s 172.16.0.0/12 -j ACCEPT
#iptables -A INPUT -s 192.168.0.0/16 -j ACCEPT
iptables -A INPUT -s 192.168.31.1 -j LOG --log-prefix "[ACCEPT] " --log-level 4 ##记录通过流量
iptables -A INPUT -s 192.168.31.1 -j ACCEPT
iptables -A INPUT -s 224.0.0.0/4 -j ACCEPT
iptables -A INPUT -p tcp --dport 22 -j ACCEPT
iptables -A INPUT -j PORT_BLACK_LIST # 拦截端口日志做在自定义链中
iptables -A INPUT -j NET_WHITE_LIST # 拦截IP日志做在自定义链中
iptables -A INPUT -j LOG --log-prefix "[ACCEPT] " --log-level 4 ##记录通过流量
iptables -A INPUT -j ACCEPT

iptables -A PORT_BLACK_LIST -j RETURN # 开关:关
iptables -A PORT_BLACK_LIST -p tcp --dport 23 -j DROP
iptables -A PORT_BLACK_LIST -p tcp --dport 80 -j DROP
iptables -A PORT_BLACK_LIST -p tcp --dport 21 -j DROP
#iptables -A PORT_BLACK_LIST -p tcp --dport 21 -j DROP # TEST ADD
#iptables -D PORT_BLACK_LIST -p tcp --dport 21 -j DROP # TEST DEL

#iptables -I NET_WHITE_LIST 1 -s 104.26.5.31 -j RETURN # TEST ADD
#iptables -D NET_WHITE_LIST 1 -s 104.26.5.31 -j RETURN # TEST DEL
iptables -A NET_WHITE_LIST -j LOG --log-prefix "[DROP] " --log-level 4
#iptables -A NET_WHITE_LIST -j DROP #开关:

# 配置 rsyslog 将日志输出到指定的文件
RSYSLOG_CONF="/etc/rsyslog.d/iptables.conf"

echo "配置 rsyslog 将日志输出到 $LOG_FILE ..."

# 写入 rsyslog 配置文件
cat <<EOL > "$RSYSLOG_CONF"
:msg, contains, "[DROP]" $LOG_FILE
:msg, contains, "[ACCEPT]" $LOG_FILE
& stop
EOL

# 重启 rsyslog 使配置生效
echo "重启 rsyslog 服务..."
systemctl restart rsyslog