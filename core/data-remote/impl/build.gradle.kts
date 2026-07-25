plugins {
    alias(libs.plugins.leaf.android.library)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.github.jean.core.dataremote"
}

dependencies {
    api(projects.core.dataRemote.api)

    implementation(projects.core.common)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.json)

    // Ktor
    implementation(libs.bundles.ktor)
}
