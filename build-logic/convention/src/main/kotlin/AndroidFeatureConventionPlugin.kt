import io.github.jean.leaf.implementation
import io.github.jean.leaf.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "leaf.android.library")
            apply(plugin = "leaf.android.library.compose")
            apply(plugin = "leaf.metro")
            apply(plugin = "leaf.detekt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                implementation(project(":core:data"))
                implementation(project(":core:common"))
                implementation(project(":core:designsystem"))
                implementation(project(":core:ui"))

                // AndroidX
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.activity.compose)
                implementation(libs.bundles.androidx.lifecycle)
                // AndroidX Navigation
                implementation(libs.androidx.navigation3.runtime)
                implementation(libs.androidx.navigation3.ui)
                // AndroidX Compose material3
                implementation(libs.androidx.compose.material3)

                // MVI
                implementation(libs.bundles.mvi)

                // DI
                implementation(libs.bundles.di)

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
