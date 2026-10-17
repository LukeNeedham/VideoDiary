# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Shared library (com.lukeneedham:common): its logs and crashes are saved as JSON with
# kotlinx.serialization. Keep the entry classes so the Logs and Crashes pages work in minified builds.
-keep class com.lukeneedham.common.logger.LogEntry { *; }
-keep class com.lukeneedham.common.crash.CrashEntry { *; }
