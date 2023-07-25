# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-verbose
-allowaccessmodification
-repackageclasses

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

-assumenosideeffects class co.touchlab.kermit.Logger {
    public final *** v(...);
    public final *** d(...);
    public final *** e(...);
    public final *** a(...);
    public final *** i(...);
    public final *** w(...);
}

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile