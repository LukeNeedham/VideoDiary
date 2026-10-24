---
name: lukeneedham-conventions
description: Luke Needham's conventions for Android apps - project setup, debug keystore, CI workflows, architecture (single activity, Compose, Navigation 3, MVVM, Koin DI, Coil 3 images, own AppTheme) and the shared `common` debug library. Use when starting an app, adding a feature, screen, dependency or CI change, or touching logging, crashes or debug pages.
---

<!-- Synced from LukeNeedham/ci-workflows (skills/lukeneedham-conventions) by CI. Do not edit here: change it
     in that repo. -->

# Luke's app conventions

The full, current guides are in the public repo `LukeNeedham/ci-workflows` under `docs/`:
`new-app.md` (read first when starting an app), `setup.md` (CI workflows), `app-icon.md`, `app-conventions.md` (libraries and code patterns), `library.md` and
`using-the-library.md` (shared library), `consuming-apps.md` (which apps use it). If you can reach
GitHub, read the relevant page there; otherwise the rules below are enough.

## Keep this skill current
CI keeps this folder, and the four callers in `.github/workflows/`, in line with `LukeNeedham/ci-workflows`: it
opens a PR called "Sync from ci-workflows" in the app when they differ. Do not copy them by hand and do not
edit them in an app; change them in `ci-workflows`. The callers hold no settings: every app
is built the same way (`docs/app-config.md` in `ci-workflows`), so there is nothing to configure per app.

## Project
- Kotlin only, Gradle Kotlin DSL, version catalog (`gradle/libs.versions.toml`), Gradle wrapper.
- Package `com.lukeneedham.<app>` for both `namespace` and `applicationId`. App module is `:app`.
- AGP 8.13.2, Kotlin 2.2.21, Gradle 8.14.3 (bump together); `compileSdk`/`targetSdk` 36; `minSdk` 23+; JDK 17.
- **Commit a debug keystore** at `app/debug.keystore` (alias `androiddebugkey`, password `android`) and
  use it for the `debug` signing config, so CI builds can update each other.
- No Android SDK in the sandbox: you usually cannot compile locally. CI is the first build.

## CI
- Callers `trigger_on_pull_request.yml`, `build_main.yml` (AppHub builds main through it, so keep the name), `delete_prereleases.yml` and `release.yml` come from
  `LukeNeedham/ci-workflows/callers/`; the logic is in the shared reusable workflows there.
- Every app follows the same standard, with no per-app options: module `:app`, debug APK `app/build/outputs/apk/debug/app-debug.apk`, PRs into `main` only (never `master` or `develop`), JDK 17, the shared library, and all four callers (`release.yml` fails with a clear message until the app has the release secrets and `gradle/android-release.gradle`). A new app needs only its entry (`name`, `repo`) in `ci-workflows`' `apps.json`.
- A PR that changes only `.claude/skills/` does not build (no APK, no AppHub link comment from CI).
- Debug mode always comes from `BuildConfig.DEBUG` (the build config field).
- Open a PR for completed work. Paste the PR's **AppHub link** (`https://www.lukeneedham.com/apphub/pr/<repo name>/<number>`, also in CI's AppHub comment) into the chat as soon as the PR is open, without waiting for the build, and not the APK download link.

## App icon
- `app-icon.png` at the repo root is the app's icon for the app hub: a 512x512 circle on a transparent
  background (the adaptive launcher icon's background with its foreground centred, as a circular launcher
  mask shows it). **CI generates it**: the `app-icon` job of the shared PR workflow renders it from the
  adaptive icon on every PR and commits it to the PR branch when it differs. Never draw or edit it by hand.
- So: the app needs an adaptive launcher icon (`android:icon` -> `mipmap-anydpi*` adaptive icon), and a
  caller that grants `contents: write` (the example callers do). Changing the launcher icon needs no extra
  step. After a push, `git pull --rebase` to pick up the bot's `app-icon.png` commit before pushing again.
- To look at it before pushing (optional): `pip install cairosvg pillow`, fetch
  `scripts/render_app_icon.py` from `main` of `LukeNeedham/ci-workflows` and run it from the repo root.

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
- Repository `https://lukeneedham.github.io/ci-workflows/maven` (group `com.lukeneedham` only); version in `libs.versions.toml`.
- **Every app uses the library** (Compose or not): add it, and call `DebugKit.init(DebugKitConfig(enabled = BuildConfig.DEBUG))` first thing in `Application.onCreate`.
- The debug pages are optional. An app that shows them: wrap the UI in `ProvideDebugSettings(theme = AppTheme.current.toDebugTheme())`, inside `AppTheme`: the library uses no Material and the app passes it a `DebugTheme` (colours + font family, mapped from `AppTheme` in one function). Add `DebugPage`, `LogsPage`, `CrashesPage` to navigation, and a `DebugButton` on a settings page (never a floating button on the main page; the library's `DebugPage` shows the app's package and current version).
- Use `Logger.d/i/w/e`, never `Log` or `println`, and log caught failures instead of swallowing them. Scale animation durations with `LocalAnimationSpeed.current.scale(ms)`.
- Apps with their own debug tools keep their own debug page, with a button that opens the library's `DebugPage`.

## Updating
"Update all consuming apps" means every app in `apps.json` of
`ci-workflows`: bump `common` in each, one PR per app. Whenever a project starts using workflows, the
library or this skill, add it to that file.
