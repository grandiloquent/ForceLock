plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "psycho.euphoria.forcelock"
    compileSdk = 34

    defaultConfig {
        applicationId = "psycho.euphoria.forcelock"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.work:work-runtime-ktx:2.9.0")
}