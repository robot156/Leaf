# Leaf — Compose Multiplatform 마이그레이션 계획

> 대상 플랫폼: **Android + iOS**
> 방식: **bottom-up 단계적 전환** (각 단계마다 Android 빌드 통과 유지)
> Android 전용 기능: **expect/actual 추상화** (기능 손실 없음)

---

## 1. 의존성 KMP 호환성 검증 결과

Maven Central / Google Maven 메타데이터를 직접 조회해 iOS 타깃 퍼블리시 여부를 확인했다.
(추측이 아니라 `.module` 파일의 `org.jetbrains.kotlin.native.target` 기준)

### 그대로 쓸 수 있는 것 (좌표 변경 불필요)

| 의존성 | 현재 버전 | iOS 타깃 |
|---|---|---|
| `org.orbit-mvi:orbit-core / -viewmodel / -compose` | 12.0.0 | ✅ arm64, simulatorArm64, x64 |
| `dev.zacsweers.metro:metrox-viewmodel / -compose` | 1.3.2 | ✅ |
| `io.coil-kt.coil3:coil-compose / -network-ktor3` | 3.5.0 | ✅ |
| `net.engawapg.lib:zoomable` | 2.13.0 | ✅ |
| `com.mikepenz:aboutlibraries-core` | 15.0.4 | ✅ |
| `org.jetbrains.kotlinx:*` (datetime, serialization, coroutines, immutable) | — | ✅ |
| `androidx.room:room-runtime` | 2.8.4 | ✅ (Room KMP) |
| `androidx.datastore:datastore-preferences(-core)` | 1.2.1 | ✅ |
| `androidx.annotation:annotation` | — | ✅ |
| `io.ktor:ktor-client-*` | 3.5.1 | ✅ (`ktor-client-darwin` 추가 필요) |
| `androidx.lifecycle:*` (runtime-compose, viewmodel, viewmodel-compose, viewmodel-savedstate, viewmodel-navigation3) | 2.11.0 | ✅ **전부 iOS 퍼블리시 — 좌표 변경 불필요** |
| `androidx.navigation3:navigation3-runtime` | 1.1.4 | ✅ (runtime 은 upstream KMP) |
| `androidx.compose.runtime:runtime` | — | ✅ (upstream KMP) |
| `androidx.savedstate:savedstate-compose` | 1.4.0 | ✅ |

### 좌표를 갈아타야 하는 것

Google이 퍼블리시하는 `androidx.compose.ui / foundation / material3` 는 **iOS 타깃이 없다**
(`android` + `jvmStubs` + `linuxx64Stubs` 뿐). `androidx.compose.runtime` 만 upstream KMP.
따라서 UI 계층은 JetBrains 포크를 써야 한다.

규칙: **upstream `androidx.*` 가 iOS 를 퍼블리시하면 그대로 쓰고, 없을 때만 JetBrains 포크로 갈아탄다.**
갈아타야 하는 건 결국 **Compose UI 계층 + navigation3-ui** 뿐이다.

| 현재 | 전환 후 |
|---|---|
| `androidx.compose:compose-bom` (BOM) | BOM 없음 → 아티팩트별 버전 명시 |
| `androidx.compose.ui:ui`, `:ui-tooling-preview` | `org.jetbrains.compose.ui:ui`, `:ui-tooling-preview` |
| `androidx.compose.runtime:runtime` | `org.jetbrains.compose.runtime:runtime` (relocation 아티팩트 — Android 변형은 내부적으로 upstream 을 가리킴) |
| `androidx.compose.foundation:foundation` | `org.jetbrains.compose.foundation:foundation` |
| `androidx.compose.animation:animation` | `org.jetbrains.compose.animation:animation` |
| `androidx.compose.material3:material3` | `org.jetbrains.compose.material3:material3` ⚠️ **별도 버전 라인** |
| `androidx.navigation3:navigation3-ui` | `org.jetbrains.androidx.navigation3:navigation3-ui` |
| `androidx.compose.ui:ui-tooling-preview` | `org.jetbrains.compose.ui:ui-tooling-preview` — **패키지는 그대로** (아래 참고) |
| `R.drawable` / `R.string` / `res/font` | `compose-resources` (`Res.drawable.*`, `Res.string.*`) |
| *`androidx.lifecycle:*`, `androidx.navigation3:navigation3-runtime`* | *변경 없음* |

