#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$PROJECT_ROOT" || exit 1

BASE_DIR="target"

ANDROID_RERUN="$BASE_DIR/rerun/android.txt"
IOS_RERUN="$BASE_DIR/rerun/ios.txt"

echo "🔍 Checking failed scenarios..."

if [[ -s "$ANDROID_RERUN" ]]; then
  echo "♻️  Rerunning ANDROID failures..."
  mvn test \
    -Ptestng-rerun-android \
    -Dtest=TestNgFailedRunner \
    -Dplatform=ANDROID \
    -Dallure.results.directory=target/allure-results/rerun-android || true
else
  echo "✅ Android clean — no rerun needed"
fi

if [[ -s "$IOS_RERUN" ]]; then
  echo "♻️  Rerunning IOS failures..."
  mvn test \
    -Ptestng-rerun-ios \
    -Dtest=TestNgFailedRunner \
    -Dplatform=IOS \
    -Dallure.results.directory=target/allure-results/rerun-ios || true
else
  echo "✅ iOS clean — no rerun needed"
fi

echo "🔁 Rerun phase completed"