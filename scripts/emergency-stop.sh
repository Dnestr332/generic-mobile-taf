#!/bin/bash

echo "🛑 EMERGENCY STOP — killing automation processes"

pkill -9 -f "mvn.*test" || true
pkill -9 -f "appium" || true
pkill -9 -f "AppiumDriverLocalService" || true

echo "✔ Automation processes force-stopped"
