plugins {
    alias(libs.plugins.leaf.android.library)
    alias(libs.plugins.leaf.android.library.compose)
    alias(libs.plugins.leaf.detekt)
}

android {
    namespace = "io.github.jean.core.designsystem"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.immutable.collection)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.bundles.coil)

    debugApi(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
