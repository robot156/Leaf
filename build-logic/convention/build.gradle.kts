plugins {
    `kotlin-dsl`
}

group = "io.github.jean.leaf.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.detekt.gradle.plugin)
    compileOnly(libs.kotlin.compose.compiler.extension)
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
        register("androidLibrary") {
            id = "leaf.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "leaf.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureConvention") {
            id = "leaf.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
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
        register("jvmLibrary") {
            id = "leaf.jvm.library"
            implementationClass = "JvmLibraryPlugin"
        }
        register("moduleGenerator") {
            id = "leaf.module.generator"
            implementationClass = "ModuleGeneratorPlugin"
        }
    }
}
