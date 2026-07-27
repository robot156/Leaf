import io.github.jean.leaf.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Compose 를 쓰지 않는 KMP 라이브러리 모듈용 convention.
 * (Compose 가 필요하면 `leaf.kmp.library.compose` 를 함께 적용한다)
 */
internal class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.kotlin.multiplatform.library")

            configureKotlinMultiplatform()
        }
    }
}
