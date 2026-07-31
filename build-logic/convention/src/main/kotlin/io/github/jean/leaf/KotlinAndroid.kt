package io.github.jean.leaf

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        defaultConfig.apply {
            minSdk = LeafConfig.MIN_SDK
            compileSdk = LeafConfig.COMPILE_SDK
        }

        compileOptions.apply {
            sourceCompatibility = LeafConfig.javaCompileTarget
            targetCompatibility = LeafConfig.javaCompileTarget
            isCoreLibraryDesugaringEnabled = true
        }

        buildFeatures.apply {
            buildConfig = true
        }
    }

    configureKotlin()

    dependencies {
        coreLibraryDesugaring(libs.android.desugarJdkLibs)
    }
}

private fun Project.configureKotlin() =
    configure<KotlinAndroidProjectExtension> {
        // Treat all Kotlin warnings as errors (disabled by default)
        // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
        val warningsAsErrors =
            providers
                .gradleProperty("warningsAsErrors")
                .map {
                    it.toBoolean()
                }.orElse(false)
        compilerOptions.apply {
            jvmTarget = JvmTarget.JVM_11
            allWarningsAsErrors = warningsAsErrors
            freeCompilerArgs.addAll(
                "-Xconsistent-data-class-copy-visibility",
                "-Xstring-concat=inline",
                "-XXLanguage:+PropertyParamAnnotationDefaultTargetMode",
                "-opt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                // Enable experimental compose APIs
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
                "-opt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi",
            )
        }
    }
