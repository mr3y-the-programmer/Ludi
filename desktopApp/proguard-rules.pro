-verbose
-allowaccessmodification
-repackageclasses

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

-keepclasseswithmembernames class com.mr3y.ludi.datastore.model.** { *; }

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Wire
-keep class com.squareup.wire.** { *; }

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile