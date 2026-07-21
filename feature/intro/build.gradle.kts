plugins {
    alias(libs.plugins.leaf.android.feature)
}

android {
    namespace = "io.github.jean.feature.intro"
    resourcePrefix = "intro_"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
