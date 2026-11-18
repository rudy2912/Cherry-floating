plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.pinkbeat.cherry"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.pinkbeat.cherry"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
