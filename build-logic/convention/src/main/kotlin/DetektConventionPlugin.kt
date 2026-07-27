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

                // Detekt 기본 소스 탐색은 src/main/{java,kotlin} 규약만 본다.
                // KMP 모듈은 src/{commonMain,androidMain,iosMain}/kotlin 을 쓰므로
                // 기본값으로는 NO-SOURCE 로 '조용히' 스킵된다 — 분석이 사라진 걸 눈치채기 어렵다.
                // src 전체를 넘겨서 두 레이아웃을 모두 커버한다. (detekt 가 .kt/.kts 만 골라낸다)
                source.setFrom(layout.projectDirectory.dir("src"))
            }

            dependencies {
                detektPlugins(libs.detekt.formatting)
                detektPlugins(libs.detekt.compose.rule)
            }
        }
    }
}
