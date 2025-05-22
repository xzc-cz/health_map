# 🗺️ HealthMap Project

**HealthMap** is an Android mobile application designed to help users record and visualize daily health activity plans via an interactive map. The app supports date-based filtering and background synchronization via Firebase.

---

## 📱 Core Features

* 📝 Record daily health activities such as running, walking, or gym sessions
* 🗺️ Visualize all user plans on Mapbox using ViewAnnotations
* 🗕️ Date Picker: Filter plans by a specific date
* 🔁 Manual background sync with Firebase via WorkManager (with Logcat proof)
* 🌍 Future-ready for internationalization and multi-user support

---

## 📷 Screens Overview

| Screen             | Description                                            |
| ------------------ | ------------------------------------------------------ |
| 🏠 Home Screen     | Greets the user, navigation to "Create Plan" and "Map" |
| 📝 Form Screen     | Input form for activity name, time, date, and location |
| 🗺️ Map Screen     | Shows daily plans using ViewAnnotation; filter by date |
| 🗕️ Date Picker    | Select a different date to view filtered plans         |
| 👤 Profile / About | View user info or trigger background sync via button   |

---

## 💠 Tech Stack

* Kotlin + Jetpack Compose
* Mapbox SDK v11.11.0
* Room Database (local)
* Firebase Authentication + Firestore (cloud backend)
* WorkManager (background data sync)
* Material Design 3 styling
* Android SDK (minSdk 26, targetSdk 35)

---

## 🚀 How to Run Locally

1. Clone this repository:

```bash
git clone https://github.com/xzc-cz/health_map.git
```

2. Open the project in **Android Studio**

3. Add your **Mapbox token** to:

```xml
app/src/main/res/values/mapbox_access_token.xml
```

4. Ensure Firebase is configured:

   * Add your `google-services.json` under `app/`

5. Run the project (Use API 26+ emulator or device)

---

## 👥 Team Members

* **Zechen Xu**: Map display and filter (MapScreen)
* **Haojun Huang**: Form input and map annotation (FormScreen)
* **Xinjia Wang**: Authentication and login system (LoginScreen)
* **Xilong Wang**: Home screen and navigation structure (HomeScreen)

---

## 🔁 Git Collaboration Guidelines

### 📌 Branch Naming Conventions

| Branch Name     | Purpose                                    |
| --------------- | ------------------------------------------ |
| `main`          | Stable branch, contains release-ready code |
| `dev`           | Main development branch                    |
| `feature/map`   | Map and filtering functionality (Zechen)   |
| `feature/form`  | Form and validation UI (Haojun)            |
| `feature/login` | Auth module (Xinjia)                       |
| `feature/home`  | Navigation and dashboard (Xilong)          |

### 👨‍💻 Development Flow

```bash
# Create your feature branch
git checkout dev
git pull
git checkout -b feature/your-feature

# Make changes, then commit and push
git add .
git commit -m "Add [your feature description]"
git push origin feature/your-feature
```

### 🔁 Pull Request Workflow

1. Submit PR from `feature/your-feature` to `dev`
2. Get team review and approval
3. Merge into `dev` and test
4. After all features are stable, merge `dev` into `main`

---

## 🗓️ Development Timeline (Summary)

| Milestone            | Target Week | Status      |
| -------------------- | ----------- | ----------- |
| UI Compose Prototype | Week 7      | ✅ Completed |
| Firebase Integration | Week 9–10   | ✅ Completed |
| WorkManager Logging  | Week 10     | ✅ Completed |

---

## 📄 License & Usage

This project is licensed under the [MIT License](LICENSE).
Forking and reusing for educational purposes is welcome.
