plugins {
    `kotlin-dsl`
}

group = "io.github.jean.leaf.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.detekt.gradle.plugin)
    compileOnly(libs.kotlin.compose.compiler.extension)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "leaf.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "leaf.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("room") {
            id = "leaf.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("metro") {
            id = "leaf.metro"
            implementationClass = "MetroConventionPlugin"
        }
        register("detekt") {
            id = "leaf.detekt"
            implementationClass = "DetektConventionPlugin"
        }
        register("kmpLibrary") {
            id = "leaf.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpLibraryCompose") {
            id = "leaf.kmp.library.compose"
            implementationClass = "KmpLibraryComposeConventionPlugin"
        }
        register("kmpFeature") {
            id = "leaf.kmp.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
        register("moduleGenerator") {
            id = "leaf.module.generator"
            implementationClass = "ModuleGeneratorPlugin"
        }
    }
}