> `androidx.navigation3:navigation3-ui:1.1.4` 는 iOS 미퍼블리시(`android` + `jvmStubs` + `linuxx64Stubs`)임을 확인.
> 반면 `navigation3-runtime` 은 iOS 가 있고, JetBrains 는 `navigation3-ui` 만 포크한다
> (`org/jetbrains/androidx/navigation3/` 디렉터리에 `navigation3-ui` 만 존재).
> Compose 자체와 동일한 **runtime=upstream / UI=포크** 구조.

### 버전 세트 (CMP 1.11.1 stable 기준)

CMP 는 아티팩트 그룹별로 버전 라인이 갈라져 있어 릴리스 노트의 조합을 그대로 따라야 한다.

```
composeMultiplatform      = 1.11.1          # org.jetbrains.compose.{runtime,ui,foundation,animation,components}
composeMaterial3          = 1.11.0-alpha07  # org.jetbrains.compose.material3   ← 버전 라인 다름
jbNavigation3             = 1.1.1           # org.jetbrains.androidx.navigation3:navigation3-ui
androidxSqlite            = 2.7.0           # androidx.sqlite:sqlite-bundled    ← room 과 버전 라인 다름
```

> `jb navigation3-ui:1.1.1` 의 pom 은 `androidx.navigation3:navigation3-runtime:1.1.1` 을 참조하지만
> 프로젝트는 `1.1.4` 를 쓴다. 같은 1.1.x 라인이라 Gradle 이 1.1.4 로 올려 해소한다 — 빌드로 실측 확인 필요.

대안: CMP `1.12.0-beta02` (Kotlin 2.4 정렬, material3 `1.12.0-alpha04`,
lifecycle `2.11.0-rc02`, navigation3 `1.2.0-alpha03`).
현재 프로젝트가 Kotlin 2.4.10 / AGP 9.2.1 로 최신을 따르고 있어,
**1.11.1 로 시작해서 Compose 컴파일러 비호환이 나오면 1.12.0-beta02 로 올린다.**

CMP Gradle 플러그인은 KGP >= 2.0 만 요구하고 별도 Kotlin 버전 게이트는 없음(플러그인 jar 확인).

---

## 2. Android 전용 코드 인벤토리

전체 195개 `.kt` 중 Android 전용 API를 쓰는 파일은 **11개**뿐. 이 부분만 expect/actual 처리한다.

| 파일 | Android API | 전환 방침 |
|---|---|---|
| `core/data/impl/.../env/AndroidEnvironment.kt` | `Build.VERSION`, `BuildConfig` | `expect class PlatformEnvironment` + 빌드 시 생성되는 `LeafBuildConfig` |
| `core/data-local/impl/.../datastore/PreferenceStorageImpl.kt` | `preferencesDataStore(Context)` | `expect fun createDataStore()` — okio path 기반 |
| `core/data-local/impl/.../image/ImageCacheDataSourceImpl.kt` | `SingletonImageLoader.get(Context)` | Coil3 는 KMP — `PlatformContext` 로 교체 |
| `core/data-local/impl/.../license/LicenseDataSourceImpl.kt` | assets 읽기 | compose-resources 또는 `expect fun readLicenseJson()` |
| `core/data-local/impl/.../database/LeafDatabase.kt` | `Room.databaseBuilder(Context)` | `expect fun databaseBuilder()` + `sqlite-bundled` |
| `core/data-remote/impl/.../util/HtmlText.kt` | `android.text.Html` | `expect fun unescapeHtml()` — iOS 는 순수 Kotlin 구현 |
| `core/ui/.../navigation/NavTransitions.kt` | `AccelerateDecelerateInterpolator` | Compose `Easing` 으로 대체 (expect 불필요) |
| `core/ui/.../animation/MaskBox.kt` | `ValueAnimator`, `android.graphics.*` | Compose `Animatable` + `DrawScope` 로 대체 |
| `feature/note-detail` 이미지 공유 | `FileProvider`, `ShareCompat`, `Bitmap` | `expect fun shareImage()` — iOS 는 `UIActivityViewController` |
| `feature/main/MainActivity.kt` | `ComponentActivity`, `enableEdgeToEdge`, splashscreen | androidApp 에만 존치 + iOS `ComposeUIViewController` 진입점 신설 |
| `app/LeafApplication.kt`, `di/AndroidBindings.kt` | `Application`, Crashlytics | androidApp 에만 존치, Crashlytics 는 `expect object CrashReporter` |

