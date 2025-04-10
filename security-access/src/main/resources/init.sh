#!/bin/bash

# 设置日志文件路径
LOG_FILE="/var/access/acc_ctl.log"

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

# 创建自定义链，初始化状态
iptables -L NET_WHITE_LIST >/dev/null 2>&1 || iptables -N NET_WHITE_LIST
iptables -L PORT_BLACK_LIST >/dev/null 2>&1 || iptables -N PORT_BLACK_LIST
iptables -F INPUT
iptables -F NET_WHITE_LIST
iptables -F PORT_BLACK_LIST
# 直接放行
iptables -A INPUT -p tcp --dport 19268 -j ACCEPT
iptables -A INPUT -p tcp --dport 37399 -j ACCEPT
iptables -A INPUT -p tcp --dport 8200 -j ACCEPT
iptables -A INPUT -p tcp --dport 22 -j ACCEPT
iptables -A INPUT -p tcp --dport 33066 -j ACCEPT
iptables -A INPUT -p tcp --dport 3306 -j ACCEPT
iptables -A INPUT -p tcp --dport 6379 -j ACCEPT
iptables -A INPUT -j PORT_BLACK_LIST
iptables -A INPUT -s 127.0.0.1 -j ACCEPT
iptables -A INPUT -s 10.0.0.0/8 -j ACCEPT
iptables -A INPUT -s 172.16.0.0/12 -j ACCEPT
#iptables -A INPUT -s 192.168.0.0/16 -j ACCEPT
iptables -A INPUT -s 224.0.0.0/4 -j ACCEPT

iptables -A INPUT -j NET_WHITE_LIST

iptables -A INPUT -j LOG --log-prefix "[ACCEPT] " --log-level 4 ##记录通过流量
iptables -A INPUT -j ACCEPT

#iptables -A PORT_BLACK_LIST -j RETURN #暂时没用这功能了

#iptables -A PORT_BLACK_LIST -p tcp --dport 23 -j DROP
#iptables -A PORT_BLACK_LIST -p tcp --dport 80 -j DROP
#iptables -A PORT_BLACK_LIST -p tcp --dport 21 -j DROP
#iptables -A PORT_BLACK_LIST -p tcp --dport 21 -j DROP # TEST ADD
#iptables -D PORT_BLACK_LIST -p tcp --dport 21 -j DROP # TEST DEL

#iptables -I NET_WHITE_LIST 1 -s 104.26.5.31 -j RETURN # TEST ADD
#iptables -D NET_WHITE_LIST 1 -s 104.26.5.31 -j RETURN # TEST DEL
#iptables -A NET_WHITE_LIST -j LOG --log-prefix "[DROP]" --log-level 4
#iptables -A NET_WHITE_LIST -j DROP #打开

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