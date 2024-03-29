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

-assumenosideeffects class com.mr3y.ludi.shared.core.internal.KermitLogger {
    public *** v(...);
    public *** d(...);
    public *** e(...);
    public *** i(...);
    public *** w(...);
}

-dontwarn org.slf4j.impl.StaticLoggerBinder

-keepclasseswithmembernames class com.mr3y.ludi.datastore.model.** { *; }

# TODO: remove these rules once https://github.com/square/retrofit/issues/3751 is solved.
#-keep,allowobfuscation,allowshrinking interface retrofit2.Call
#-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Wire
-keep class com.squareup.wire.** { *; }

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile