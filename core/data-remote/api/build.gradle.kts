plugins {
    alias(libs.plugins.leaf.kmp.library)
    alias(libs.plugins.leaf.detekt)
}

kotlin {
    android {
        namespace = "io.github.jean.core.dataremote.api"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)

            // Kotlin
            implementation(libs.kotlin.stdlib)
        }
    }
}
