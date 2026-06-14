#!/bin/bash
set -euo pipefail

# Only needed for Claude Code on the web (ephemeral remote containers).
if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

ANDROID_SDK_DIR="$HOME/android-sdk"
CMDLINE_TOOLS_DIR="$ANDROID_SDK_DIR/cmdline-tools/latest"
SDKMANAGER="$CMDLINE_TOOLS_DIR/bin/sdkmanager"

# Download cmdline-tools only if not already present (idempotent / cache-friendly).
if [ ! -x "$SDKMANAGER" ]; then
  mkdir -p "$ANDROID_SDK_DIR/cmdline-tools"
  curl -sL -o /tmp/cmdline-tools.zip \
    "https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"
  unzip -q /tmp/cmdline-tools.zip -d "$ANDROID_SDK_DIR/cmdline-tools"
  mv "$ANDROID_SDK_DIR/cmdline-tools/cmdline-tools" "$CMDLINE_TOOLS_DIR"
  rm /tmp/cmdline-tools.zip
fi

export ANDROID_HOME="$ANDROID_SDK_DIR"
export ANDROID_SDK_ROOT="$ANDROID_SDK_DIR"

yes | "$SDKMANAGER" --sdk_root="$ANDROID_SDK_DIR" --licenses >/dev/null 2>&1 || true

# compileSdk/targetSdk 35 (app/build.gradle.kts), plus build-tools 34 which AGP
# pulls in automatically during the build.
"$SDKMANAGER" --sdk_root="$ANDROID_SDK_DIR" \
  "platform-tools" \
  "platforms;android-35" \
  "build-tools;35.0.0" \
  "build-tools;34.0.0" >/dev/null

echo "sdk.dir=$ANDROID_SDK_DIR" > "$CLAUDE_PROJECT_DIR/local.properties"

{
  echo "export ANDROID_HOME=$ANDROID_SDK_DIR"
  echo "export ANDROID_SDK_ROOT=$ANDROID_SDK_DIR"
  echo "export PATH=\$PATH:$ANDROID_SDK_DIR/cmdline-tools/latest/bin:$ANDROID_SDK_DIR/platform-tools"
} >> "$CLAUDE_ENV_FILE"