리소스: `R.*` 참조 약 100개. drawable/font 는 `core/designsystem` 에 집중되어 있고
`LeafRes` 가 이미 간접 지점 역할을 하므로 `DrawableResource` 타입으로 바꾸면 사용처는 대부분 무변경.

---

## 3. 진행 순서 (bottom-up)

각 단계 종료 시 `./gradlew assembleDebug` 통과 + 커밋.

| # | 단계 | 내용 | 상태 |
|---|---|---|---|
| 0 | 빌드 인프라 | `leaf.kmp.library` convention plugin, `configureKotlinMultiplatform`, `libs.versions.toml` 좌표 추가 | ✅ 완료 |
| 1 | `core:common` + `*/api` | KMP 전환 (순수 Kotlin 계층) | ✅ 완료 |
| 2 | `core:data-remote:impl` | Ktor 엔진 자동 탐색, `HtmlText` 순수 Kotlin 재작성 + 테스트 | ✅ 완료 |
| 3 | `core:data-local:impl` | Room KMP + `sqlite-bundled`, DataStore okio path, license/image | ✅ 완료 |
| 4 | `core:data:impl` | `Environment` expect/actual + `LeafBuildConfig` 생성 태스크 | ✅ 완료 |
| 5 | `core:designsystem` | compose-resources 전환, material3·Preview 좌표 교체 | ✅ 완료 |
| 6 | `core:ui` | MVIViewModel / Navigator / NavTransitions / MaskBox | ✅ 완료 |
| 7 | `feature/*` (9개) | 전 모듈 KMP 전환, `R.string` 76곳 → compose-resources, 플랫폼 액션 분리 | ✅ 완료 |
| 8 | 진입점 | `:app-ios` 프레임워크 + `iosApp` Xcode 프로젝트, `ComposeUIViewController` | ✅ 완료 |
| 9 | 정리 | 죽은 convention plugin·catalog 항목 제거, 템플릿·README 갱신 | ✅ 완료 |

### 9단계에서 제거한 것

feature 전환으로 쓰이지 않게 된 것들을 정리했다.

- convention plugin 4개: `leaf.android.library`, `leaf.android.library.compose`,
  `leaf.android.feature`, `leaf.jvm.library` (+ `configureKotlinJvm`)
- catalog: `androidx-compose-{animation,ui,ui-tooling-preview,material3}`,
  `androidx-lifecycle-runtime-ktx`, `androidx-room-ktx`,
  번들 `androidx-room`·`detekt`, 플러그인 `jetbrains-kotlin-jvm`·`android-library`
  (`detekt` 번들은 마이그레이션 전부터 미사용이었다)
- `templates/feature-module` 을 KMP 레이아웃으로 (`src/commonMain/kotlin`, 매니페스트 제거,
  `leaf.kmp.feature`). `GenerateFeatureModuleTask` 도 경로를 맞추고,
  생성 후 `LeafNavConfiguration` 등록을 잊지 않도록 안내 로그를 추가했다.
  실제로 생성해 iOS 컴파일까지 통과하는지 확인했다.
