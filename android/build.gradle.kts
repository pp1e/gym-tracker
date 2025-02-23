plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.example.gymtracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gymtracker"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.get()
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Internal common packages

    implementation(project(":common"))

    // External dependencies

//    implementation(libs.android.core.ktx)
//    implementation(libs.android.runtime.ktx)

    implementation(libs.storage)

    annotationProcessor(libs.roomCompiler)

    ksp(libs.roomCompiler)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidJunit)
    androidTestImplementation(libs.espressoCore)
    androidTestImplementation(libs.composeJunitUiTest)

    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}