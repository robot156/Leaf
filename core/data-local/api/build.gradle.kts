plugins {
    alias(libs.plugins.leaf.jvm)
    alias(libs.plugins.leaf.detekt)
}

dependencies {
    api(projects.core.common)
    api(libs.kotlinx.coroutines)

    // Kotlin
    implementation(libs.kotlin.stdlib)
}
