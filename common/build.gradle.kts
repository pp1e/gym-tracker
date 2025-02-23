import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

android {
    namespace = "com.example.gymtracker.common"
    compileSdk = 35

    defaultConfig {
        minSdk = 30
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    androidTarget()
//    iosX64()
//    iosArm64()

    sourceSets {
        commonMain.dependencies {
//            implementation(libs.android.core.ktx)
//            implementation(libs.android.runtime.ktx)
            api(libs.android.activity.compose)
            implementation(libs.bundles.compose)
            implementation(libs.bundles.decompose)
            implementation(libs.bundles.mvikotlin)
            implementation(libs.bundles.reactive)
    //        implementation(libs.bundles.room)
        }
    }
}
