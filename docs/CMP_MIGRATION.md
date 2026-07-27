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
| 8 | 진입점 | `androidApp`(기존 app) + `iosApp` Xcode 프로젝트, `ComposeUIViewController` | 🚧 **Xcode 필요** |
| 9 | 정리 | `templates/feature-module` 갱신, README 모듈 구조 갱신 | ⬜ |

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

## 3-1. 환경 제약 — iOS 링킹 불가 (현재 머신)

이 머신에는 **Xcode 정식 설치 없이 Command Line Tools 만** 있다.

```
xcode-select -p        → /Library/Developer/CommandLineTools
xcrun xcodebuild -version → error: unable to find utility "xcodebuild"
```

그래서 iOS 관련 작업이 두 갈래로 갈린다.

| 가능 | 불가능 |
|---|---|
| `compileKotlinIosArm64` 등 **klib 컴파일** — 소스가 iOS 에서 컴파일되는지 전부 검증 가능 | `linkDebugTest*`, `linkDebugFramework*` 등 **네이티브 링킹** |
| `commonTest` 를 JVM host test 로 실행 | `iosSimulatorArm64Test` — 테스트 실행 |
| 8단계의 Kotlin 측 진입점(`ComposeUIViewController`) 작성 | Xcode 프로젝트 빌드 · 시뮬레이터 실행 · 프레임워크 산출 |

**즉 1-7·9 단계는 이 머신에서 완결할 수 있고, 8단계는 Xcode 설치 후에만 마무리된다.**
(App Store 또는 developer.apple.com 에서 Xcode 설치 → `sudo xcode-select -s /Applications/Xcode.app`)

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
