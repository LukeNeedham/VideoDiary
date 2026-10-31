---
name: lukeneedham-conventions
description: Luke Needham's conventions for Android apps - project setup, debug keystore, CI workflows, architecture (single activity, Compose, Navigation 3, MVVM, Koin DI, Coil 3 images, own AppTheme) and the shared `common` library (logger, crash handler, debug pages). Use when starting an app, adding a feature, screen, dependency or CI change, or touching logging, crashes or debug pages.
---

# Luke's app conventions

This folder is copied from `LukeNeedham/ci-workflows` (`skills/lukeneedham-conventions`) by CI, which opens a
"Sync from ci-workflows" PR when it differs, and that PR merges itself once its checks pass. Never edit it here; change it
there. The same goes for the callers in `.github/workflows/`.

The long guides are in that repo's `docs/`: `new-app.md` (read first when starting an app), `setup.md`,
`app-config.md`, `app-icon.md`, `readme.md` (the README layout and `readme.yml`), `pages.md` (`XPage` and
`XPageContent`), `app-shell.md` (`MainActivity`, `Root`, navigation), `networking.md` (Ktor/OkHttp, DTOs, the network log), `screenshots.md` (the README screenshots), `app-conventions.md`, `library.md`,
`using-the-library.md`, `consuming-apps.md` and `create-app.md` (how a new app repo is created). If you can reach GitHub, read the relevant page; otherwise the rules
below are enough.

## Project
- Kotlin only, Gradle Kotlin DSL, version catalog (`gradle/libs.versions.toml`), Gradle wrapper.
- Package `com.lukeneedham.<app>` for both `namespace` and `applicationId`. App module is `:app`.
- AGP 8.13.2, Kotlin 2.2.21, Gradle 8.14.3 (bump together); `compileSdk`/`targetSdk` 36; `minSdk` 23+; JDK 17.
- **Commit a debug keystore** at `app/debug.keystore` (alias `androiddebugkey`, password `android`) and
  use it for the `debug` signing config, so CI builds can update each other.
