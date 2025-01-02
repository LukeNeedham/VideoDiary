plugins {
    id(deps.plugins.application)
    id(deps.plugins.kotlinAndroid)
    id(deps.plugins.kotlinCompose)
    id(deps.plugins.parcelize)
}

android {
    namespace = "com.lukeneedham.videodiary"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lukeneedham.videodiary"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
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
    implementation(deps.libs.compose.material3)

    // Navigation
    implementation(deps.libs.navigation.compose)

    // Media3
    implementation(deps.libs.androidx.media3)

    // Koin
    implementation(platform(deps.libs.koin.bom))
    implementation(deps.libs.koin.core)
    implementation(deps.libs.koin.android)
    implementation(deps.libs.koin.compose)
    implementation(deps.libs.koin.viewModel)

    // Datastore
    implementation(deps.libs.androidx.datastore)

    // Video editing -
    // These are local jar files because I had to tweak them to get them working on Android.
    // Namely, I had to remove the `PropertyBoxParserImpl` class from the jar,
    // so that we can build the project using a custom implementation of that class
    // (which works properly on Android)
    // without getting errors about having duplicate classes at build time.
    // The custom class of the same name is located within this Android project.
    implementation(files("libs/mp4parser/isoparser-1.9.56.jar"))
    implementation(files("libs/mp4parser/muxer-1.9.56.jar"))

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