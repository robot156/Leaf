import androidx.room.gradle.RoomExtension
import io.github.jean.leaf.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("androidx.room")
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            // KMP 에서는 `ksp(...)` 한 줄로는 android 타깃만 처리된다.
            // Room 컴파일러는 타깃마다 돌아야 @ConstructedBy 의 actual 이 생성되므로
            // 타깃별 configuration(kspAndroid, kspIosArm64, ...)에 각각 걸어준다.
            pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                val kmp = extensions.getByType(KotlinMultiplatformExtension::class.java)
                kmp.targets.configureEach {
                    // metadata 타깃은 KSP 대상이 아니다.
                    if (name == "metadata") return@configureEach

                    val configuration = "ksp${name.replaceFirstChar { it.uppercase() }}"
                    dependencies {
                        add(configuration, libs.androidx.room.compiler)
                    }
                }
            }
        }
    }
}
