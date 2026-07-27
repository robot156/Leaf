package io.github.jean.leaf

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * CMP 공통 설정.
 *
 * Compose UI 계층은 Google 이 iOS 를 퍼블리시하지 않으므로 JetBrains 좌표를 쓴다
 * (`androidx.compose.runtime` 만 upstream KMP). BOM 은 없어서 아티팩트별로 버전을 명시한다.
 *
 * `compose.resources { }` 는 모듈마다 패키지가 달라 각 모듈의 build 파일에서 설정한다.
 */
internal fun Project.configureComposeMultiplatform() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        @Suppress("UnstableApiUsage")
        stabilityConfigurationFiles
            .add(isolated.rootProject.projectDirectory.file("compose-stability.conf"))
    }

    extensions.configure<KotlinMultiplatformExtension> {
        compilerOptions {
            freeCompilerArgs.addAll(
                // Enable experimental compose APIs
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
                // compose-resources 의 Res 접근자는 실험 API 로 표시돼 있다
                "-opt-in=org.jetbrains.compose.resources.ExperimentalResourceApi",
            )
        }

        sourceSets.getByName("commonMain").dependencies {
            implementation(libs.compose.runtime)
        }
    }
}
