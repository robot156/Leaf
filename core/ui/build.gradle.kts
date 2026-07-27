plugins {
    alias(libs.plugins.leaf.kmp.library)
    alias(libs.plugins.leaf.kmp.library.compose)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
}

kotlin {
    android {
        namespace = "io.github.jean.core.ui"
    }

    sourceSets {
        commonMain.dependencies {
            // LeafErrorScreen 등이 designsystem 의 컴포넌트와 Res 를 공개 API 로 노출한다.
            api(projects.core.designsystem)

            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.animation)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.mvi)
            // LeafViewModelFactory — 두 플랫폼 그래프가 함께 쓰는 DI 배선
            api(libs.bundles.di.kmp)

            // Navigator·Route·NavTransitions 가 NavKey/NavDisplay 를 공개 API 로 노출한다.
            api(libs.bundles.androidx.navigation3)
        }

        androidMain.dependencies {
            // MaskBox 의 Android 구현이 View 캡처(createBitmap/applyCanvas)와
            // ValueAnimator 의 addListener 확장을 쓴다.
            implementation(libs.androidx.core.ktx)
            // LeafBackHandler 의 Android 구현이 activity-compose 의 BackHandler 를 쓴다.
            implementation(libs.androidx.activity.compose)
        }
    }
}
