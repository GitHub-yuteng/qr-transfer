# 二维码传输聊天室

基于SpringBoot和WebSocket的实时二维码传输系统，支持管理员创建聊天室，代理扫描二维码，客户端实时接收显示。

## 功能特性

- 🏠 **聊天室管理**：管理员可以创建、查询、解散聊天室
- 📱 **二维码扫描**：代理使用摄像头实时扫描二维码
- 💻 **实时传输**：基于WebSocket的实时数据传输
- 🎯 **客户端展示**：客户端实时接收并显示扫描到的二维码信息
- 🔄 **自动重连**：支持连接断开后自动重连

## 技术栈

- **后端**：Spring Boot 2.7.6
- **WebSocket**：Spring WebSocket
- **前端**：HTML5 + JavaScript + jsQR
- **构建工具**：Maven

## 快速开始

### 1. 环境要求

- JDK 8+
- Maven 3.6+

### 2. 运行项目

```bash
# 克隆项目
git clone <repository-url>
cd qr-transfer

# 编译运行
mvn spring-boot:run
```

### 3. 访问应用

启动成功后，访问：http://localhost:8080

## 使用说明

### 1. 管理员操作

1. 访问 http://localhost:8080/admin.html
2. 输入聊天室代码，点击"创建聊天室"
3. 点击"加入聊天室"监控连接状态

### 2. 代理操作

1. 访问 http://localhost:8080/proxy.html?room=聊天室代码
2. 允许浏览器访问摄像头
3. 使用摄像头扫描二维码，系统会自动传输

### 3. 客户端操作

1. 访问 http://localhost:8080/customer.html?room=聊天室代码
2. 等待代理扫描二维码
3. 实时接收并显示扫描到的二维码信息

## 项目结构

```
src/main/java/com/qr/transfer/
├── Application.java              # 启动类
├── config/
│   └── WebSocketConfig.java      # WebSocket配置
├── constant/
│   └── UserType.java             # 用户类型枚举
├── room/
│   ├── Room.java                 # 聊天室管理
│   └── WebSocket.java            # WebSocket处理
├── utils/
│   └── DateUtils.java            # 日期工具类
└── web/
    └── ChatRoomController.java   # 聊天室控制器

src/main/resources/
├── application.properties        # 配置文件
└── static/
    ├── index.html                # 首页
    ├── admin.html                # 管理员页面
    ├── proxy.html                # 代理扫描页面
    └── customer.html             # 客户端展示页面
```

## API接口

### 聊天室管理

- `GET /chatroom/creat?code={code}` - 创建聊天室
- `GET /chatroom/breakup?code={code}` - 解散聊天室
- `GET /chatroom/remove` - 解散所有聊天室
- `GET /chatroom/query` - 查询聊天室列表
- `GET /chatroom/exists?code={code}` - 检查聊天室是否存在

### WebSocket端点

- `ws://localhost:8080/room/{qrCode}/{userType}` - WebSocket连接
  - `{qrCode}`: 聊天室代码
  - `{userType}`: 用户类型 (admin/proxy/customer)

## 用户类型

- **admin**: 管理员，可以监控聊天室状态
- **proxy**: 代理，负责扫描二维码
- **customer**: 客户，接收二维码信息

## 注意事项

1. 确保浏览器支持摄像头访问
2. 二维码需要以 "https://www.kiwa-tech.com" 开头才会被处理
3. 所有用户需要在同一个聊天室中才能正常通信
4. 建议使用HTTPS协议以获得更好的摄像头访问权限

## 开发说明

### 添加新的用户类型

1. 在 `UserType.java` 中添加新的枚举值
2. 在 `WebSocket.java` 中处理新用户类型的逻辑
3. 创建对应的前端页面

### 自定义二维码前缀

修改 `WebSocket.java` 中的 `PREFIX` 常量：

```java
private static final String PREFIX = "your-custom-prefix";
```

## 许可证

MIT License 