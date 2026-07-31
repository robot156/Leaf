plugins {
    alias(libs.plugins.leaf.kmp.feature)
}

kotlin {
    android {
        namespace = "io.github.jean.feature.imageviewer"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.image.zoomable)
        }
    }
}
