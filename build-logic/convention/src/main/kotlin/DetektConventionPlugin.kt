import io.github.jean.leaf.detektPlugins
import io.github.jean.leaf.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention for Detekt static analysis: applies the Detekt plugin, points it at
 * the shared rule set in `config/detekt/detekt.yaml`, and wires in the formatting
 * and Compose rule plugins.
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            val detektConfigFile = files("$rootDir/config/detekt/detekt.yaml")

            extensions.configure<DetektExtension> {
                buildUponDefaultConfig = true
                parallel = true
                autoCorrect = true
                config.setFrom(detektConfigFile)
            }

            dependencies {
                detektPlugins(libs.detekt.formatting)
                detektPlugins(libs.detekt.compose.rule)
            }
        }
    }
}