- 마이그레이션과 무관하게 남아 있던 kotlinx-datetime deprecation 2건
  (`monthNumber`, `dayOfMonth`) 도 함께 정리했다.

**남겨둔 것**: `app` 의 `leaf.android.application.compose`.
`app` 에는 현재 `@Composable` 이 없어 형식상 불필요하지만, 제거하면 release 빌드의
Compose 컴파일러 참여가 바뀌고 이 환경에서는 release 빌드(서명·google-services 필요)를
검증할 수 없어 손대지 않았다.

### 0-2 단계에서 확정된 사항

- **AGP 9.2.1 의 KMP Android DSL**: `kotlin { androidLibrary {} }` 는 deprecated.
  `kotlin { android {} }` 를 쓴다. `compileSdk`/`minSdk` 는 convention plugin 이
  `targets.withType<KotlinMultiplatformAndroidLibraryTarget>().configureEach` 로 채우고,
  `namespace` 만 각 모듈이 지정한다.
- **JVM 모듈은 KMP 모듈을 소비할 수 없다**: `leaf.jvm.library` 모듈은
  `platform.type=jvm` 변형을 요구하는데 KMP 모듈은 android/native 만 낸다.
  `core:common` 을 전환하는 순간 `core/*/api` 세 모듈도 같이 전환해야 빌드가 유지된다.
- **`-Xstring-concat=inline` 은 JVM 전용**이라 native 컴파일에서 거부된다. KMP 공통 옵션에서 제외.
- **detekt 가 KMP 소스셋을 못 본다**: 기본 탐색이 `src/main/{java,kotlin}` 뿐이라
  `NO-SOURCE` 로 조용히 스킵된다. `source` 를 `src` 전체로 지정해 두 레이아웃을 모두 커버.
- **AndroidManifest 위치**: KMP android 타깃은 `src/androidMain/AndroidManifest.xml`.
- **테스트 실행 경로**: android 타깃에 `withHostTest {}` 를 켜서 `commonTest` 를
  `testAndroidHostTest`(JVM) 로 돌린다. iOS 테스트는 링킹이 필요해 Xcode 없이는 못 돌린다.
- **`iosX64` 타깃은 넣지 않는다**: `coil3`, `aboutlibraries-core`,
  `androidx.sqlite:sqlite-bundled` 가 `ios_x64` 를 퍼블리시하지 않아 의존성 해석이 깨진다.
  Intel Mac 시뮬레이터용이라 Apple Silicon 에서는 실행도 안 되므로 실익이 없다.
  → 타깃은 `iosArm64` + `iosSimulatorArm64` 둘만.
- **`Dispatchers.IO` 는 commonMain 에서 못 쓴다**: kotlinx-coroutines 가 `concurrent`
  소스셋에만 선언한다. 게다가 Kotlin/Native 에서는 아직 `internal` 이라 iosMain 에서도 참조 불가.
  → `core:common` 의 `expect val ioDispatcher` 로 추상화 (Android=`Dispatchers.IO`, iOS=`Dispatchers.Default`).
- **KMP android 라이브러리 타깃에는 `buildConfig` 도 빌드 타입도 없다**(단일 variant).
  `BuildConfig.DEBUG` / `buildConfigField` 를 쓸 수 없다.
  → 빌드 시점 상수(API_URL·API_KEY·VERSION_NAME)는 `generateLeafBuildConfig` 태스크가
  commonMain 소스로 생성하고, 빌드 타입에 따라 갈리는 `isDebug` 는 진입점(app)이
  `BuildFlags` 로 그래프에 주입한다. 라이브러리 모듈에서는 variant 를 알 방법이 없다.
- **detekt 는 매 단계 전체(`./gradlew detekt`)로 돌린다.** 모듈 단위로만 돌리면
  다른 모듈에 남은 위반을 놓친다 (2단계의 `BracesOnWhenStatements` 를 4단계에서 발견).
