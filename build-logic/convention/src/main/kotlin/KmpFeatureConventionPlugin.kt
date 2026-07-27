import io.github.jean.leaf.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * KMP feature 모듈용 convention. `leaf.android.feature` 의 KMP 대응판.
 */
internal class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "leaf.kmp.library")
            apply(plugin = "leaf.kmp.library.compose")
            apply(plugin = "leaf.metro")
            apply(plugin = "leaf.detekt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(project(":core:data:api"))
                    implementation(project(":core:common"))
                    implementation(project(":core:designsystem"))
                    implementation(project(":core:ui"))

                    // AndroidX (2.11.0 부터 전 아티팩트가 iOS 를 퍼블리시한다)
                    implementation(libs.bundles.androidx.lifecycle)
                    // Navigation3 — ui 는 JetBrains 포크
                    implementation(libs.bundles.androidx.navigation3)

                    // Compose
                    implementation(libs.compose.foundation)
                    implementation(libs.compose.material3)
                    implementation(libs.compose.ui)

                    // MVI
                    implementation(libs.bundles.mvi)

                    // DI — metrox-android 는 Android 전용이라 제외
                    implementation(libs.bundles.di.kmp)

                    // Kotlin
                    implementation(libs.kotlin.stdlib)
                    implementation(libs.kotlinx.coroutines)
                    implementation(libs.kotlinx.datetime)
                    implementation(libs.kotlinx.immutable.collection)
                    implementation(libs.kotlinx.serialization.json)

                    // ETC
                    implementation(libs.bundles.coil)
                }
            }
        }
    }
}
