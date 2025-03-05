import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ktlint)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "GymTracker"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Core
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Bundles

            implementation(libs.bundles.kotlinMultiplatformLibs)
            implementation(libs.bundles.decompose)
            implementation(libs.bundles.mvikotlin)
            implementation(libs.bundles.reactive)

            // Other

//            androidTestImplementation(libs.androidJunit)
//            androidTestImplementation(libs.espressoCore)
//            androidTestImplementation(libs.composeJunitUiTest)
//
//            debugImplementation(libs.composeUiTooling)
//            debugImplementation(libs.composeUiTestManifest)
        }

        androidMain.dependencies {
//            implementation(compose.preview)
            implementation(libs.android.activity.compose)
            implementation(libs.sqldelight.android.driver)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
    }
}

android {
    namespace = "com.example.gymtracker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.gymtracker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.compileSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.example.gymtracker.database")
        }
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidJunit)
}

// dependencies {
//    implementation(libs.android.core.ktx)
//    implementation(libs.android.runtime.ktx)
//    implementation(libs.android.activity.compose)
//    implementation(libs.bundles.compose)
//    implementation(libs.bundles.decompose)
//    implementation(libs.bundles.mvikotlin)
//    implementation(libs.bundles.reactive)
//    implementation(libs.bundles.room)
//
//    implementation(platform(libs.composeBom))
//    implementation(libs.storage)
//
//    annotationProcessor(libs.roomCompiler)
//
//    ksp(libs.roomCompiler)
//
//    testImplementation(libs.junit)
//
//    androidTestImplementation(libs.androidJunit)
//    androidTestImplementation(libs.espressoCore)
//    androidTestImplementation(platform(libs.composeBom))
//    androidTestImplementation(libs.composeJunitUiTest)
//
//    debugImplementation(libs.composeUiTooling)
//    debugImplementation(libs.composeUiTestManifest)
// }
