FROM openjdk:8
# WORKDIR /security
ADD ruoyi-admin.jar security.jar
# COPY libjnetpcap.so /usr/lib/libjnetpcap.so
# 定义日志目录
VOLUME /security/logs
EXPOSE 5618
VOLUME ["/home/security"]
ENTRYPOINT [ "java", "-jar", "/security.jar" ]