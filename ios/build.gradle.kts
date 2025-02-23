import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.Family

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose)
}

kotlin {
    applyDefaultHierarchyTemplate()
    iosArm64()
    iosSimulatorArm64()

    targets
        .filterIsInstance<KotlinNativeTarget>()
        .filter { it.konanTarget.family == Family.IOS }
        .forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "GymTracker"
                isStatic = true
                linkerOpts.add("-lsqlite3")
                export(project(":common:components"))
                export(project(":common:utils"))
                export(libs.bundles.mvikotlin)
                export(libs.bundles.decompose)
//                implementation(libs.bundles.compose)
//                implementation(libs.bundles.decompose)
//                implementation(libs.bundles.mvikotlin)
//                implementation(libs.bundles.reactive)
//                implementation(libs.bundles.room)
            }
        }

    sourceSets {
        val commonMain by getting {
            dependencies {
//                implementation(libs.bundles.compose)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                api(project(":common:components"))
                api(project(":common:utils"))
//                implementation(project(":common:ui"))
            }
        }
    }

    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget = JvmTarget.JVM_11
    }
}