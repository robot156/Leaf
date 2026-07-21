import org.gradle.api.Plugin
import org.gradle.api.Project

internal class MetroConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dev.zacsweers.metro")
        }
    }
}