### 5단계에서 확정된 사항 (CMP·리소스)

- **`@Preview` 는 import 를 바꿀 필요가 없다.** `org.jetbrains.compose.ui:ui-tooling-preview` 는
  `androidx.compose.ui.tooling.preview` 패키지를 **그대로** commonMain 에 제공한다.
  `name`/`uiMode`/`backgroundColor`/`showBackground`, `PreviewParameter(Provider)` 전부 있다.
  Android 전용이던 `Configuration.UI_MODE_NIGHT_*` 만
  `androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_*` 로 바꾸면 된다 —
  프리뷰 충실도 손실 없음.
- **KMP android 타깃은 Android 리소스가 기본 비활성**이다. `androidResources.enable = true`
  를 켜야 `src/androidMain/res` 가 인식된다.
- **Android 플랫폼 리소스는 compose-resources 로 옮기면 안 된다.** 스플래시 테마
  (`Theme.Leaf.Splash`, `splash_background`, `values-night`)는 `feature/main` 의 매니페스트가
  `@style/` 로 참조하므로 `androidMain/res` 에 남는다. compose-resources 는 Compose 코드에서만 읽힌다.
- **`compose-resources` 의 `Res` 는 기본 `internal`** 이다. 다른 모듈이 쓰려면
  `compose.resources { publicResClass = true }` 가 필요하다.
- **`Font()` 가 `@Composable` 로 바뀐다.** Android 의 `Font(R.font.x)` 는 top-level `val` 에서
  됐지만 compose-resources 의 `Font(Res.font.x)` 는 컴포지션 안에서만 호출할 수 있다.
  `LeafSerif`/`LeafSans`/`LeafTypographyDefault` 를 `rememberLeafTypography()` 로 합쳤다.
  `LocalLeafTypography` 가 `staticCompositionLocalOf` 라서 `remember` 없이 매번 새 인스턴스를
  주면 하위 전체가 재구성된다.
- **`PlatformTextStyle(includeFontPadding = ...)` 은 Android 전용 파라미터**다.
  → `koreanPlatformTextStyle()` expect/actual (iOS 는 `null`).
- **`String.format` 은 JVM 전용**이라 `padStart` 등으로 바꿔야 한다.
- **빌드 타입이 없어 `debugImplementation` 을 못 쓴다.** 프리뷰 렌더링용 `ui-tooling` 은
  빌드 타입이 있는 `app` 에서 `debugImplementation` 으로 넣는다.
- **문자열은 7단계까지 이중 관리**다. feature 들이 아직 `R.string.*`(81곳/23파일)을 쓰므로
  `androidMain/res/values/strings.xml` 에 사본을 두고 컴파일을 유지한다.
  정본은 `commonMain/composeResources/values/strings.xml` — 7단계에서 사본을 삭제한다.
  drawable·font 는 사용처가 designsystem 내부(`LeafTheme.res` 경유)뿐이라 사본 없이 단일화했다.
- **JetBrains Compose 와 androidx Compose 는 Android 에서 혼용 가능**하다.
  `org.jetbrains.compose.ui:ui` 의 android variant 가 `androidx.compose.ui:ui` 를 의존하므로
  클래스가 중복되지 않는다. 덕분에 designsystem 만 먼저 CMP 로 옮기고 feature 는 나중에 옮길 수 있다.

### 6단계에서 확정된 사항 (core:ui)

- **`navigation3-ui` 도 패키지가 `androidx.navigation3.ui` 그대로다.** JetBrains 포크로
  좌표만 바꾸면 되고 import 변경은 없다. `iosMain/NavDisplay.ios.kt` 도 있어 실제 iOS 구현이 있다.
