#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$PROJECT_ROOT" || exit 1

echo "📁 Project root: $PROJECT_ROOT"

echo "🧹 Cleaning old rerun files..."
rm -f target/rerun/*.txt || true

echo "🛠 Building full reactor (no tests)..."
mvn clean install -DskipTests

echo "🚀 Running Android & iOS in parallel..."

# ANDROID
mvn test \
  -Dplatform=ANDROID \
  -Ptestng-android \
  -Dtestng.suite=src/test/resources/testng/testng-android.xml \
  -Dallure.results.directory=target/allure-results/android &
PID_ANDROID=$!

# IOS
mvn test \
  -Dplatform=IOS \
  -Ptestng-ios \
  -Dtestng.suite=src/test/resources/testng/testng-ios.xml \
  -Dallure.results.directory=target/allure-results/ios &
PID_IOS=$!

wait $PID_ANDROID || true
wait $PID_IOS || true

echo "🎯 Main execution finished"
