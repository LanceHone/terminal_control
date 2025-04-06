docker run -p 6379:6379 --name redis -v /home/admin/docker-config/redis/redis.conf:/etc/redis/redis.conf -d redis:latest redis-server /etc/redis/redis.conf --appendonly yes
docker run -d --name redis  --network security-network --ip 172.17.0.2 -p 6379:6379 redis:latest

-------------
cd /home/dap/security/jdk




docker build -t security:v1.0 .

docker save -o security.tar security #导出镜像

docker load -i security.tar #在服务器上导入

docker run -itd -p 5618:8080 -v /home/dap/security/logs:/app/logs --restart=always --name=security_java security:v1.0 tail -f /dev/null


docker image inspect openjdk:8 | grep Architecture
 docker image inspect security:v1.0 | grep Architecture

cd /home/dap/security
 # 先停止并删除原有容器
docker stop security_java
docker rm security_java

# 然后重新构建镜像
docker build -t security:v1.0 .

# 最后启动新容器
docker run -itd -p 5618:8080 -v /home/dap/security/logs:/app/logs --restart=always --name=security_java  --network security-network security:v1.0 tail -f /dev/null


docker restart security_java

------------
docker network inspect bridge

docker logs security_java

docker network create security-network
docker network connect security-network security_java
docker network connect security-network redis

docker exec redis redis-cli FLUSHALL