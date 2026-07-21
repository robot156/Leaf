
import io.github.jean.leaf.GenerateFeatureModuleTask
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class ModuleGeneratorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val featureNameProvider = target.providers.gradleProperty("featureName")

        val projectRootDirectory = target.layout.projectDirectory
        val templateRootDirectory = projectRootDirectory.dir("templates/feature-module")
        val settingsGradleFile = projectRootDirectory.file("settings.gradle.kts")
        val featuresRootDirectory = projectRootDirectory.dir("feature")

        target.tasks.register("generateFeatureModule", GenerateFeatureModuleTask::class.java) {
            this.featureName.set(featureNameProvider)
            this.basePackageName.set("io.github.jean.feature")
            this.templateRootDirectory.set(templateRootDirectory)
            this.outputDirectory.set(featureNameProvider.map { name -> featuresRootDirectory.dir(name) })
            this.settingsGradleFile.set(settingsGradleFile)
        }
    }
}
