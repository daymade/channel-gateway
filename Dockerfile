# 设置解析器
# syntax=docker/dockerfile:1.2
FROM debian:stable-slim

LABEL authors="daymade" \
      maintainer="daymade <daymadev89@gmail.com>"

ARG JAVA_VERSION="21.0.2-graal"
ARG MAVEN_VERSION="3.9.1"

ARG http_proxy
ARG https_proxy

ENV http_proxy=${http_proxy} \
    https_proxy=${https_proxy} \
    SDKMAN_DIR=/root/.sdkman

RUN apt-get update \
    && apt-get install -y --no-install-recommends tzdata curl zip unzip build-essential libz-dev zlib1g-dev ca-certificates fontconfig locales \
    && echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen \
    && locale-gen en_US.UTF-8 \
    && curl 'https://get.sdkman.io' | bash \
    && rm -rf /var/lib/apt/lists/* \
    && echo "sdkman_auto_answer=true" > $SDKMAN_DIR/etc/config \
    && echo "sdkman_auto_selfupdate=false" >> $SDKMAN_DIR/etc/config \
    && echo "sdkman_insecure_ssl=true" >> $SDKMAN_DIR/etc/config \
    && chmod +x $SDKMAN_DIR/bin/sdkman-init.sh

RUN bash -c "source $SDKMAN_DIR/bin/sdkman-init.sh \
        && sdk version \
        && sdk install java $JAVA_VERSION \
        && sdk install maven $MAVEN_VERSION \
        && rm -rf $SDKMAN_DIR/archives/* \
        && rm -rf $SDKMAN_DIR/tmp/*"

# 设置工作目录
WORKDIR /opt/app

# 将项目文件复制到容器中, 包括代码
COPY . /opt/app

## 构建应用,使用 Maven 缓存
RUN --mount=type=cache,target=/root/.m2 bash -c "source $SDKMAN_DIR/bin/sdkman-init.sh && mvn clean package -DskipTests"

VOLUME /root/.m2

ENV SERVICE_OPTS="${SERVICE_OPTS} -Dspring.profiles.active=${ENV_TYPE}"

# 暴露端口
EXPOSE 9090

# 确保脚本可执行
RUN chmod +x "/opt/app/bin/entrypoint.sh"

# 设置入口点脚本
ENTRYPOINT ["/opt/app/bin/entrypoint.sh"]

# 运行应用
CMD ["java", "-jar", "target/channel-gateway-0.0.1-SNAPSHOT.jar"]