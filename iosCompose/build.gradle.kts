import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.Family

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {
    androidTarget {
        compilerOptions.jvmTarget = JvmTarget.JVM_11
    }
//    applyDefaultHierarchyTemplate()
//    iosArm64()
//    iosSimulatorArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))

//                implementation(compose.runtime)
//                implementation(compose.foundation)
//                implementation(compose.material)
//                api(project(":common:components"))
//                api(project(":common:utils"))
//                implementation(project(":common:ui"))
            }
        }
    }

//    targets
//        .filterIsInstance<KotlinNativeTarget>()
//        .filter { it.konanTarget.family == Family.IOS }
//        .forEach { iosTarget ->
//            iosTarget.binaries.framework {
//                baseName = "GymTracker"
//                isStatic = true
//                linkerOpts.add("-lsqlite3")
//                export(project(":common:components"))
//                export(project(":common:utils"))
//                export(libs.bundles.mvikotlin)
//                export(libs.bundles.decompose)
//                implementation(libs.bundles.compose)
//                implementation(libs.bundles.decompose)
//                implementation(libs.bundles.mvikotlin)
//                implementation(libs.bundles.reactive)
//                implementation(libs.bundles.room)
//            }
//        }

    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget = JvmTarget.JVM_11
    }
}

//android {
//    namespace = "com.example.gymtracker"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.example.gymtracker"
//        minSdk = 30
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//    }
//
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//        }
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}
