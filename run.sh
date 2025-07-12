#!/bin/bash

echo "二维码传输聊天室启动脚本"
echo "========================"

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境"
    exit 1
fi

echo "Java版本:"
java -version

# 检查项目文件
if [ ! -f "src/main/java/com/qr/transfer/Application.java" ]; then
    echo "错误: 未找到Application.java文件"
    exit 1
fi

echo "项目文件检查完成"

# 如果Maven可用，使用Maven运行
if command -v mvn &> /dev/null; then
    echo "使用Maven启动项目..."
    mvn spring-boot:run
else
    echo "Maven未安装，请手动在IDE中运行项目"
    echo "或者安装Maven: brew install maven"
    echo ""
    echo "项目访问地址:"
    echo "- 首页: http://localhost:8080"
    echo "- 管理员: http://localhost:8080/admin.html"
    echo "- 代理扫描: http://localhost:8080/proxy.html?room=房间代码"
    echo "- 客户端: http://localhost:8080/customer.html?room=房间代码"
fi 