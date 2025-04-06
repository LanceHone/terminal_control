cd /home/dap/security/jdk
docker run -d --name redis  --network security-network -p 6379:6379 redis:latest
#docker run -d --name redis  --network security-network --ip 172.17.0.2 -p 6379:6379 redis:latest



docker build -t security:v1.0 .

docker save -o security.tar security #导出镜像

docker load -i security.tar #在服务器上导入

docker run -itd -p 5618:8080 -v /home/dap/security/logs:/app/logs --restart=always --name=security_java  --network security-network security:v1.0 tail -f /dev/null


#docker image inspect openjdk:8 | grep Architecture
# docker image inspect security:v1.0 | grep Architecture#

## 更新
## 后端
cd /home/dap/security
docker stop security_java
docker rm security_java
docker build -t security:v1.0 .
docker run -itd -p 5618:8080 -v /home/dap/security/logs:/app/logs --restart=always --name=security_java  --network security-network security:v1.0 tail -f /dev/null

docker logs security_java
docker network inspect bridge

docker exec -it security_java ping 172.17.0.2
docker exec -it security_java ping redis

##前端
docker rm -f nginx
docker run -itd --name=nginx --net=host --restart=always -v /home/dap/nginx/conf.d:/etc/nginx/conf.d -v /home/dap/nginx/data:/data nginx:1.26.1 nginx -g "daemon off;"

docker run -itd --name=nginx -p 80:82 --net=host --restart=always -v /home/dap/nginx/conf.d:/etc/nginx/conf.d -v /home/dap/nginx/data:/data nginx:1.26.1 nginx -g "daemon off;"
#docker run -d --name nginx -p 80:80 -v /home/dap/security/nginx.conf:/etc/nginx/nginx.conf --network security-network nginx:latest
