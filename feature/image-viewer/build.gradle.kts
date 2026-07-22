plugins {
    alias(libs.plugins.leaf.android.feature)
}

android {
    namespace = "io.github.jean.feature.imageviewer"
    resourcePrefix = "image_viewer_"
}

dependencies {
    implementation(libs.image.zoomable)
}
