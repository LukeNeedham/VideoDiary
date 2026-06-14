# CLAUDE.md

This file provides guidance for AI assistants (and developers) working with this repository.

## Project overview

**Video Diary** is an open-source Android app (Kotlin + Jetpack Compose) that lets users record
one short video per day, browse past entries on a calendar, and export their diary as a single
recap video. It's privacy-focused (no internet permission, no telemetry) and distributed via
F-Droid.

- Package: `com.lukeneedham.videodiary`
- More context: [README.md](README.md), [docs/](docs)

## Tech stack

- **Language**: Kotlin
- **UI**: Jetpack Compose (Material)
- **DI**: Koin (`koin-android`, `koin-compose`, `koin-compose-viewmodel`)
- **Navigation**: [Reimagined Navigation](https://github.com/olshevski/compose-navigation-reimagined) (`dev.olshevski.navigation.reimagined`)
- **Media**: CameraX (recording) + Media3 (ExoPlayer for playback, Transformer/Effect for export)
- **Persistence**: AndroidX DataStore (preferences) for settings; plain files (via `Context.filesDir`) for videos
- **Build**: Gradle Kotlin DSL, version catalog-style deps centralized in `buildSrc` (see below)
- **Min/Target/Compile SDK**: minSdk 21, targetSdk/compileSdk 35
- **Java/Kotlin target**: Java 11, Kotlin 2.0.0, AGP 8.7.1

## Repository layout

```
app/                         Android app module
  src/main/java/com/lukeneedham/videodiary/
    data/                     Data layer: DAOs, repositories, Android-specific data sources, mappers
    domain/                   Domain models and platform-agnostic utilities (logger, date utils, timer)
    di/                       Koin module definitions (KoinModule.kt, KoinQualifier.kt)
    ui/
      feature/                One subpackage per feature/screen (see "Feature structure" below)
      navigation/             Reimagined Navigation routers + page (route) sealed classes
      root/                   App root composable + RootViewModel (top-level state machine)
      theme/                  Compose theme (Color, Typography, Theme)
      media/                  Shared media helpers (e.g. VideoPlayerPool)
      permissions/            Permission models/holders shared across the app
      share/                  Sharer (creates share intents)
      fileprovider/           FileProvider-related helpers
      util/                   UI-layer utilities/extensions
    util/ext/                 General Kotlin extensions
    App.kt                    Application class - starts Koin, sets up Logger
    MainActivity.kt           Single Activity - hosts Root composable, handles orientation/share/permissions
  src/debug/res/              Debug-only resources (e.g. preview_video drawable for screenshots)
  src/test/                   JVM unit tests
  src/androidTest/            Instrumented tests
buildSrc/                     Centralized dependency version catalog (deps.kt)
docs/                         Technical docs (Release process, Screenshots)
metadata/                     F-Droid metadata (changelogs, screenshots, store listing)
.github/workflows/            CI: build APK on PRs, attach as a GitHub Release asset
```

## Architecture & conventions

### Layers

- **`domain`**: Pure Kotlin models (`Day`, `Video`, `Orientation`, `ExportedVideo`, `ShareRequest`,
  `CameraResolutionRotation`) and platform-agnostic utilities. No Android dependencies where possible.
- **`data`**: DAOs (`VideosDao`, `SettingsDao`, `VideoExportDao`), repositories
  (`CalendarRepository`, `VideoResolutionRepository`, `MockDataRepository`), mappers
  (`VideoFileNameMapper`), and Android-specific helpers (`PermissionChecker`).
  - Videos are stored as files in `context.filesDir/videos`, named via `VideoFileNameMapper`.
  - Settings are stored via `SettingsDao` using DataStore preferences.
- **`ui`**: Compose UI organized by feature, using a ViewModel + State pattern (see below).

### Feature structure (`ui/feature/<name>/`)

Each screen/feature typically has:
- `<Name>Page.kt` — thin composable that wires a ViewModel's state/callbacks into `<Name>PageContent`
- `<Name>PageContent.kt` — "dumb" composable taking explicit parameters (state + lambdas), easy to preview
- `<Name>ViewModel.kt` — `androidx.lifecycle.ViewModel` exposing Compose state via `mutableStateOf`/`derivedStateOf`
- `component/` — subcomponents specific to that feature (sometimes split further into `portrait/`/`landscape/`)

Look at `ui/feature/calendar/` as the canonical example (`CalendarPage`, `CalendarPageContent`,
`CalendarViewModel`, plus `component/day`, `component/portrait`, `component/landscape`).

### ViewModels

- State is exposed as Compose `mutableStateOf`/`mutableIntStateOf`/`derivedStateOf` properties with
  `private set`, not as `StateFlow`/`LiveData`.
- Async work happens in `viewModelScope.launch { ... }`, often collecting Flows from DAOs/repositories.
- ViewModels that need runtime parameters (e.g. `CheckVideoViewModel`) are registered with Koin's
  parameterized `viewModel { (param1, param2) -> ... }` and instantiated via
  `koinViewModel { parametersOf(...) }`.

### Dependency Injection (Koin)

All DI wiring lives in `di/KoinModule.kt`, grouped into module functions:
`getUtil()`, `getData()`, `getRepositories()`, `getViewModel()`, `getNonAndroidViewModel()`, `getPersistence()`.

- Use `factory { }` for most things; use `single { }` for stateful singletons — notably DataStore-backed
  DAOs (`SettingsDao`) **must** be singletons, since only one DataStore instance can exist per file.
- Dispatchers are provided via qualifiers in `KoinQualifier.kt` (`Dispatcher.io`, `Dispatcher.default`, `Dispatcher.main`).
- When adding a new ViewModel/repository/DAO, register it in the relevant `KoinModule` function
  following the existing pattern (constructor injection via `get()`).

### Navigation

- Two navigation graphs, each with a sealed `Page` class and a `Router` composable:
  - `ui/navigation/setup/` (`SetupPage`, `SetupRouter`) — first-run setup flow (intro → orientation → resolution → video duration)
  - `ui/navigation/normal/` (`NormalPage`, `NormalRouter`) — main app flow (calendar, record video, check video, export diary, debug)
- `ui/root/Root.kt` + `RootViewModel`/`RootState` decide which graph (or permissions screen / loading
  spinner) to show, based on permission state and whether setup has completed.
- Built on Reimagined Navigation (`rememberNavController`, `NavHost`, `NavBackHandler`, `navigate`/`pop`/`popUpTo`).
- Log navigation events via `Logger.debug("Navigating to: $to")` (existing convention in both routers).

### Logging

- **Always use `Logger` (`domain/util/logger/Logger.kt`)** — never call Android's `Log` directly
  (it would break unit tests, since `Logger.setLoggerEngine` is set up only in `App.onCreate`).
- `Logger.debug(...)` — for logs useful in testing/diagnostics.
- `Logger.warning(...)` — expected-but-notable error states.
- `Logger.error(...)` — unexpected errors / likely bugs.

### Debug-only features

- `BuildConfig.DEBUG` gates debug-only UI (e.g. extra buttons on the Calendar toolbar that open the
  Debug page, `AndroidLoggerEngine` verbose logging).
- The Debug page (`ui/feature/debug/`) lets developers fill the diary with mock data via
  `MockDataRepository` / `VideosDao.fillWithMockVideos(...)` — only reachable from debug builds.
- `app/src/debug/res/drawable/preview_video` is a large placeholder image used by Composable
  Previews for video players; it's excluded from release builds. A small fallback `preview_video`
  exists in the main `drawable` folder so release builds still compile. See [docs/Screenshots.md](docs/Screenshots.md).

## Build, run & test

```bash
# Build debug APK
./gradlew assembleDebug

# Run JVM unit tests
./gradlew test

# Run instrumented tests (requires a connected device/emulator)
./gradlew connectedAndroidTest

# Full check (build + tests + lint)
./gradlew check
```

- Debug builds use a checked-in `debug.keystore` (in repo root, referenced from `app/build.gradle.kts`)
  for consistent signing, and append `.dbg` to the application ID.
- Dependency versions are centralized in `buildSrc/src/main/kotlin/deps.kt` (object `deps`) — add new
  dependencies there rather than hardcoding versions in `app/build.gradle.kts`.

## CI/CD

- `.github/workflows/trigger_on_pull_request.yml` runs on PRs targeting `main`:
  1. Builds `assembleDebug`.
  2. Creates a draft GitHub Release tagged with the branch/run info and uploads `app-debug.apk` as an asset.
  3. Posts/updates a sticky PR comment with a direct download link to the APK.

## Release process

See [docs/Release.md](docs/Release.md) for the full F-Droid release checklist (changelog entry under
`metadata/en-US/changelogs/`, version bump in `app/build.gradle.kts`, tagging, signed APK build, GitHub Release).

## General conventions for changes

- Follow the existing Page / PageContent / ViewModel split for new screens; keep `PageContent`
  composables free of ViewModel references so they remain previewable.
- Add new domain models to `domain/model/`, keeping them free of Android framework types where possible.
- Wire any new injectable class into `di/KoinModule.kt` in the appropriate module function.
- Prefer `Logger` over print/`Log` statements.
- Keep landscape/portrait-specific UI in `component/landscape/` and `component/portrait/`
  subpackages, mirroring the calendar feature's structure.
