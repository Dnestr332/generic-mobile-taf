#!/bin/bash
set -e

echo "🧹 Cleaning up test processes..."

# Stop Appium (Node)
if pgrep -f "appium" > /dev/null; then
  echo "🔌 Stopping Appium..."
  pkill -15 -f "appium"
  sleep 3
fi

# Stop Appium Java services
if pgrep -f "AppiumDriverLocalService" > /dev/null; then
  echo "🔌 Stopping Appium Java service..."
  pkill -15 -f "AppiumDriverLocalService"
  sleep 2
fi

# Stop Maven test processes for this repo only
if pgrep -f "generic-mobile-taf" > /dev/null; then
  echo "🧨 Stopping Maven processes..."
  pkill -15 -f "generic-mobile-taf"
  sleep 5
fi

echo "✅ Cleanup completed"
