import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import kotlin.toString

plugins {
    alias(libs.plugins.leaf.android.application)
    alias(libs.plugins.leaf.android.application.compose)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    val localProperties = gradleLocalProperties(rootDir, providers)

    namespace = "io.github.jean.leaf"

    signingConfigs {
        create("release") {
            storeFile = file("leaf.keystore")
            storePassword = localProperties["STORE_PASSWORD"].toString()
            keyAlias = localProperties["KEY_ALIAS"].toString()
            keyPassword = localProperties["KEY_PASSWORD"].toString()
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            optimization {
                enable = false
            }
        }
    }
}

// config/libraries, config/licenses 의 커스텀 JSON을 스캔 결과에 병합한다. (폰트 등 비-의존성 고지)
aboutLibraries {
    collect {
        configPath = layout.projectDirectory.dir("config")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    // LeafViewModelFactory 의 @ContributesBinding 이 DI 그래프 생성 시점에 보여야 한다.
    implementation(projects.core.ui)

    implementation(projects.core.data.impl)
    implementation(projects.core.dataLocal.impl)
    implementation(projects.core.dataRemote.impl)

    implementation(projects.feature.main)
    implementation(projects.feature.intro)
    implementation(projects.feature.home)
    implementation(projects.feature.write)
    implementation(projects.feature.noteDetail)
    implementation(projects.feature.setting)
    implementation(projects.feature.settingTheme)
    implementation(projects.feature.settingLicense)
    implementation(projects.feature.imageViewer)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.metrox.android)
    implementation(libs.metrox.viewmodel)
}

/**
 * 프리뷰 렌더러는 프리뷰가 선언된 모듈에 있어야 해서 `leaf.kmp.library.compose` 가
 * 모든 compose KMP 모듈의 androidMain 에 넣는다 (빌드 타입이 없어 debug 한정이 불가능).
 *
 * 빌드 타입을 가진 app 에서 release 만 걷어낸다. minify 가 코드는 지워도
 * ui-tooling 의 `PreviewActivity` 는 매니페스트 병합으로 남는다.
 */
// AGP 의 variant 별 configuration 은 afterEvaluate 에 만들어져 named() 로는 못 잡는다.
configurations.matching { it.name == "releaseRuntimeClasspath" }.configureEach {
    exclude(group = "androidx.compose.ui", module = "ui-tooling")
}
