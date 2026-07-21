plugins {
    alias(libs.plugins.leaf.android.application)
    alias(libs.plugins.leaf.android.application.compose)
    alias(libs.plugins.leaf.metro)
    alias(libs.plugins.leaf.detekt)
    alias(libs.plugins.aboutlibraries)
}

android {
    namespace = "io.github.jean.leaf"

    buildTypes {
        release {
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
    implementation(projects.core.data)
    implementation(projects.core.dataLocal)
    implementation(projects.core.dataRemote)
    implementation(projects.core.designsystem)

    implementation(projects.feature.main)
    implementation(projects.feature.intro)
    implementation(projects.feature.home)
    implementation(projects.feature.write)
    implementation(projects.feature.noteDetail)
    implementation(projects.feature.setting)
    implementation(projects.feature.settingTheme)
    implementation(projects.feature.settingLicense)

    implementation(libs.metrox.android)
    implementation(libs.metrox.viewmodel)
}
