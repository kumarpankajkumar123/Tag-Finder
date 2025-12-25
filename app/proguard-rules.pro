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

####################################
# 1. API Models (Gson / YouTube)
####################################
-keep class com.example.tagfinderapp.Model.** { *; }

####################################
# 2. ViewModel (reflection)
####################################
-keep class * extends androidx.lifecycle.ViewModel

####################################
# 3. Retrofit interfaces
####################################
-keep interface com.example.tagfinderapp.network.** { *; }

####################################
# 4. Gson – annotation based (BEST)
####################################
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

####################################
# 5. Repository (members only)
####################################
-keepclassmembers class com.example.tagfinderapp.Repository.** {
    public *;
}

####################################
# 6. Retrofit / OkHttp core
####################################
-keepattributes Signature
-keepattributes *Annotation*

####################################
# Retrofit – minimal required
####################################
-keepattributes Signature
-keepattributes *Annotation*

# Keep only Retrofit interfaces (your APIs)
-keep interface com.example.tagfinderapp.network.** { *; }

# Retrofit annotations
-keep class retrofit2.http.** { *; }

####################################
# OkHttp – no keep needed
####################################
-dontwarn okhttp3.**

####################################
# 7. Firebase + Ads
####################################
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.ads.**

####################################
# 8. Picasso
####################################
-dontwarn com.squareup.picasso.**

####################################
# 9. Kotlin / Coroutines
####################################
-keep class kotlin.Metadata { *; }
-dontwarn kotlinx.coroutines.**

####################################
# ❌ INTENTIONALLY NOT KEPT
# adapter, ui, fragments, util, base, constants
####################################
