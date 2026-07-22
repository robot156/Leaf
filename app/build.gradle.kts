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
    implementation(projects.feature.imageViewer)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.metrox.android)
    implementation(libs.metrox.viewmodel)
}
