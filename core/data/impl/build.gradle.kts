import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import io.github.jean.leaf.LeafConfig

plugins {
    alias(libs.plugins.leaf.kmp.library)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
}

/**
 * KMP android 라이브러리 타깃은 `buildConfig` 를 지원하지 않으므로(빌드 타입도 없다)
 * 빌드 시점 상수를 commonMain 소스로 직접 생성한다.
 *
 * 빌드 타입에 따라 갈리는 `DEBUG` 는 여기서 만들 수 없다 —
 * 진입점이 [io.github.jean.core.common.env.BuildFlags] 로 주입한다.
 *
 * API_URL/API_KEY 는 local.properties 값에 이미 따옴표가 포함돼 있어 그대로 끼워 넣는다.
 * (기존 buildConfigField 호출도 따옴표를 덧붙이지 않았다)
 */
val generateLeafBuildConfig by tasks.registering {
    val localProperties = gradleLocalProperties(rootDir, providers)
    val apiUrl = localProperties["API_URL"].toString()
    val apiKey = localProperties["API_KEY"].toString()
    val versionName = LeafConfig.VERSION_NAME
    val outputDir = layout.buildDirectory.dir("generated/leafBuildConfig/kotlin")

    inputs.property("apiUrl", apiUrl)
    inputs.property("apiKey", apiKey)
    inputs.property("versionName", versionName)
    outputs.dir(outputDir)

    doLast {
        val packageDir = outputDir.get().asFile.resolve("io/github/jean/core/data/env")
        packageDir.mkdirs()
        packageDir.resolve("LeafBuildConfig.kt").writeText(
            """
            |// generateLeafBuildConfig 태스크가 생성한 파일이다. 직접 수정하지 말 것.
            |package io.github.jean.core.data.env
            |
            |internal object LeafBuildConfig {
            |    const val API_URL: String = $apiUrl
            |    const val API_KEY: String = $apiKey
            |    const val VERSION_NAME: String = "$versionName"
            |}
            |
            """.trimMargin(),
        )
    }
}

kotlin {
    android {
        namespace = "io.github.jean.core.data"
    }

    sourceSets {
        commonMain {
            kotlin.srcDir(generateLeafBuildConfig)

            dependencies {
                api(projects.core.data.api)

                implementation(projects.core.common)
                implementation(projects.core.dataLocal.api)
                implementation(projects.core.dataRemote.api)

                // Kotlin
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines)
            }
        }
    }
}