- ⚠️ **`rememberNavBackStack` 의 설정 없는 오버로드는 Android 전용**이다.
  리플렉션으로 `NavKey` 서브타입을 찾기 때문에 공용 코드에서 쓸 수 없다.
  → `routeSavedStateConfiguration { }` 으로 **모든 Route 를 명시적으로 등록**해야 한다.
  Route 는 feature 마다 흩어져 있어 전부를 아는 `MainNavHost` 에서 등록한다.
  **등록 누락은 컴파일에 걸리지 않고, 프로세스 사망 후 백스택 복원 시점에 예외로 터진다.**
  7단계에서 `feature:main` 이 KMP 가 되면 commonTest 로 회귀 테스트를 붙일 것.
- **`MaskBox`(테마 전환 원형 리빌)는 expect/actual 로 분리했다.**
  전환 직전 화면을 **동기적으로** 캡처해야 하는데 Android 는 `View.draw(Canvas)` 로 되지만
  CMP 의 `GraphicsLayer.toImageBitmap()` 은 `suspend` 라 같은 타이밍을 보장할 수 없다.
  캡처가 늦으면 이미 바뀐 새 화면을 찍어 효과가 무의미해진다.
  → 검증된 Android 구현을 그대로 두고, iOS 는 애니메이션 없이 즉시 전환(TODO).
- **`androidx.lifecycle.ViewModel`, orbit-mvi 는 무수정**으로 commonMain 에서 동작한다.

### 7단계에서 확정된 사항 (feature 9개)

- **`BackHandler`(activity-compose)는 Android 전용.** 공용 대체는
  `androidx.navigationevent.compose.NavigationBackHandler` 인데 `NavigationEventState` 를
  hoist 해야 한다. `core:ui` 의 `LeafBackHandler` expect/actual 로 한 번만 감쌌다
  (Android 는 검증된 activity-compose 구현 유지).
- **`metrox-android` 는 Android 전용**이라 `di` 번들을 commonMain 용 `di-kmp` 로 분리했다.
  `MainActivity` 의 `@ActivityKey` 등록 때문에 `feature:main` 의 androidMain 에는 필요하다.
- **`resourcePrefix` 는 Android 리소스 DSL** — feature 에 Android res 가 없어져서 전부 제거했다.
- **`feature:main` 만 `androidResources.enable = true`** 가 필요하다
  (`MainActivity` 와 스플래시 테마를 선언하는 매니페스트 때문).
- **앱 밖으로 나가는 동작은 `PlatformActions` 로 모았다** (링크 열기 · 문의 메일 · 이미지 공유).
  `@Composable expect fun rememberPlatformActions()` — Android 구현은 `LocalContext` 가
  필요해 컴포지션 안에서만 만들 수 있다.
  iOS 는 링크·메일은 `UIApplication.openURL` 로 동작하고,
  **이미지 공유는 `UIActivityViewController` 표시에 루트 `UIViewController` 참조가 필요해 8단계로 미뤘다.**
- **6단계에서 미룬 Route 등록 회귀 테스트를 붙였다.** `feature:main` 이 KMP 가 되어
  commonTest 를 쓸 수 있게 됐다. 등록을 하나 지우면 실제로 실패하는지까지 확인했다.
- **detekt autoCorrect 는 한 번에 다 못 고친다.** 모듈 단위로 수정하고 그 회차는 실패로
  끝내므로, 위반이 여러 모듈에 걸쳐 있으면 `./gradlew detekt` 를 여러 번 돌려야 수렴한다.

- **DI 그래프에 서드파티 타입을 키로 노출하지 않는다**: 그래프를 생성하는 app 모듈이
  그 타입을 해석하지 못한다(기존 `PreferenceStorageImpl` 주석에 있던 제약).
  → 플랫폼별 생성이 필요한 것은 androidMain/iosMain 의 binding container 가
  **api 계층 인터페이스**(`PreferenceStorage`, `LicenseDataSource`, …)로 제공하고,
  로직은 commonMain 에 둔다. `DataStore<Preferences>`·`RoomDatabase.Builder` 는 그래프에 등장하지 않는다.

