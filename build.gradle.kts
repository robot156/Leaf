// Top-level build file where you can add configuration options common to all sub-projects/modules.
// These plugins are declared (but not applied) here so they land on the shared
// classpath, letting the build-logic convention plugins apply them by id.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.metro) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.leaf.module.generator)
}
