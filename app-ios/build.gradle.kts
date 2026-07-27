import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.aboutlibraries)
}

/**
 * 오픈소스 라이선스 목록을 iOS 앱 번들 리소스로 내보낸다.
 *
 * Android 는 플러그인이 `res/raw/aboutlibraries.json` 을 만들어 주지만 iOS 에는 그 경로가 없다.
 * `iosApp/iosApp/` 에 두면 Xcode 의 동기화 그룹이 번들 리소스로 자동 포함한다.
 * (읽는 쪽은 `core:data-local:impl` 의 `IosLocalBindings`)
 *
 * `app` 과 같은 커스텀 항목(폰트 등 비-의존성 고지)을 쓰도록 config 경로를 공유한다.
 */
aboutLibraries {
    collect {
        configPath = rootProject.layout.projectDirectory.dir("app/config")
    }
    export {
        outputFile = rootProject.layout.projectDirectory.file("iosApp/iosApp/aboutlibraries.json")
        prettyPrint = false
    }
}

/**
 * iOS 진입점. Xcode 가 링크할 `LeafApp.framework` 를 만든다.
 *
 * android 타깃이 없으므로 `leaf.kmp.library` 를 쓰지 않고 직접 구성한다.
 * (해당 convention 은 `com.android.kotlin.multiplatform.library` 를 함께 적용한다)
 *
 * `isStatic = true` — 동적 프레임워크는 앱 번들에 임베드/서명 단계가 더 필요하다.
 * 정적 링크가 Xcode 설정이 단순하고 실행 시 dyld 비용도 없다.
 */
kotlin {
    iosArm64()
    iosSimulatorArm64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "LeafApp"
            isStatic = true
            // Swift 에서 진입점을 부르려면 헤더에 노출돼야 한다.
            export(projects.feature.main)
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xconsistent-data-class-copy-visibility",
            "-Xexpect-actual-classes",
        )
    }

    sourceSets {
        iosMain.dependencies {
            // export 한 의존성은 api 로 선언해야 프레임워크 헤더에 나온다.
            api(projects.feature.main)

            implementation(projects.core.ui)
            implementation(projects.core.common)

            // DI 그래프가 각 impl 의 @ContributesBinding 을 보려면 여기서 의존해야 한다.
            implementation(projects.core.data.impl)
            implementation(projects.core.dataLocal.impl)
            implementation(projects.core.dataRemote.impl)

            implementation(libs.bundles.di.kmp)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
        }
    }
}
