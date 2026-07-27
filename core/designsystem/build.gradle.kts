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

/**
 * compose-resources 는 `%1${'$'}d` 처럼 **인덱스가 붙은** 형식 지정자만 치환한다
 * (`SimpleStringFormatRegex = %(\d+)\$[ds]`).
 *
 * 맨 `%d`/`%s` 는 예외도 경고도 없이 그대로 화면에 노출된다.
 * Android 의 `R.string` 은 `String.format` 을 타서 동작했기 때문에 놓치기 쉽다.
 */
val checkStringFormatArgs by tasks.registering {
    val stringsFile = layout.projectDirectory.file("src/commonMain/composeResources/values/strings.xml")
    inputs.file(stringsFile)
    outputs.upToDateWhen { true }

    doLast {
        val offenders =
            Regex("""<string name="([^"]+)">(.*?)</string>""", RegexOption.DOT_MATCHES_ALL)
                .findAll(stringsFile.asFile.readText())
                .filter { Regex("""(?<!%\d{1,2}\$)%[ds]""").containsMatchIn(it.groupValues[2]) }
                .map { it.groupValues[1] }
                .toList()

        require(offenders.isEmpty()) {
            buildString {
                appendLine("인덱스가 없는 형식 지정자를 쓴 문자열이 있습니다: $offenders")
                appendLine("compose-resources 는 %1\$d / %1\$s 형태만 치환합니다.")
                append("맨 %d, %s 는 치환되지 않고 화면에 그대로 노출됩니다.")
            }
        }
    }
}

tasks.named("check") { dependsOn(checkStringFormatArgs) }
tasks.named("assemble") { dependsOn(checkStringFormatArgs) }

compose.resources {
    // core:ui 와 feature 모듈들이 Res 를 참조하므로 internal 기본값을 public 으로 바꾼다.
    publicResClass = true
    packageOfResClass = "io.github.jean.core.designsystem.generated.resources"
}
