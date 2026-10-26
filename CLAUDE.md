# CLAUDE.md

This file provides guidance for AI assistants (and developers) working with this repository.

# Commands
Here is a list of short-hand commands that you may be asked.
- "Implement the top todo" - read the TODO.md file and inplement the top item, following the rules described in that file.
- "Delete pre-releases" - run the "delete_prereleases" workflow yml

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
- **Navigation**: Navigation 3 (`androidx.navigation3`)
- **Media**: CameraX (recording) + Media3 (ExoPlayer for playback, Transformer/Effect for export)
- **Persistence**: AndroidX DataStore (preferences) for settings; plain files (via `Context.filesDir`) for videos; Room for saved recaps
- **Shared library**: [`com.lukeneedham:common`](https://github.com/LukeNeedham/ci-workflows/tree/main/common) (logger, crash handler, Debug/Logs/Crashes pages), from the Maven repository at `https://lukeneedham.github.io/ci-workflows/maven` (added in `settings.gradle.kts`, version in `buildSrc/.../deps.kt`)
- **Build**: Gradle Kotlin DSL, version catalog-style deps centralized in `buildSrc` (see below)
- **Min/Target/Compile SDK**: minSdk 23, targetSdk 35, compileSdk 36 (minSdk 23 and compileSdk 36 are required by the shared library)
- **Java/Kotlin target**: Java 11, Kotlin 2.2.21, AGP 8.13.2, Gradle 8.14.3, KSP 2.2.21-2.0.4 (Kotlin 2.2 or newer is required by the shared library)

## Repository layout

```
app/                         Android app module
  src/main/java/com/lukeneedham/videodiary/
    data/                     Data layer: DAOs, repositories, Android-specific data sources, mappers
    domain/                   Domain models and platform-agnostic utilities (date utils, timer)
    di/                       Koin module definitions (KoinModule.kt, KoinQualifier.kt)
    ui/
      feature/                One subpackage per feature/screen (see "Feature structure" below)
      navigation/             Navigation 3: `AppNavigation` (back stack + `NavDisplay`), `Router` (exhaustive `when` over `Route`) and the `Route` sealed interface of the main flow
      root/                   RootPage + RootPageContent + RootViewModel (top-level state machine)
      theme/                  Compose theme (Color, Typography, AppTheme)
      media/                  Shared media helpers (e.g. VideoPlayerPool)
      permissions/            Permission models/holders shared across the app
      share/                  Sharer (creates share intents)
      fileprovider/           FileProvider-related helpers
      util/                   UI-layer utilities/extensions
    util/ext/                 General Kotlin extensions
    App.kt                    Application class - initialises the shared library's `DebugKit` (logger + crash handler), starts Koin
    MainActivity.kt           Single Activity - hosts RootPage, handles orientation/share/permissions
  src/debug/res/              Debug-only resources (e.g. preview_video drawable for screenshots)
  src/test/                   JVM unit tests
  src/androidTest/            Instrumented tests
buildSrc/                     Centralized dependency version catalog (deps.kt)
docs/                         Technical docs (Release process, Screenshots)
metadata/                     F-Droid metadata (changelogs, screenshots, store listing)
.github/workflows/            CI: build APK on PRs (shared workflows), attach as a GitHub Release asset
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
- `<Name>Page.kt` — thin composable that gets its own `<Name>ViewModel` (`koinViewModel`) and wires its state/callbacks into `<Name>PageContent`.
  One ViewModel per page: it is never shared, and only the page requests it (CI checks this: `docs/lint.md` in ci-workflows)
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
  parameterized `viewModel { (param1, param2) -> ... }` and instantiated by the page, via
  `koinViewModel { parametersOf(...) }`, with the parameters coming from the page's own arguments (the route's).

### Dependency Injection (Koin)

All DI wiring lives in `di/KoinModule.kt`, grouped into module functions:
`getUtil()`, `getData()`, `getRepositories()`, `getViewModel()`, `getComponentState()`, `getPersistence()`.

- Use `factory { }` for most things; use `single { }` for stateful singletons — notably DataStore-backed
  DAOs (`SettingsDao`) **must** be singletons, since only one DataStore instance can exist per file.
- Dispatchers are provided via qualifiers in `KoinQualifier.kt` (`Dispatcher.io`, `Dispatcher.default`, `Dispatcher.main`).
- When adding a new ViewModel/repository/DAO, register it in the relevant `KoinModule` function
  following the existing pattern (constructor injection via `get()`).

### Navigation

- Two flows: the main app flow on Navigation 3 (a sealed `Route : NavKey`, `AppNavigation` and an exhaustive `Router`), and the first-run setup flow (a pager):
  - `ui/feature/setup/` (`SetupPage`, `SetupPageContent`, `SetupViewModel`) — first-run setup flow, one swipeable pager
    (intro → permissions → orientation → resolution → video duration). The intro, permission and final steps are plain
    `…SlideContent` composables; the orientation, resolution and duration steps are pages with their own ViewModels, which
    hand up what the shared "Next" button saves (`registerSave`), so no page ever touches another page's ViewModel
  - `ui/navigation/normal/` (`Route.kt`, `AppNavigation.kt`, `Router.kt`) — main app flow (calendar, record video, check video, export diary, debug, logs, crashes)
- `ui/root/RootPage` + `RootPageContent` + `RootViewModel`/`RootState` decide which flow (or permissions screen / loading
  spinner) to show, based on permission state and whether setup has completed.
- Built on Navigation 3 (`rememberNavBackStack`, `NavDisplay`, `NavEntry`); `Router` defines `navigateTo`, `onBack` and `popUpTo` once, and each page's ViewModel is scoped to its back stack entry.
- Log navigation events via `Logger.d("Navigating to: $to")` (existing convention in both routers).

### Bottom sheets

- **All bottom sheets must use `SheetLayout` and `SheetDefaults`** (`ui/feature/common/sheet/SheetLayout.kt`).
  Never write a custom drag handle or style a sheet container by hand. When adding a new sheet, or
  touching an existing one, make sure it follows this. See [docs/BottomSheets.md](docs/BottomSheets.md).

### Logging

The logger, crash handler and the Debug / Logs / Crashes pages come from the shared library
`com.lukeneedham:common` (source: `common/` in [LukeNeedham/ci-workflows](https://github.com/LukeNeedham/ci-workflows),
docs: `docs/using-the-library.md` there). The app has no logger of its own.

- **Always use `com.lukeneedham.common.logger.Logger`** — never call Android's `Log` directly. It logs to
  Logcat and to a file, so the lines show on the Debug > Logs page. It works in unit tests without setup.
- `Logger.d(...)` — for logs useful in testing/diagnostics. Dropped in release builds (`minLogLevel` is set in `App.onCreate`).
- `Logger.i(...)` — informational.
- `Logger.w(msg, throwable)` — expected-but-notable error states.
- `Logger.e(msg, throwable)` — unexpected errors / likely bugs.
- `DebugKit.init(...)` is the first call in `App.onCreate`, so it runs before anything can log or crash.
- `RootPage` wraps the app in `ProvideAppDebugSettings` (which wraps the library's `ProvideDebugSettings`) (the library's single provider of debug composition locals). Keep it wrapped, even though no debug setting is read yet, so a setting added to the library later reaches every screen with only a version bump.
  Uncaught exceptions are saved by the library (do not install another handler: crashes would be saved twice).
- The library adds no permissions; the app still has no internet permission. Keep checking the merged
  manifest when upgrading the library.
- Crashes saved by the app's old crash handler (plain `.txt` files in `filesDir/crashlogs`) are not shown
  any more and are not migrated. They are left on disk; delete that folder in a later release.
  (There was never a Room table for crash logs in this app; Room only holds saved recaps.)

### Debug-only features

- `BuildConfig.DEBUG` gates debug-only UI (e.g. the Settings entry that opens the Debug page) and the
  library's `DebugKitConfig(enabled = ...)`, and keeps debug logs (`Logger.d`) out of release builds.
- The Debug page is the app's own (`ui/feature/debug/`: `AppDebugPage` / `AppDebugPageContent` / `AppDebugViewModel`, routed as
  `Route.AppDebug` in `Router`). It holds the app's own tools, which let developers fill the diary with mock data via
  `MockDataRepository` / `VideosDao.fillWithMockVideos(...)` — only reachable from debug builds — and has an "SDK debug" row that
  opens the shared library's `DebugPage` (`Route.Debug`: animation speed, links to the Logs and Crashes pages). The
  library's page has no slot for app-specific tools, so add new ones to the app's page, never to the library's.
  The library's pages draw with Material 3 and the app theme is Material 2, so they look slightly different from the rest of the app.
  The library's animation speed (Debug > SDK debug) applies to the app's animations that have an explicit duration, through
  `LocalAnimationSpeed.current.scale(ms)` (`com.lukeneedham.common.ui.LocalAnimationSpeed`, `com.lukeneedham.common.debug.scale`;
  `ProvideDebugSettings` is in `ProvideAppDebugSettings`, used by `RootPage`): the recap share sheet settle (`RecapViewPageContent`) and the camera focus indicator
  hold and fade (`CameraInput`). Give any new animation an explicit duration and pass it through `scale`. Animations that use the default
  spring (`SingleOptionSelector`, the orientation text colour) are not affected by the setting.
  Mock entries are populated by copying the `sample_video` raw resource in as each video file.
- `app/src/debug/res/drawable/preview_video` is a large placeholder image used by Composable
  Previews for video players; it's excluded from release builds. A small fallback `preview_video`
  exists in the main `drawable` folder so release builds still compile. See [docs/Screenshots.md](docs/Screenshots.md).
- `app/src/debug/res/raw/sample_video` is the real placeholder video copied into mock diary
  entries; it's excluded from release builds. A tiny fallback `sample_video` exists in the main
  `raw` folder (never actually used) so release builds still compile, mirroring the
  `preview_video` pattern above.

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

- `.github/workflows/trigger_on_pull_request.yml` runs on PRs targeting `main` (opened, reopened,
  pushed to, closed) and calls the shared `android_pr.yml` from `LukeNeedham/ci-workflows` (`@main`):
  1. While the PR is open it builds `assembleDebug`, creates a pre-release tagged with the branch/run
     info with `app-debug.apk` as an asset, and posts/updates a sticky PR comment with a direct
     download link to the APK. A newer push cancels the running build.
  2. When the PR is closed, merged or not, it deletes the PR's build pre-releases (and tags).
- `.github/workflows/delete_prereleases.yml` (manual) calls the shared `delete_prereleases.yml`,
  which deletes every pre-release and tag.
- The APK build/cleanup logic lives in the shared repo (`LukeNeedham/ci-workflows`), not here, so make changes
  there. The callers hold no settings: every app is built the same way.

## Release process

See [docs/Release.md](docs/Release.md) for the full F-Droid release checklist (changelog entry under
`metadata/en-US/changelogs/`, version bump in `app/build.gradle.kts`, tagging, signed APK build, GitHub Release).

## General conventions for changes

- Follow the existing Page / PageContent / ViewModel split for new screens; keep `PageContent`
  composables free of ViewModel references so they remain previewable.
- Add new domain models to `domain/model/`, keeping them free of Android framework types where possible.
- Wire any new injectable class into `di/KoinModule.kt` in the appropriate module function.
- Prefer `Logger` over print/`Log` statements.
- Any new bottom sheet must use the common `SheetLayout` (see "Bottom sheets" above).
- Keep landscape/portrait-specific UI in `component/landscape/` and `component/portrait/`
  subpackages, mirroring the calendar feature's structure.

<!-- ci-workflows:begin (managed by LukeNeedham/ci-workflows, do not edit) -->
- `.claude/skills/lukeneedham-conventions` and the callers `trigger_on_pull_request.yml`, `build_main.yml`, `delete_prereleases.yml`, `release.yml` in `.github/workflows/` are kept in sync from
  `LukeNeedham/ci-workflows` by CI, which keeps one PR titled "Sync from ci-workflows" open when they
  differ. Do not edit them here and do not copy them by hand: change them in ci-workflows. The callers
  hold no settings: every app is built the same way (see `docs/app-config.md` there).
- **Always give the user the AppHub link when work on this app completes.** As soon as a PR is open,
  paste `📦 [Open in AppHub](https://www.lukeneedham.com/apphub/pr/<repo name>/<PR number>)` into the chat
  on a line of its own, never inside a sentence (CI posts the same link as a PR comment), and put the same
  line at the very top of the PR description. Do not wait for the build and do not paste the APK link instead.
- **`README.md` is generated by CI from `readme.yml`**: never edit `README.md`, edit `readme.yml` (write it once
  from `docs/readme-template.yml` in `LukeNeedham/ci-workflows` if it is missing) and keep it accurate. Every app
  has a `LICENSE` file. The skill has the details.
- **Every page is an `XPage` plus a stateless `internal` `XPageContent`, always** (`XPage` gets its
  `XViewModel`; its route is always `Route.X`, a member of the one `sealed interface Route`; each of `XPage`,
  `XPageContent`, `XViewModel` and `XUiState` is in its own file; never `XScreen`). The README screenshots are private
  `@Preview`s in the package `screenshot` (in `src/debug`), each owning its mock data (a page can have several,
  or none; nothing is shared); CI renders them into `docs/screenshots/`, never by hand. The skill has the details.
<!-- ci-workflows:end -->