---

### 8단계에서 확정된 사항 (iOS 진입점)

- **구조**: `:app-ios`(Kotlin, `LeafApp.framework` 산출) + `iosApp/`(Xcode 프로젝트).
  `:app-ios` 는 android 타깃이 없어 `leaf.kmp.library` 를 쓸 수 없다
  (그 convention 은 `com.android.kotlin.multiplatform.library` 를 함께 적용한다) → 직접 구성.
- **컴포지션 루트를 공용화**: `MainActivity` 의 `setContent` 내용을 `feature:main` 의
  `LeafApp(viewModelFactory, onDarkThemeChanged)` 로 뽑았다.
  Android 만 `onDarkThemeChanged` 로 `enableEdgeToEdge` 를 다시 호출한다.
- **`AppViewModelFactory` → `core:ui` 의 `LeafViewModelFactory`**.
  두 플랫폼 그래프가 모두 이 `@ContributesBinding` 을 필요로 한다.
  그래프를 만드는 모듈의 **컴파일 클래스패스에 보여야** 하므로 `app` 에 `core:ui` 를 직접 의존으로 추가했다
  (feature 경유 `implementation` 은 노출되지 않아 `[Metro/MissingBinding]` 이 났다).
- **iOS 그래프는 `MetroAppComponentProviders` 를 상속하지 않는다** — metrox-android 는 Android 전용이고
  iOS 에는 Activity 개념이 없다. `IosAppGraph : ViewModelGraph` 로 충분하다.
  `isDebug` 는 Kotlin 이 알 수 없어 Swift 의 `#if DEBUG` 가 `createIosAppGraph(isDebug:)` 로 넘긴다.
- ⚠️ **`Info.plist` 에 `CADisableMinimumFrameDurationOnPhone` 이 없으면 앱이 즉시 죽는다.**
  CMP 의 `PlistSanityCheck` 가 실행 시점에 검사하고 `error()` 를 던진다(SIGABRT).
  고주사율 기기 성능에 직접 영향이 있어 체크를 끄는 대신 키를 추가했다.
- **Xcode 프로젝트는 objectVersion 77(Xcode 16+) 의 동기화 그룹**(`PBXFileSystemSynchronizedRootGroup`)을 쓴다.
  파일을 pbxproj 에 하나씩 등록하지 않아 관리가 훨씬 쉽다. 대신 `Info.plist` 를 동기화 폴더 안에 두면
  리소스로도 복사되어 `Multiple commands produce ... Info.plist` 로 실패한다 → 폴더 밖(`iosApp/Info.plist`)에 둔다.
- **`ENABLE_USER_SCRIPT_SANDBOXING = NO`** 가 필요하다. 켜져 있으면 빌드 스크립트가
  Gradle 출력 디렉터리에 쓸 수 없다.
- **라이선스 목록**: `:app-ios:exportLibraryDefinitions` 가 `iosApp/iosApp/aboutlibraries.json` 을 만들고
  동기화 그룹이 번들 리소스로 포함한다. iOS 는 `NSBundle.pathForResource` 로 읽는다.
  **이 JSON 은 커밋한다** — Xcode 는 빌드 계획 시점에 파일 목록을 확정하므로,
  없는 상태로 시작하면 첫 빌드에서 리소스에 포함되지 않는다.
- **이미지 공유**: `ImageBitmap` → `asSkiaBitmap()` → PNG → `UIImage` → `UIActivityViewController`.
  이미 present 된 컨트롤러가 있으면 그 위에 올려야 무시되지 않는다.

## 3-1. 환경 제약 — iOS 링킹 (해결됨)

1-7·9 단계는 Command Line Tools 만으로 진행했다. klib 컴파일은 되지만 **네이티브 링킹**
(`linkDebugTest*`, `linkDebugFramework*`)은 Xcode 정식 설치가 필요해 8단계와 iOS 테스트 실행이 막혀 있었다.

