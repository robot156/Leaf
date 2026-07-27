import io.github.jean.leaf.configureComposeMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Compose 를 쓰는 KMP 라이브러리 모듈용 convention.
 * `leaf.kmp.library` 와 함께 적용한다.
 */
internal class KmpLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            pluginManager.apply("org.jetbrains.compose")

            configureComposeMultiplatform()
        }
    }
}
