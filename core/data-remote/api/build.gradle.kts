plugins {
    alias(libs.plugins.leaf.jvm)
    alias(libs.plugins.leaf.detekt)
}

dependencies {
    api(projects.core.common)

    // Kotlin
    implementation(libs.kotlin.stdlib)
}