Xcode 26.6 설치 후 해소되어 8단계를 완료했다. 확인된 것:

| 항목 | 결과 |
|---|---|
| `linkDebugFrameworkIosSimulatorArm64` | ✅ `LeafApp.framework` 산출, 헤더에 진입점 노출 |
| `xcodebuild` (Debug / iphonesimulator) | ✅ `Leaf.app` 빌드 |
| 시뮬레이터 실행 (iPhone 17 Pro) | ✅ 홈 화면 렌더 — 폰트·문자열·드로어블·팔레트·Room DB·intro→home 내비게이션 |
| `iosSimulatorArm64Test` | ✅ 10건 통과 |

**수동 확인이 남은 것**: `simctl` 에는 탭 입력 API 가 없고 Simulator GUI 자동화는 접근성 권한이 필요해서,
화면 전환이 필요한 흐름은 자동으로 검증하지 못했다 — 책 검색(Ktor 네트워킹), 라이선스 목록 렌더,
이미지 공유 시트, 텍스트 입력. 라이선스 JSON 이 앱 번들에 들어간 것까지는 확인했다.

---

## 3-2. 3단계 사전 조사 결과 (`core:data-local:impl`)

- TypeConverter 3종(`InstantConverter`, `NoteContentConverter`, `LongListConverter`)은
  `androidx.room.TypeConverter` + `kotlin.time.Instant` + kotlinx-serialization 뿐이라 **무수정 이동 가능**.
- **Room**: `@ConstructedBy` + `expect object ... : RoomDatabaseConstructor<LeafDatabase>` 추가,
  `Room.databaseBuilder` 는 `expect fun` 으로 분리(Android=Context, iOS=NSDocumentDirectory 경로),
  `setDriver(BundledSQLiteDriver())` 필요. KSP 를 타깃별 configuration(`kspAndroid`, `kspIosArm64`, …)에 각각 걸어야 한다.
- **DataStore**: `preferencesDataStore(name)` Context 확장은 Android 전용.
  `PreferenceDataStoreFactory.createWithPath { path }` + `expect fun preferencesPath(): okio.Path` 로 전환.
- **Coil**: `Context` → `coil3.PlatformContext` 로 바꾸면 공통화된다 (Android 에서는 `Context` 의 typealias).
- **aboutlibraries**: Gradle 플러그인이 JSON 을 **app 모듈의 Android `res/raw/aboutlibraries.json`** 으로
  생성한다(`app/build/generated/aboutLibraries/…`). iOS 에는 이 경로가 없다.
  → `expect suspend fun readAboutLibrariesJson(): String` 으로 분리하고,
  androidMain 은 현재 동작을 그대로 유지, iosMain 은 5단계에서 compose-resources
  (`composeResources/files/aboutlibraries.json`)가 붙을 때까지 빈 목록을 반환하도록 둔다.

---

## 4. 알려진 리스크

- **Room KMP**: `androidx.sqlite:sqlite-bundled` 는 room 과 버전 라인이 다름(현재 최신 `2.7.0`). KSP2 + KMP 소스셋 설정 필요.
- **Metro DI**: `metrox-android` 는 Android 전용 → androidMain 으로 격리. 그래프 정의는 commonMain 유지.
- **material3 버전 라인 분리**: `compose.material3` DSL 이 플러그인 기본값(`1.9.0`)을 쓰므로 DSL 대신 좌표를 직접 명시한다.
- **`@Preview`**: CMP 의 preview 는 Android Studio 에서만 렌더 가능. `PreviewParameterProvider` 도 좌표 이동.
- **Firebase Crashlytics**: iOS 대응 시 별도 SDK 필요. 1차로는 `expect object CrashReporter` 의 iosMain 을 no-op 으로 두고 후속 과제로 분리.
- **`compileSdk 37` / AGP 9.2.1**: KMP + AGP 9 조합에서 `androidLibrary` DSL 변화 가능성 — 0단계에서 실측 확인.
