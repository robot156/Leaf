plugins {
    alias(libs.plugins.leaf.kmp.library)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.room)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "io.github.jean.core.datalocal"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.dataLocal.api)
            implementation(projects.core.common)

            // AndroidX DataStore
            implementation(libs.androidx.datastore.preferences)

            // AndroidX Room — room-ktx 는 iOS 미퍼블리시라 runtime 만 쓴다
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // Coil
            implementation(libs.coil.compose)

            // Kotlin
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.serialization.json)

            // ETC
            implementation(libs.aboutlibraries.core)
        }
    }
}
