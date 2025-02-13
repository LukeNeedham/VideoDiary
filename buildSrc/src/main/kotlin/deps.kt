object deps {
    object plugins {
        object versions {
            val agp = "8.7.1"
            val kotlin = "2.0.0"
        }

        val application = "com.android.application"
        val kotlinAndroid = "org.jetbrains.kotlin.android"
        val kotlinCompose = "org.jetbrains.kotlin.plugin.compose"
        val parcelize = "kotlin-parcelize"
    }

    object libs {
        object androidx {
            val coreKtx = "androidx.core:core-ktx:1.15.0"
            val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:2.8.7"
            val datastore = "androidx.datastore:datastore-preferences:1.1.1"
        }

        object media3 {
            private val version = "1.5.1"
            val player = "androidx.media3:media3-exoplayer:$version"
            val transformer = "androidx.media3:media3-transformer:$version"
            val effect = "androidx.media3:media3-effect:$version"
        }

        object camera {
            private val version = "1.4.1"

            val core = "androidx.camera:camera-core:$version"
            val camera2 = "androidx.camera:camera-camera2:$version"
            val lifecycle = "androidx.camera:camera-lifecycle:$version"
            val view = "androidx.camera:camera-view:$version"
            val video = "androidx.camera:camera-video:$version"
        }

        object navigation {
            val compose = "dev.olshevski.navigation:reimagined:1.5.0"
        }

        object testing {
            val junit = "junit:junit:4.13.2"
            val androidxJunit = "androidx.test.ext:junit:1.2.1"
            val espresso = "androidx.test.espresso:espresso-core:3.6.1"
        }

        object compose {
            val activity = "androidx.activity:activity-compose:1.9.3"
            val bom = "androidx.compose:compose-bom:2024.04.01"
            val ui = "androidx.compose.ui:ui"
            val uiGraphics = "androidx.compose.ui:ui-graphics"
            val material3 = "androidx.compose.material3:material3"

            object tooling {
                val core = "androidx.compose.ui:ui-tooling"
                val preview = "androidx.compose.ui:ui-tooling-preview"
                val manifest = "androidx.compose.ui:ui-test-manifest"
            }
        }

        object desugaring {
            val coreLibrary = "com.android.tools:desugar_jdk_libs:2.0.2"
        }

        object koin {
            val bom = "io.insert-koin:koin-bom:4.0.1"
            val core = "io.insert-koin:koin-core"
            val android = "io.insert-koin:koin-android"
            val compose = "io.insert-koin:koin-compose"
            val viewModel = "io.insert-koin:koin-compose-viewmodel"
        }
    }
}