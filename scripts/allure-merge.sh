#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$PROJECT_ROOT" || exit 1

echo "📊 Generating Allure reports..."

BASE_DIR="target"

generate_report() {
  local platform=$1

  local main_dir="$BASE_DIR/allure-results/$platform"
  local rerun_dir="$BASE_DIR/allure-results/rerun-$platform"
  local report_dir="$BASE_DIR/allure-report-$platform"

  if [[ ! -d "$main_dir" ]]; then
    echo "⚠️ No results for $platform — skipping"
    return
  fi

  INPUTS="$main_dir"

  if [[ -d "$rerun_dir" ]]; then
    echo "🔁 Merging rerun results for $platform"
    INPUTS="$INPUTS $rerun_dir"
  else
    echo "ℹ️ No rerun results for $platform"
  fi

  allure generate $INPUTS --clean -o "$report_dir"
  echo "📦 $platform report ready"
}

generate_report "android"
generate_report "ios"

echo "🎉 Allure reports generated"