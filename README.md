# Android WebView/WebApp Template

A reusable template for creating Android apps with WebView that load local HTML/CSS/JS assets. Automatic APK builds via GitHub Actions.

## 🚀 Quick Start

1. **Clone this template**
```bash
   git clone https://github.com/Goodboycat/android-webview-template.git my-app
   cd my-app
```

1. Customize these files:
   · app/build.gradle - Change namespace and applicationId to com.yourname.yourapp
   · app/src/main/res/values/strings.xml - Change app_name to your app name
   · settings.gradle - Change rootProject.name to your project name
   · Move app/src/main/java/com/example/webviewapp/ to match your package name
   · Update package declaration in MainActivity.java
2. Add your web files:
   · HTML: app/src/main/assets/html/ (replace index.html)
   · CSS: app/src/main/assets/css/
   · JS: app/src/main/assets/js/
   · Images: app/src/main/assets/images/
3. Push to GitHub - APK builds automatically in Actions tab

📁 Files to Edit for New App

Essential Changes:

· app/build.gradle:
  ```gradle
  namespace 'com.yourname.yourapp'      ← Change this
  applicationId "com.yourname.yourapp"  ← Change this
  ```
· app/src/main/res/values/strings.xml:
  ```xml
  <string name="app_name">Your App Name</string>  ← Change this
  ```
· settings.gradle:
  ```gradle
  rootProject.name = "Your App Name"  ← Change this
  ```
· Java Package: Move app/src/main/java/com/example/webviewapp/ to your package structure

Add Your Content:

· Delete placeholder files in assets folders
· Add your HTML/CSS/JS files to respective folders
· Update index.html to load your main page

⚙️ Features

· ✅ Modern WebView - JavaScript, DOM storage, mobile-optimized
· ✅ Automatic APK builds - GitHub Actions workflow included
· ✅ Responsive design - Mobile-first CSS template
· ✅ Latest Android - SDK 34, Material Design, no deprecated APIs
· ✅ Ready structure - HTML/CSS/JS/assets folders pre-created

📦 Building

APK builds automatically on every push to main branch. Download from:
GitHub Repo → Actions → Latest workflow run → Artifacts

Local build:

```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

🛠️ Package Renaming Example

From: com.example.webviewapp
To: com.goodboycat.myapp

1. Update app/build.gradle with new package
2. Create directory: app/src/main/java/com/goodboycat/myapp/
3. Move MainActivity.java to new directory
4. Update package declaration in MainActivity.java to package com.goodboycat.myapp;

📄 Project Structure

```
app/src/main/
├── assets/           # Web content
│   ├── html/        # HTML files (replace index.html)
│   ├── css/         # Stylesheets
│   ├── js/          # JavaScript
│   └── images/      # Images
├── java/            # Android code (update package)
└── res/             # Resources (update strings.xml)
```

🔗 Web Asset References

In your HTML files, reference assets like:

```html
<link rel="stylesheet" href="file:///android_asset/css/style.css">
<script src="file:///android_asset/js/app.js"></script>
<img src="file:///android_asset/images/logo.png">
```

📝 License

[MIT License]
