#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR"

cd "$PROJECT_ROOT" || exit 1

echo "🧪 LOCAL RUN STARTED"
echo "📁 Project root: $PROJECT_ROOT"

"$SCRIPT_DIR/scripts/run-parallel.sh"
"$SCRIPT_DIR/scripts/run-failed.sh"
"$SCRIPT_DIR/scripts/allure-merge.sh"
"$SCRIPT_DIR/scripts/cleanup.sh"

echo "📱 Opening Android Allure report..."
allure open target/allure-report-android &

echo "🍏 Opening iOS Allure report..."
allure open target/allure-report-ios &

echo "✅ LOCAL RUN FINISHED"
