plugins {
    alias(libs.plugins.leaf.kmp.library)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "io.github.jean.core.dataremote"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.dataRemote.api)

            implementation(projects.core.common)

            // Kotlin
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.serialization.json)

            // Ktor — 엔진은 플랫폼 소스셋에서 넣는다
            implementation(libs.bundles.ktor)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.ktor.client.cio)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
