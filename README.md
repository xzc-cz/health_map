# 🗺️ HealthMap 项目

**HealthMap** 是一个 Android 移动端应用，旨在帮助用户记录、查看每日健康计划，并通过 Mapbox 地图进行可视化展示。支持通过日期筛选查看计划，未来可扩展集成 Google 日历，实现个性化推荐。

---

## 📱 核心功能

- 📝 录入每日健康活动，如跑步、散步、健身等
- 🗺️ 地图展示所有用户的活动位置（使用 Mapbox ViewAnnotation）
- 📅 日期筛选器：只显示某一天的计划
- 🧠（开发中）Google 日历集成与情境感知
- 🌍 支持未来国际化与多用户拓展

---

## 📷 页面概览

| 页面              | 描述                                         |
|-------------------|----------------------------------------------|
| 🏠 首页          | 欢迎语，按钮跳转至“添加计划”与“地图页”      |
| 📝 表单录入页面   | 用户输入计划内容，包括活动、时间、位置等     |
| 🗺️ 地图展示页面 | 以标记方式显示每日计划，支持日期选择器筛选 |
| 📅 日期选择器     | 通过点击选择不同日期切换显示的计划内容       |

---

## 🛠️ 技术栈

- Kotlin + Jetpack Compose
- Mapbox SDK v11.11.0
- Android SDK（minSdk 26，targetSdk 35）
- Material Design 3 组件风格

---

## 🚀 本地运行指南

1. 克隆仓库：
```bash
git clone https://github.com/xzc-cz/health_map.git
```

2. 使用 Android Studio 打开项目

3. 添加你的 Mapbox token 到：
```xml
app/src/main/res/values/mapbox_access_token.xml
```

4. 运行项目（需使用 API 26+ 的设备或模拟器）

---

## 👥 团队成员

- zechen xu：地图功能（MapScreen）
- haojun huang：表单录入（FormScreen）
- xinjia wang：登录与认证模块（LoginScreen）
- xilong wang：首页与导航结构（HomeScreen）

---

## 🔁 团队协作指南（Git 分支规范）

### 📌 分支说明

| 分支名称       | 用途说明                                |
|----------------|------------------------------------------|
| `main`         | 稳定分支，仅合并测试通过的代码          |
| `dev`          | 开发主分支，整合各模块                  |
| `feature/map`  | 地图功能开发（Alex）                    |
| `feature/form` | 表单录入功能（Mia）                     |
| `feature/login`| 登录与认证模块（Leo）                   |
| `feature/home` | 首页导航开发（Sophie）                  |

### 👨‍💻 分支开发流程

```bash
# 创建功能分支
git checkout dev
git pull
git checkout -b feature/xxx

# 提交代码
git add .
git commit -m "描述你的功能"
git push origin feature/xxx
```

### 🔁 Pull Request 合并流程

1. 从 `feature/xxx` 分支 → 发起合并请求到 `dev`
2. 通过 Review 后统一合并
3. 测试无误后再从 `dev` 合并至 `main`

---

## 📅 开发计划（简要）

待定

---

## 📄 协议与版权

本项目遵循 [MIT License](LICENSE) 协议，欢迎 fork 与二次开发。
