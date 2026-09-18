#!/bin/bash

echo "Android WebView App Setup"
echo "========================="

read -p "Enter app name: " APP_NAME
read -p "Enter package name (e.g., com.yourname.yourapp): " PACKAGE_NAME
read -p "Enter GitHub repo name: " REPO_NAME

# Update strings.xml
sed -i "s/My App/$APP_NAME/g" app/src/main/res/values/strings.xml

# Update package name in build.gradle
sed -i "s/com\\.goodboycat\\.ttrpg_advance_toolkit/$PACKAGE_NAME/g" app/build.gradle

# Update package structure
OLD_PKG_DIR="app/src/main/java/com/goodboycat/ttrpg_advance_toolkit"
NEW_PKG_DIR="app/src/main/java/$(echo $PACKAGE_NAME | sed 's/\./\//g')"
mkdir -p "$(dirname "$NEW_PKG_DIR")"
mv "$OLD_PKG_DIR" "$NEW_PKG_DIR"

# Update Java package declaration
find "$NEW_PKG_DIR" -name "*.java" -exec sed -i "s/package com\\.goodboycat\\.ttrpg_advance_toolkit/package $PACKAGE_NAME/g" {} \;

echo "Setup complete! Your app '$APP_NAME' is ready."
echo "Next steps:"
echo "1. Add your HTML files to app/src/main/assets/html/"
echo "2. Create GitHub repository: $REPO_NAME"
echo "3. git init && git add . && git commit -m 'Initial commit'"
echo "4. git remote add origin https://github.com/YourUsername/$REPO_NAME.git"
echo "5. git push origin main"
