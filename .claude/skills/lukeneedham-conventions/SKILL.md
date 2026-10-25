---
name: lukeneedham-conventions
description: Luke Needham's conventions for Android apps - project setup, debug keystore, CI workflows, architecture (single activity, Compose, Navigation 3, MVVM, Koin DI, Coil 3 images, own AppTheme) and the shared `common` library (logger, crash handler, debug pages). Use when starting an app, adding a feature, screen, dependency or CI change, or touching logging, crashes or debug pages.
---

# Luke's app conventions

This folder is copied from `LukeNeedham/ci-workflows` (`skills/lukeneedham-conventions`) by CI, which opens a
"Sync from ci-workflows" PR when it differs. Never edit it here; change it there. The same goes for the four
callers in `.github/workflows/`.

The long guides are in that repo's `docs/`: `new-app.md` (read first when starting an app), `setup.md`,
`app-config.md`, `app-icon.md`, `app-conventions.md`, `library.md`, `using-the-library.md` and `consuming-apps.md`.
If you can reach GitHub, read the relevant page; otherwise the rules below are enough.

## Project
- Kotlin only, Gradle Kotlin DSL, version catalog (`gradle/libs.versions.toml`), Gradle wrapper.
- Package `com.lukeneedham.<app>` for both `namespace` and `applicationId`. App module is `:app`.
- AGP 8.13.2, Kotlin 2.2.21, Gradle 8.14.3 (bump together); `compileSdk`/`targetSdk` 36; `minSdk` 23+; JDK 17.
- **Commit a debug keystore** at `app/debug.keystore` (alias `androiddebugkey`, password `android`) and
  use it for the `debug` signing config, so CI builds can update each other.
- No Android SDK in the sandbox: you usually cannot compile locally. CI is the first build.

## CI
- Every app is built the same way and nothing is configured per app: module `:app`, debug APK at
  `app/build/outputs/apk/debug/app-debug.apk`, PRs into `main` only (never `master` or `develop`), JDK 17.
  A new app needs only its entry (`name`, `repo`) in `ci-workflows`' `apps.json`.
- Every app has all four callers: `trigger_on_pull_request.yml`, `build_main.yml` (AppHub builds main through
  it, so keep the name), `delete_prereleases.yml` and `release.yml`. `release.yml` fails at once, with a message
  that lists what is missing, until the app has the four release secrets and `gradle/android-release.gradle`.
- A PR that changes only `.claude/skills/` is not built, so it has no APK and no AppHub comment from CI.
- Debug mode always comes from `BuildConfig.DEBUG`.
- Open a PR for completed work. Paste the PR's **AppHub link** (`https://www.lukeneedham.com/apphub/pr/<repo name>/<number>`,
  also in CI's AppHub comment) into the chat as soon as the PR is open, without waiting for the build, and not the
  APK download link.

## App icon
- `app-icon.png` at the repo root is the icon for the app hub: a 512x512 circle on a transparent background.
  **CI generates it** from the adaptive launcher icon on every PR and commits it to the PR branch when it
  differs. Never draw or edit it by hand.
- So the app needs an adaptive launcher icon (`android:icon` -> `mipmap-anydpi*`). After a push,
  `git pull --rebase` to pick up the bot's `app-icon.png` commit before pushing again.
- To look at it first (optional): `pip install cairosvg pillow`, fetch `scripts/render_app_icon.py` from `main`
  of `LukeNeedham/ci-workflows` and run it from the repo root.

## Architecture
- Single activity (`ComponentActivity`), Jetpack Compose, no XML, no fragments.
- Navigation 3 (`NavKey` destinations that are `@Serializable`, back stack owned by the app).
- MVVM with unidirectional data flow: a `ViewModel` exposes one immutable `UiState` `StateFlow`; composables
  use `collectAsStateWithLifecycle()` and send events up as function calls; no logic in composables.
- Coroutines + Flow; `viewModelScope` for screen work. UI -> domain/data layers; no Android types in the lower layer.
- **Koin for dependency injection**: `module { }` with `singleOf` / `viewModelOf` / `factoryOf`, `startKoin`
  in `Application.onCreate`, `koinViewModel()` in composables. Constructor injection; classes never call Koin.
- **Coil 3 for image loading** (`io.coil-kt.coil3:coil-compose`, not Coil 2.x, Glide or Picasso): `AsyncImage`
  for files, URIs and URLs. Add `coil-network-okhttp` only if the app loads images over the network.
- Unit tests for ViewModels and repositories (JUnit, `kotlinx-coroutines-test`).

## Libraries and code patterns (details: `docs/app-conventions.md`)
- Libraries: `kotlinx.serialization`, Room (structured data), DataStore (settings, never `SharedPreferences`),
  Ktor client with the OkHttp engine (networking), `kotlinx-datetime`, Media3 (audio/video).
- **All text in `strings.xml`**, none hard-coded (including content descriptions).
- **Own `AppTheme`**, a hand-made composition local holding colours and typefaces. Never read `MaterialTheme`
  in the app; always `AppTheme.current`. No fixed text-style scale: set font size and line height inline in
  the composable, take only the typeface from the theme.
- **One composable per file with its `@Preview`** (fake `UiState`, wrapped in `AppTheme`), and a preview for
  every composable; trivial ones can sit next to their parent and skip both.
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
  navigation, and puts a `DebugButton` on a settings page (never a floating button). The library's `DebugPage` shows
  the app's package and version; an app with its own debug tools keeps its own debug page with a button to it.
  Scale animation durations with `LocalAnimationSpeed.current.scale(ms)`.
- "Update all consuming apps" means every app in `apps.json` of `ci-workflows`: bump `common` in each, one PR per
  app.
