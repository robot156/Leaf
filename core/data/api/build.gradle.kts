plugins {
    alias(libs.plugins.leaf.kmp.library)
    alias(libs.plugins.leaf.detekt)
}

kotlin {
    android {
        namespace = "io.github.jean.core.data.api"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(libs.kotlinx.coroutines)

            // Kotlin
            implementation(libs.kotlin.stdlib)
        }
    }
}
