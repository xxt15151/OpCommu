# 算子搭配社区 OpCommu(Operator Assembly Community)

一个基于 Vue 3 + Spring Boot 的垂直工具社区网站，支持用户自由搭配“算子”（属性单元）、创建搭配组、发布文章与评论，并配备独立的管理后台。

## ✨ 主要功能

### 普通用户
- 注册 / 登录
- 算子列表浏览与搜索
- 搭配组：创建、保存、加载、属性自动累加/拼接
- 文章：发布、列表排序（点赞量/时间）、详情查看、点赞
- 评论：发表评论与嵌套回复
- 个人中心：我的文章 / 我的搭配组
- 深色模式切换

### 管理员
- 独立登录（账号手动初始化）
- 普通用户的增删改查
- 算子的增删改查

## 🛠 技术栈

| 模块 | 技术 |
|------|------|
| 后端 | Java Spring Boot, H2 Database |
| 前端 | Vue 3, Vite, Pinia, Vue Router, Axios |
| 样式 | CSS Variables（支持深色模式），BMW 设计规范 |
| 构建工具 | Maven (后端) / npm (前端) |

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- Maven (或使用 IDE 内置)

### 1. 克隆仓库
```bash
git clone https://github.com/your-repo/operator-assembly-community.git
cd operator-assembly-community
```

### 2. 后端启动

进入后端根目录（假设为 `backend/`）：
```bash
cd backend
mvn spring-boot:run
```
- 默认端口：`8080`
- H2 控制台：`http://localhost:8080/h2-console`
- 数据库文件自动生成在 `~/h2db/` 或项目目录

### 3. 前端启动

进入前端目录（假设为 `frontend/`）：
```bash
cd frontend
npm install
npm run dev
```
- 默认端口：`5173`
- 自动代理 `/api` 到后端 `http://localhost:8080`

### 4. 初始化管理员账号

后端启动后，手动执行 SQL（H2 控制台或通过脚本）：
```sql
INSERT INTO admin_user (username, password, email) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@example.com');
```
默认密码：`admin123`

## 📁 项目结构简览

```
.
├── backend/                     # Spring Boot 后端
│   ├── src/main/java/...        # Controller, Service, Repository
│   └── src/main/resources/      # application.properties, schema.sql
├── frontend/                    # Vue 3 前端
│   ├── src/
│   │   ├── api/                 # Axios 请求封装
│   │   ├── components/          # 可复用组件
│   │   ├── stores/              # Pinia 状态管理
│   │   ├── views/               # 页面
│   │   └── style/               # CSS 变量与全局样式
│   └── package.json
└── README.md
```

## ⚙️ 配置说明

### 前端代理 (vite.config.js)
```js
export default {
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
}
```

### 后端端口修改
编辑 `application.properties`：
```properties
server.port=8080
```

### 深色模式
自动跟随系统或手动切换，状态保存在 `localStorage`。

## 📝 API 文档参考

详细接口文档位于 `docs/` 目录（或见项目内 Markdown）：
- 管理员接口 (`/api/admin/*`)
- 用户接口 (`/api/users/*`)
- 算子 (`/api/operators`)
- 搭配组 (`/api/groups`)
- 文章与评论 (`/api/articles`, `/api/comments`)

> 当前认证方式为请求头传递 `X-User-Id`（普通用户）或 `X-Admin-Id`（管理员），适合开发测试。生产环境建议升级为 JWT。

## 🧪 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 普通用户 | 自行注册 | — |
| 管理员 | `admin` | `admin123` |

## 📄 许可证

MIT License

---

**Enjoy building your own operator assembly community!**