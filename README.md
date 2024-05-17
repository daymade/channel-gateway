
构建
```shell
cd channel-gateway
DOCKER_BUILDKIT=1 docker build -t channel-gateway .
```

使用代理加速构建
```shell
DOCKER_BUILDKIT=1 docker build -t channel-gateway . --build-arg https_proxy=http://192.168.31.221:7890 --build-arg http_proxy=http://192.168.31.221:7890 --progress=plain
```

临时运行
```shell
docker run -p 9090:9090 channel-gateway
```

后台运行
```shell
docker run -d -p 9090:9090 channel-gateway
```