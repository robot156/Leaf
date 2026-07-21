plugins {
    alias(libs.plugins.leaf.android.library)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.room)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.github.jean.core.datalocal"
}

dependencies {
    implementation(projects.core.common)

    // AndroidX DataStore
    implementation(libs.androidx.datastore.preferences)

    // AndroidX Room
    implementation(libs.bundles.androidx.room)
    ksp(libs.androidx.room.compiler)

    // Coil
    implementation(libs.coil.compose)

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.json)

    // ETC
    implementation(libs.aboutlibraries.core)
}
