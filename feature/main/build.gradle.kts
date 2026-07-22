plugins {
    alias(libs.plugins.leaf.android.feature)
}

android {
    namespace = "io.github.jean.feature.main"
}

dependencies {
    implementation(projects.feature.intro)
    implementation(projects.feature.home)
    implementation(projects.feature.write)
    implementation(projects.feature.noteDetail)
    implementation(projects.feature.setting)
    implementation(projects.feature.settingTheme)
    implementation(projects.feature.settingLicense)
    implementation(projects.feature.imageViewer)

    implementation(libs.androidx.core.splashscreen)
}
