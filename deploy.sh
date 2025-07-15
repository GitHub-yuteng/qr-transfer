#!/bin/bash

# 配置信息
REMOTE_USER="root"
REMOTE_HOST="139.129.27.112"
REMOTE_PATH="/www/wwwroot/wins/qr-transfer-1.0.0.jar"
DIST_FILE="target/qr-transfer-1.0.0.jar"

# 检查本地 JAR 文件是否存在
if [ ! -f "$DIST_FILE" ]; then
    echo "错误：本地文件 $DIST_FILE 不存在！"
    exit 1
fi

echo "开始上传文件..."
# 上传文件到远程服务器
echo "正在上传 $DIST_FILE 到 $REMOTE_HOST:$REMOTE_PATH..."

# 使用 rsync 上传文件
# -a: 归档模式，保持所有文件属性
# -v: 显示详细信息
# -z: 传输时压缩文件
rsync -avz -P $DIST_FILE $REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH

# 检查上传是否成功
if [ $? -ne 0 ]; then
    echo "文件上传失败"
    exit 1
fi

echo "正在重启远程服务器上的服务..."


ssh "${REMOTE_USER}@${REMOTE_HOST}" << 'EOF'
    # 杀死进程
    PID=$(sudo -u www cat /var/tmp/springboot/vhost/pids/qr-transfer.pid)
    if [ ! -z "$PID" ]; then
        echo "找到旧进程 PID: $PID，正在终止..."
        sudo -u www kill -9 "$PID"
        sleep 2
    else
        echo "未找到旧进程。"
        exit 1
    fi

    # 启动新服务
    echo "启动新服务..."
    sudo -u www sh /var/tmp/springboot/vhost/scripts/qr-transfer.sh
    sleep 2

    # 检查新进程是否启动成功
    NEW_PID=$(sudo -u www cat /var/tmp/springboot/vhost/pids/qr-transfer.pid)
    if [ ! -z "$NEW_PID" ]; then
        echo "服务启动成功，新进程 PID: $NEW_PID"
    else
        echo "错误：服务启动失败！"
        exit 1
    fi
EOF

if [ $? -ne 0 ]; then
    echo "错误：远程操作失败！"
    exit 1
fi

echo "上传和重启完成！"
