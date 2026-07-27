package io.github.jean.leaf

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * KMP 공통 설정 — Android + iOS 타깃과 공통 컴파일러 옵션.
 *
 * `namespace` 는 모듈마다 다르므로 각 모듈이 `kotlin { android { namespace = "..." } }` 로 지정한다.
 * `compileSdk`/`minSdk` 는 AGP 가 android 타깃을 만드는 시점에 여기서 채워 넣는다
 * (`withType(...).configureEach` 라 타깃 생성 순서에 의존하지 않는다).
 *
 * iosX64 는 Intel 시뮬레이터용이라 Apple Silicon 에서 실행할 수 없지만,
 * 컴파일 검증 가치가 있어 타깃으로는 유지한다. (경고는 gradle.properties 에서 억제)
 */
internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<KotlinMultiplatformExtension> {
        targets.withType<KotlinMultiplatformAndroidLibraryTarget>().configureEach {
            compileSdk = LeafConfig.COMPILE_SDK
            minSdk = LeafConfig.MIN_SDK

            // commonTest 를 JVM 에서 돌리는 경로. iOS 테스트는 링킹에 Xcode 정식 설치가 필요하지만
            // host test 는 그렇지 않아서, 공용 로직 검증을 Xcode 없이도 CI/로컬에서 돌릴 수 있다.
            withHostTest {}

            compilerOptions {
                jvmTarget = JvmTarget.JVM_11
            }
        }

        iosX64()
        iosArm64()
        iosSimulatorArm64()

        // Treat all Kotlin warnings as errors (disabled by default)
        // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
        val warningsAsErrors =
            providers
                .gradleProperty("warningsAsErrors")
                .map { it.toBoolean() }
                .orElse(false)

        compilerOptions {
            allWarningsAsErrors = warningsAsErrors
            freeCompilerArgs.addAll(
                // NOTE: JVM 전용인 -Xstring-concat 은 native 컴파일에서 거부되므로 제외한다.
                "-Xconsistent-data-class-copy-visibility",
                // expect/actual class 를 경고 없이 쓰기 위해 필요 (플랫폼별 구현 다수)
                "-Xexpect-actual-classes",
                "-XXLanguage:+PropertyParamAnnotationDefaultTargetMode",
                "-opt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
        }
    }
}
