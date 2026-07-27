plugins {
    alias(libs.plugins.leaf.kmp.library)
    alias(libs.plugins.leaf.kmp.library.compose)
    alias(libs.plugins.leaf.detekt)
}

kotlin {
    android {
        namespace = "io.github.jean.core.designsystem"

        // KMP android 타깃은 Android 리소스가 기본 비활성이다.
        // androidMain/res 에 스플래시 테마(Theme.Leaf.Splash)와 전환용 strings.xml 이 있어 켜야 한다.
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.immutable.collection)

            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.bundles.coil)

            // LeafRes 가 DrawableResource 를, ThemePreviews 가 @Preview 를 공개 API 로 노출한다.
            api(libs.compose.components.resources)
            api(libs.compose.ui.tooling.preview)
        }

        androidMain.dependencies {
            // androidMain/res 의 themes.xml 이 Theme.SplashScreen 을 부모로 쓴다.
            implementation(libs.androidx.core.splashscreen)
        }
    }
}

compose.resources {
    // core:ui 와 feature 모듈들이 Res 를 참조하므로 internal 기본값을 public 으로 바꾼다.
    publicResClass = true
    packageOfResClass = "io.github.jean.core.designsystem.generated.resources"
}
