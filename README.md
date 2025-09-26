# Agatha Christie小说阅读平台后端

## 项目简介
Agatha Christie小说在线阅读平台的后端服务，基于Spring Boot构建，提供小说内容管理、用户系统、章节浏览等API接口。

## 技术栈
- Spring Boot 2.x
- Java 8+
- MySQL
- Redis
- JPA/Hibernate

## 功能特性
- 小说信息管理
- 章节内容读取与分页
- 用户注册登录
- 评论与讨论功能
- 媒体内容管理（电影、电视剧等）
- Redis缓存优化

## 项目结构
src/main/java/com/agatha/agatha/
├── config/ # 配置类（RedisConfig, WebConfig） 
├── controller/ # 控制器层（NovelController, UserController等） 
├── dto/ # 数据传输对象 
├── entity/ # 实体类（Novel, Chapter, User等） 
├── repository/ # 数据访问层 
├── service/ # 业务逻辑层 
└── AgathaApplication.java # 启动类
## 环境要求
- Java 8+
- MySQL 5.7+
- Redis
- Maven 3.6+

## 配置文件
在 `src/main/resources/application.properties` 中配置：
- 数据库连接信息
- Redis配置
- 服务器端口（默认6460）

## 安装部署

### 本地运行
bash
克隆项目
git clone <your-repository-url> cd agatha-backend
修改application.properties配置文件
设置正确的数据库和Redis连接信息
编译运行
mvn spring-boot:run
### 打包部署
bash
打包
mvn clean package
运行
java -jar target/agatha-0.0.1-SNAPSHOT.jar
## API接口
主要API包括：
- `/api/novels` - 小说列表
- `/api/novels/{id}` - 小说详情
- `/api/novels/{id}/chapters` - 章节列表
- `/api/chapters/{id}/content` - 章节内容
- `/api/users` - 用户相关接口

## 数据库设计
使用MySQL数据库，主要表包括：
- `novel` - 小说信息
- `chapter` - 章节信息
- `user` - 用户信息
- `discussion` - 讨论区
- `review` - 评论

## 小说内容存储
小说内容以文本文件形式存储在 `src/main/resources/novels/` 目录下，按小说名称分类存储。

## Docker支持
项目包含Dockerfile，支持容器化部署。
## 未完成：
评论和讨论、历史信息等功能目前正在完善中，目前还是雏形。基本的提交和展示功能已经可用，但界面交互和功能完整性还在持续改进。