- **Claude can never build or run the app.** The sandbox has no access to Gradle distributions, Maven or the Android
  SDK, so compiling, building, running and checking that it builds happen **only on CI**. Skip it at once: never try
  to download Gradle, set up the SDK or run `./gradlew` (it only wastes tokens). Write carefully, push, then read the
  PR build result and comments (CI's build comment, the AppHub link) and fix what the build log says. Pure
  Python/Kotlin-JVM logic with no Android dependency is the only thing worth running locally.

## CI
- Every app is built the same way and nothing is configured per app: module `:app`, debug APK at
  `app/build/outputs/apk/debug/app-debug.apk`, PRs into `main` only (never `master` or `develop`), JDK 17.
  A new app needs only its entry (`name`, `repo`) in `ci-workflows`' `apps.json`.
  Claude cannot create the repo (403): the user makes it, then sets it up with the *New App* workflow of
  `ci-workflows` (`docs/create-app.md`), which adds the entry, the first sync and a Kotlin
  Multiplatform project (Android target only: shared code in `commonMain`, `App`, `MainActivity` and the manifest in `androidMain`, the
  screenshots in `androidDebug`, strings as Compose resources) that builds: the debug keystore, the dependencies, the app shell,
  `AppTheme` and a `SettingsPage` with the `DebugButton`. Look at what is there before adding anything, and keep Android-only
  code out of `commonMain`.
- Every app has all the callers of `ci-workflows`' `callers/`, copied by the sync: never edit, rename or delete one
  (`docs/setup.md` there documents each). The release run fails at once, with a message that lists what is missing, until the
  app has the four release secrets and `gradle/android-release.gradle`.
- A PR that changes no Android source file (only `docs/`, `.claude/`, or Markdown outside a `src/` folder) is not built, so it has no APK and no AppHub comment from CI.
- CI's own commits (generated screenshots, icon and README) are pushed with the owner's `GENERATED_COMMIT_TOKEN` when the app has it, and are not built again.
- Debug mode always comes from `BuildConfig.DEBUG`.
- Open a PR for completed work. Paste the PR's **AppHub link** (`https://www.lukeneedham.com/apphub/pr/<repo name>/<number>`,
  also in CI's AppHub comment) into the chat as soon as the PR is open, without waiting for the build, and not the
  APK download link. Always write it as its own line, never inside a sentence, as a markdown link that starts with
  the 📦 emoji: `📦 [Open in AppHub](https://www.lukeneedham.com/apphub/pr/<repo name>/<number>)`. Put the same line
  at the very top of the PR description, so the PR page is one tap from the AppHub link too.

## App icon
- `app-icon.png` at the repo root is the icon for the app hub: a 512x512 circle on a transparent background.
  **CI generates it** from the adaptive launcher icon on every PR and commits it to the PR branch when it
  differs. Never draw or edit it by hand.
- So the app needs an adaptive launcher icon (`android:icon` -> `mipmap-anydpi*`). After a push,
  `git pull --rebase` to pick up the bot's `app-icon.png` commit before pushing again.
- To look at it first (optional): `pip install cairosvg pillow`, fetch `scripts/render_app_icon.py` from `main`
  of `LukeNeedham/ci-workflows` and run it from the repo root.

## README
- `README.md` is **generated by CI from `readme.yml`** (repo root) in the standard layout of `docs/readme.md` in
  `LukeNeedham/ci-workflows`, and committed to the PR branch. **Never edit `README.md`; edit `readme.yml`.** After a
  push, `git pull --rebase` to pick up the bot's commits (README, screenshots, app icon).
- `readme.yml` is handwritten and holds **only what is specific to the app**: name, tagline, description (with the app's
  highlights; no feature list, no limits), and optionally `links` (`play-store`, `f-droid`, `web`, `other`), `user.install`,
  `user.steps`, `developer.notes` (only app-specific setup) and `sections`. No `#` or `##` headings in its text. It does
  **not** hold the minimum Android version (read from `minSdk`), the license, the generic setup or the architecture.
  Skeleton: `docs/readme-template.yml`. If it is missing, write it once from the template; update it in the same PR as
  any change a reader would notice.
- **Every app has a `LICENSE` file**, which the README names and links. An app with no license gets one holding
  `Copyright (c) <year> Luke Needham. All rights reserved.`

## Pages and screenshots
- **The content of a bottom sheet is always its own composable, in its own file, with its own preview** (`docs/pages.md`):
  `XSheetContent` in `XSheetContent.kt`, stateless (state down, events up) and unaware of the sheet around it, with at least
  one `private` `@Preview` wrapped in `AppTheme`. `XSheet` is only the container (scrim, surface, drag handle, animation,
  insets) and only puts `XSheetContent` in it.
- **Every page is `XPage` + `XPageContent`, always** (`docs/pages.md`, which has the migration steps). `XPage` is public:
  it gets its ViewModel, always named **`XViewModel`** (`koinViewModel<XViewModel>()`), collects the state, handles
  lifecycle and one-off events, turns navigation into lambdas, and only calls `XPageContent`; no preview.
  `XPageContent` is `internal` and stateless (an `XUiState` and one lambda per action; no ViewModel, Koin, `Flow` or
  navigator) and **always has at least one `private` `@Preview` in the same file** (named `Preview`; see Previews below). **Each of `XPage`, `XPageContent`, `XViewModel` and `XUiState` is in its own file**
  (`XPage.kt`, `XPageContent.kt` with the previews, `XViewModel.kt`, `XUiState.kt`). Never `XScreen`, `XContent` or `XView`. **The route is always
  `Route.X`**, a member of the one `sealed interface Route : NavKey` in `Route.kt` (`Route.Home` is `HomePage`:
  `Route.Home -> HomePage(...)` in `Router`; never `HomeNavKey`, `HomeKey` or `HomeRoute`).
- **README screenshots live in one package, `<app package>.screenshot`, in `src/debug`** (`src/androidDebug` in a Kotlin Multiplatform app): a file per page that has
  some, named after the page (`HomePage.kt`; more previews = more screenshots, no file = none). Each screenshot is a
  **private `@Preview` composable that owns its mock data** inside its body and renders `HomePageContent` with it
  (`HomePageScreenshot`). The package's **`order.txt`** lists the screenshots (`HomePage`) in README order, one per line, **with no comments** (no `#` lines: CI fails on them);
  CI numbers the images by position (`01-HomePage.png`). **Nothing is shared** between screenshots, nor with the general previews or the app: no
  top-level `val`, `object`, helper or non-private declaration (CI fails on it). The package's `README.md` is generated by CI: never
  write it.
- **CI renders every `@Preview` of that package** into `docs/screenshots/<name without Screenshot>.png` and commits it
  to the PR branch: never take or edit them by hand. The app needs only `ui-tooling-preview` and the package: no
  Roborazzi, no test class (`docs/screenshots.md`). Every app has at least one screenshot and **CI fails without one**
  (no package, an empty one, no `order.txt`, no `@Preview`); only an app that cannot render previews (a WebView
  wrapper) opts out with `screenshots: manual` in `readme.yml`, has no `screenshot` folder and commits one by hand.

## Architecture
- **The app shell is defined in one doc, `docs/app-shell.md`, for new apps and for migrating existing ones.** Single
  activity, always **`MainActivity`** (never another name, a second activity or fragments), which only calls
  `setContent { Root(...) }` and may hand `Root` what only the activity can provide (a deep link, a permission callback); `Root` (`Root.kt`) sets up `AppTheme`, `ProvideDebugSettings` and the other app-wide providers,
  then calls `AppNavigation` (owns the `backStack` and the `NavDisplay`; only `Root` calls it); `Router` is the
  exhaustive `when (route)` with one `XPage(...)` per `Route` member, **no `else`**, and the common actions as local
  functions (`navigateTo(route)`, `onBack()`; branches pass `::onBack`, never `backStack.add(...)`); `routeEntryProvider`
  does `val route = key as Route` and returns `NavEntry(route) { Router(backStack, route) }`; never the
  `entryProvider { entry<...> { } }` DSL (a missing route fails at runtime). Fixed names, one file each: `App` (the
  `Application`), `MainActivity`, `Root`, `AppNavigation`, `Router`, `Route`, `AppTheme` (the composable `AppTheme { }`, read
  through the `object AppTheme`); never a variant (`AppRoot`, `MainScreen`, `AppRouter`, `NavHost`, `RoutePage`,
  `<AppName>Theme`, `ProvideAppTheme`). Exceptions (`docs/app-shell.md`): an overlay activity (a window over other
  apps, named after its job) may be a second activity but a screen never is; Kotlin Multiplatform apps use the JetBrains
  Navigation 3 artifacts (`rememberNavBackStack(config, ...)` with every `Route` member registered in the config, since iOS and the web have no reflection). A route needing its own `NavEntry` options (`metadata`) builds its own.
- Jetpack Compose, no XML. Navigation 3 (the `@Serializable` members of one `sealed interface Route : NavKey`, back stack owned by the app).
- MVVM with unidirectional data flow: a `ViewModel` exposes one immutable `UiState` `StateFlow`; composables
  use `collectAsStateWithLifecycle()` and send events up as function calls; no logic in composables.
- Coroutines + Flow; `viewModelScope` for screen work. UI -> domain/data layers; no Android types in the lower layer.
- **Never `AndroidViewModel`** (banned in every app): ViewModels extend `ViewModel` and take their dependencies (never an `Application` or `Context`) as constructor parameters, provided by Koin.
- **Koin for dependency injection, in every app without exception**: `module { }` with `singleOf` / `viewModelOf` / `factoryOf`, `startKoin`
  in `Application.onCreate`, `koinViewModel()` in composables. Constructor injection; classes never call Koin.
  **All Koin modules live in one Kotlin `object` named `KoinModules`** (`<app package>.di`, `val modules: List<Module>`;
  `App` calls `modules(KoinModules.modules)`); it may delegate to other objects specific to a feature or use case.
  `KoinModules.kt` contains only that object. Qualifier names are `const val`s of the one `object KoinQualifier` (`KoinQualifier.kt`, in the same `di` package,
  `named(KoinQualifier.X)`, never a string literal, nested objects allowed for groups); every other object is in a file of its own.
  **Koin modules only wire**: one-line bindings (`singleOf`, `viewModelOf`, `single<Api> { Impl(get()) }`) whose only
  arguments are `get()`, `androidContext()`, runtime parameters and named constants. Any configuration (builders and DSLs
  like `HttpClient(OkHttp) { ... }` or `Room.databaseBuilder(...)`, timeouts, cache sizes, file names, keys, an inline
  `File` or `CoroutineScope(...)`, a value read from a global, chained factory calls) lives in a dedicated class or Kotlin
  `object` that the module just calls (`single { HttpClientFactory.create() }`); a `File` or scope other bindings need is
  bound under a qualifier constant. Name a feature object's module `bindings`, not `module`.
- **Naming: an acronym is an ordinary word in camel case** (`docs/app-conventions.md#naming`): `IosSdkManager`, not `iOSSDKManager`;
  `HttpClient`, `parseJson`, `UserDto`, `UiState`, `userId`, never `HTTPClient`, `parseJSON`, `UserDTO`, `UIState`, `userID`. Only a name a library
  or platform API fixes (an override, `Dispatchers.IO`) keeps its spelling.
- **CI checks these rules.** The `conventions` job of every PR fails on a Koin module that configures, a `KoinModules.kt` or `KoinQualifier.kt` with more than its
  object, an `AndroidViewModel`, a string qualifier, or an acronym in capitals in a name (`docs/conventions-check.md`). Run it with
  `python3 scripts/check_conventions.py <app>` from `LukeNeedham/ci-workflows`.
- **Coil 3 for image loading** (`io.coil-kt.coil3:coil-compose`, not Coil 2.x, Glide or Picasso): `AsyncImage`
  for files, URIs and URLs. Add `coil-network-okhttp` only if the app loads images over the network.
- Unit tests for ViewModels and repositories (JUnit, `kotlinx-coroutines-test`).

## Libraries and code patterns (details: `docs/app-conventions.md`)
- **Networking (only apps that make network calls; `docs/networking.md`)**: Ktor client with the OkHttp engine. Every model from the
  network is a `@Serializable` **`XDto`** (`UserDto`, never `UserDTO`/`UserResponse`) with **every property nullable and defaulting
  to `null`** (enums as `String?`) and a lenient `Json` (`ignoreUnknownKeys`, `isLenient`, `coerceInputValues`, `explicitNulls = false`), so
  no API response can crash the app. A mapper `XDto.toDomain()` (never throwing; defaults, fallbacks, drops unusable items) turns each
  into a domain model; **a DTO never leaves the networking layer** (never in a ViewModel, `UiState` or `Route`). Failed calls are
  caught and logged, not crashed on. **Every client must have `NetworkLogInterceptor()` added to its engine** (the library's network
  log). An app with no networking: none of this applies.
- Libraries: `kotlinx.serialization`, Room (structured data), DataStore (settings, never `SharedPreferences`),
  Ktor client with the OkHttp engine (networking, see above), `kotlinx-datetime`, Media3 (audio/video).
- **All text in `strings.xml`**, none hard-coded (including content descriptions).
- **Own `AppTheme`**, a hand-made composition local holding colours and typefaces. Never read `MaterialTheme`
  in the app; always `AppTheme.current`. No fixed text-style scale: set font size and line height inline in
  the composable, take only the typeface from the theme.
- **One composable per file with its `@Preview`** (fake `UiState`, wrapped in `AppTheme`), and a preview for
  every composable; trivial ones can sit next to their parent and skip both.
- **Previews:** a preview is a `private fun Preview()` in the file of its composable; with several in a file, the main
  one is `Preview` and the others `Preview` plus a differentiator (`PreviewEmpty`, `PreviewLoading`). (The `screenshot`
  package is the exception: `XScreenshot`, unique across the package.)
- **One-off events** go through a `Channel` exposed as a `Flow` (`receiveAsFlow()`), never in `UiState`.
- **Touch targets** at least 48dp by 48dp for anything clickable: `Modifier.minimumInteractiveComponentSize()`.
- **Release builds are minified** (`isMinifyEnabled = true`, `isShrinkResources = true`) with keep rules.

## Shared library (`com.lukeneedham:common`)
- Every app uses it, Compose or not. Repository `https://lukeneedham.github.io/ci-workflows/maven` (group
  `com.lukeneedham` only); version in `libs.versions.toml`. Call
  `DebugKit.init(DebugKitConfig(enabled = BuildConfig.DEBUG))` first thing in `Application.onCreate`.
- Use `Logger.d/i/w/e`, never `Log` or `println`, and log caught failures instead of swallowing them.
- The debug pages are optional. An app that shows them wraps the UI in
  `ProvideDebugSettings(theme = AppTheme.current.toDebugTheme())`, inside `AppTheme` (the library uses no Material:
  the app maps `AppTheme` to a `DebugTheme` in one function), adds `DebugPage`, `LogsPage` and `CrashesPage` to
  navigation (an app with network calls also adds `NetworkPage`, passes `onOpenNetwork` to `DebugPage` and adds
  `NetworkLogInterceptor()` to its OkHttp client or Ktor OkHttp `engine { }`), and puts a `DebugButton` on a settings page (never a floating button). The library's `DebugPage` shows
  the app's package and version; an app with its own debug tools keeps its own debug page with a button to it.
  Scale animation durations with `LocalAnimationSpeed.current.scale(ms)`.
- "Update all consuming apps" means every app in `apps.json` of `ci-workflows`: bump `common` in each, one PR per
  app.
