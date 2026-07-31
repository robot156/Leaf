plugins {
    alias(libs.plugins.leaf.kmp.feature)
}

kotlin {
    android {
        namespace = "io.github.jean.feature.main"

        // androidMain/AndroidManifest.xml 이 MainActivity 와 스플래시 테마를 선언한다.
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.intro)
            implementation(projects.feature.home)
            implementation(projects.feature.write)
            implementation(projects.feature.noteDetail)
            implementation(projects.feature.setting)
            implementation(projects.feature.settingTheme)
            implementation(projects.feature.settingLicense)
            implementation(projects.feature.imageViewer)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            // MainActivity — 스플래시, edge-to-edge, setContent
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.activity.compose)
            // PlatformActions 의 Android 구현 — 공유(FileProvider/ShareCompat), 링크 열기
            implementation(libs.androidx.core.ktx)
            // MainActivity 가 @ActivityKey 로 DI 그래프에 등록된다 (metrox-android 는 Android 전용)
            implementation(libs.metrox.android)
        }
    }
}
