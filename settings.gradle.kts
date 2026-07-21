pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Leaf"

include(":app")
include(":core:designsystem")
include(":core:data")
include(":core:common")
include(":core:data-local")
include(":core:data-remote")
include(":core:ui")

include(":feature:main")
include(":feature:intro")
include(":feature:home")
include(":feature:write")
include(":feature:note-detail")
include(":feature:setting")
include(":feature:setting-theme")
include(":feature:setting-license")

