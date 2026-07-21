plugins {
    alias(libs.plugins.leaf.android.library)
    alias(libs.plugins.leaf.android.library.compose)
    alias(libs.plugins.leaf.detekt)
}

android {
    namespace = "io.github.jean.core.ui"
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.animation)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.mvi)
    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)

    debugApi(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
