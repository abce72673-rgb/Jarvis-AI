# ProGuard rules for Jarvis AI

# Keep all activities
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep all classes in com.jarvis.ai package
-keep class com.jarvis.ai.** { *; }

# Keep Kotlin metadata
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep R classes
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Suppress warnings
-dontwarn java.lang.invoke.StringConcatFactory

# Enable optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
