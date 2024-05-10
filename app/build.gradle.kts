plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.android.gms:play-services-auth:19.2.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.23")
    implementation("com.google.android.material:material:1.12.0-alpha01")

    implementation(libs.appcompat);
    implementation(libs.material);
    implementation(libs.activity);
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support.v4);
    testImplementation(libs.junit);
    androidTestImplementation(libs.ext.junit);
    androidTestImplementation(libs.espresso.core);
}
