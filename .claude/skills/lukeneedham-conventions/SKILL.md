---
name: lukeneedham-conventions
description: Luke Needham's conventions for Android apps - project setup, debug keystore, CI workflows, architecture (single activity, Compose, Navigation 3, MVVM, Koin DI) and the shared `common` debug library. Use when starting an app, adding a feature, screen, dependency or CI change, or touching logging, crashes or debug pages.
---

<!-- Copied from LukeNeedham/ci-workflows (skills/lukeneedham-conventions). Do not edit here: change it
     in that repo. -->

# Luke's app conventions

The full, current guides are in the public repo `LukeNeedham/ci-workflows` under `docs/`:
`new-app.md` (read first when starting an app), `setup.md` (CI workflows), `app-icon.md`, `library.md` and
`using-the-library.md` (shared library), `consuming-apps.md` (which apps use it). If you can reach
GitHub, read the relevant page there; otherwise the rules below are enough.

## Keep this skill current
Before starting any work in an app, compare this folder with `skills/lukeneedham-conventions` on `main` of
`LukeNeedham/ci-workflows` (public). If it differs, copy the shared folder over this one and include that
in the PR for the work you were asked to do, saying so in the description. Never open a PR just for this.

## Project
- Kotlin only, Gradle Kotlin DSL, version catalog (`gradle/libs.versions.toml`), Gradle wrapper.
- Package `com.lukeneedham.<app>` for both `namespace` and `applicationId`. App module is `:app`.
- AGP 8.13.2, Kotlin 2.2.21, Gradle 8.14.3 (bump together); `compileSdk`/`targetSdk` 36; `minSdk` 23+; JDK 17.
- **Commit a debug keystore** at `app/debug.keystore` (alias `androiddebugkey`, password `android`) and
  use it for the `debug` signing config, so CI builds can update each other.
- No Android SDK in the sandbox: you usually cannot compile locally. CI is the first build.

## CI
- Callers `trigger_on_pull_request.yml` and `delete_prereleases.yml` come from
  `LukeNeedham/ci-workflows/examples/`; the logic is in the shared reusable workflows there.
- `apk-path` is `app/build/outputs/apk/debug/app-debug.apk`; `comment-header` is `<app>-app-link`.
- Debug mode always comes from `BuildConfig.DEBUG` (the build config field).
- Open a PR for completed work. When the APK build comment appears on the PR, paste the download link into the chat.

## App icon
- Commit the icon as `app-icon.png` at the repo root: square PNG, at least 512x512. The app hub reads it
  from `main`, and the PR build (`check-icon` job) fails without it. Update it whenever the launcher icon changes.

## Architecture
- Single activity (`ComponentActivity`), Jetpack Compose + Material 3, no XML, no fragments.
- Navigation 3 (`NavKey` destinations that are `@Serializable`, back stack owned by the app).
- MVVM with unidirectional data flow: a `ViewModel` exposes one immutable `UiState` `StateFlow`; composables
  use `collectAsStateWithLifecycle()` and send events up as function calls; no logic in composables.
- Coroutines + Flow; `viewModelScope` for screen work. UI -> domain/data layers; no Android types in the lower layer.
- **Koin for dependency injection**: `module { }` with `singleOf` / `viewModelOf` / `factoryOf`, `startKoin`
  in `Application.onCreate`, `koinViewModel()` in composables. Constructor injection; classes never call Koin.
- Unit tests for ViewModels and repositories (JUnit, `kotlinx-coroutines-test`).

## Shared library (`com.lukeneedham:common`)
- Repository `https://lukeneedham.github.io/ci-workflows/maven` (group `com.lukeneedham` only); version in `libs.versions.toml`.
- Call `DebugKit.init(DebugKitConfig(enabled = BuildConfig.DEBUG))` first thing in `Application.onCreate`.
- Wrap the UI in `ProvideDebugSettings`, place a `DebugButton`, add `DebugPage`, `LogsPage`, `CrashesPage` to navigation.
- Use `Logger.d/i/w/e`, never `Log` or `println`. Scale animation durations with `LocalAnimationSpeed.current.scale(ms)`.
- Apps with their own debug tools keep their own debug page, with a button that opens the library's `DebugPage`.

## Updating
"Update all consuming apps" means every app with a library tick in `docs/consuming-apps.md` of
`ci-workflows`: bump `common` in each, one PR per app. Whenever a project starts using workflows, the
library or this skill, add it to that list.
