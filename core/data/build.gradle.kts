import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import io.github.jean.leaf.LeafConfig

plugins {
    alias(libs.plugins.leaf.android.library)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    val localProperties = gradleLocalProperties(rootDir, providers)
    namespace = "io.github.jean.core.data"

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"${LeafConfig.VERSION_NAME}\"")
        buildConfigField("String", "API_URL", localProperties["API_URL"].toString())
        buildConfigField("String", "API_KEY", localProperties["API_KEY"].toString())
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.dataLocal)
    implementation(projects.core.dataRemote)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.json)
}
