---
name: networking-setup
description: Optional. Adds networking to an app that makes network calls - the Ktor and OkHttp dependencies, the INTERNET permission, the HTTP client, DTOs and the debug network log. Use only when the app calls a web API or loads images from URLs; apps without networking must not have any of this.
---

# Networking setup

**A new app has no networking**: no Ktor, no `coil-network-okhttp` and no `INTERNET` permission. Do this only when the app
needs to call a web API or load images from URLs, and in the same PR as the feature that needs it. An app that does not
make network calls must never get the permission. The rules (DTOs, defensive modeling, mapping, the network log) are
in `docs/networking.md` of `LukeNeedham/ci-workflows`; this skill is the setup, step by step.

## 1. Version catalog (`gradle/libs.versions.toml`)

Check the current stable versions first, then add:

```toml
[versions]
ktor = "<current>"

[libraries]
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
# Only if images are loaded from URLs (Coil 3 has no network layer by default); `coil` is already in [versions]
coil-network-okhttp = { module = "io.coil-kt.coil3:coil-network-okhttp", version.ref = "coil" }
```

## 2. Dependencies (`app/build.gradle.kts`)

```kotlin
commonMain.dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    // kotlinx-serialization-json is already there
}
androidMain.dependencies {
    implementation(libs.ktor.client.okhttp)       // the OkHttp engine is Android only, so it is not in commonMain
    implementation(libs.coil.network.okhttp)      // only if images are loaded from URLs
}
```

## 3. The permission (`androidMain/AndroidManifest.xml`)

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

as the first child of `<manifest>`. Nothing else in the app declares it.

## 4. The client, once, with the network log

The client is `HttpClient(OkHttp)`, so it is built in `androidMain`, in a class of its own (never inside a Koin module):

```kotlin
// androidMain, package <app>.network
object HttpClientFactory {
    val json = Json { ignoreUnknownKeys = true; isLenient = true; coerceInputValues = true; explicitNulls = false }

    fun create(): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) { json(json) }
        engine { addInterceptor(NetworkLogInterceptor()) }   // com.lukeneedham.common.network: mandatory on every client
    }
}

// androidMain, NetworkModule.kt: a feature object, so its module is called `bindings`
object NetworkModule {
    val bindings = module { single { HttpClientFactory.create() } }
}
```

Bind the client's users (remote data sources, repositories) in the same way, and start Koin in `App` with
`modules(KoinModules.modules + NetworkModule.bindings)`. Nothing outside the networking layer touches `HttpClient`.

## 5. DTOs and the debug page

- Every network model is a `@Serializable` `XDto` with every property nullable and defaulting to `null`, enums as
  `String?`, mapped by `XDto.toDomain()` (never throwing) before it leaves the networking layer; failed calls are caught
  and logged with `Logger.e` (`docs/networking.md`).
- If the app shows the debug pages: add `Route.Network` (and register it in the back stack's `SavedStateConfiguration` in
  `AppNavigation`), pass `onOpenNetwork = { navigateTo(Route.Network) }` to `DebugPage` and add
  `Route.Network -> NetworkPage(onBack = ::onBack)` to `Router`.

## 6. Check

- The manifest has the permission, and the app really makes the calls it was added for.
- Every `HttpClient` has `NetworkLogInterceptor()`; there is no other HTTP client or engine.
- `readme.yml` mentions what the app needs the network for, if a reader would care.
- Claude cannot build: push, then read the PR build and fix what it says.
