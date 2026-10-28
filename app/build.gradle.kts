import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(deps.plugins.application)
    id(deps.plugins.kotlinAndroid)
    id(deps.plugins.kotlinCompose)
    id(deps.plugins.parcelize)
    id(deps.plugins.kotlinSerialization)
    id(deps.plugins.ksp)
}

android {
    namespace = "com.lukeneedham.videodiary"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lukeneedham.videodiary"
        minSdk = 23
        targetSdk = 35
        versionCode = 9
        versionName = "1.0.8"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("debugConfig") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            // Whether the shared library's logger and crash handler save logs and crashes to disk,
            // and the Logs and Crashes pages can be opened. Off in release builds.
            buildConfigField("boolean", "DEBUG_KIT_ENABLED", "false")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("boolean", "DEBUG_KIT_ENABLED", "true")
            applicationIdSuffix = ".dbg"
            signingConfig = signingConfigs.getByName("debugConfig")
        }
        // For sending a build to testers (not named "tester": build type names cannot start with "test").
        // Signed with the debug keystore, so it installs without the official key. BuildConfig.DEBUG is
        // false, so none of the debug-only features are in it (debug settings, fake SD card), but the
        // Logs and Crashes pages are, so testers can send them back. Installs next to the other builds.
        create("beta") {
            initWith(getByName("debug"))
            isDebuggable = false
            buildConfigField("boolean", "DEBUG_KIT_ENABLED", "true")
            applicationIdSuffix = ".beta"
            matchingFallbacks += listOf("release", "debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    // Disable encrypted dependency info in apks - required for reproducible builds on F-Droid
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

}

// `android { kotlinOptions }` is an error since Kotlin 2.2
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {

    // AndroidX
    implementation(deps.libs.androidx.coreKtx)
    implementation(deps.libs.androidx.lifecycle)

    // Camera
    implementation(deps.libs.camera.core)
    implementation(deps.libs.camera.camera2)
    implementation(deps.libs.camera.lifecycle)
    implementation(deps.libs.camera.view)
    implementation(deps.libs.camera.video)

    // Compose
    implementation(deps.libs.compose.activity)
    implementation(platform(deps.libs.compose.bom))
    implementation(deps.libs.compose.ui)
    implementation(deps.libs.compose.uiGraphics)
    implementation(deps.libs.compose.tooling.preview)
    implementation(deps.libs.compose.material)
    implementation(deps.libs.compose.material3)

    // Navigation
    implementation(deps.libs.navigation.runtime)
    implementation(deps.libs.navigation.ui)
    implementation(deps.libs.navigation.viewModel)
    implementation(deps.libs.navigation.serialization)

    // Media3
    implementation(deps.libs.media3.player)
    implementation(deps.libs.media3.transformer)
    implementation(deps.libs.media3.effect)

    // Coil
    implementation(deps.libs.coil.compose)

    // Koin
    implementation(platform(deps.libs.koin.bom))
    implementation(deps.libs.koin.core)
    implementation(deps.libs.koin.android)
    implementation(deps.libs.koin.compose)
    implementation(deps.libs.koin.viewModel)

    // Shared logger, crash handler and debug pages
    implementation(deps.libs.common.library)

    // Datastore
    implementation(deps.libs.androidx.datastore)

    // Room
    implementation(deps.libs.room.runtime)
    implementation(deps.libs.room.ktx)
    ksp(deps.libs.room.compiler)

    // Testing
    testImplementation(deps.libs.testing.junit)
    androidTestImplementation(deps.libs.testing.androidxJunit)
    androidTestImplementation(deps.libs.testing.espresso)
    androidTestImplementation(platform(deps.libs.compose.bom))
    androidTestImplementation(deps.libs.testing.androidxJunit)

    // Tooling
    debugImplementation(deps.libs.compose.tooling.core)
    debugImplementation(deps.libs.compose.tooling.manifest)

    // Desugaring
    coreLibraryDesugaring(deps.libs.desugaring.coreLibrary)
}