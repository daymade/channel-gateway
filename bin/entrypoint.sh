#!/bin/bash
# entrypoint.sh

export SDKMAN_DIR=/root/.sdkman

source /root/.bashrc

# 可以在这里加载环境变量或执行必要的服务检查
echo "Initializing the application setup..."

# 比如，使用 SDKMAN 初始化
if [ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]; then
    source "$SDKMAN_DIR/bin/sdkman-init.sh"
fi

# 执行传递到脚本的命令
exec "$@"