plugins {
    alias(libs.plugins.leaf.kmp.feature)
}

kotlin {
    android {
        namespace = "io.github.jean.feature.intro"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
