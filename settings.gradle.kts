pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://www.jitpack.io")
        }
        // Shared library (logger, crash handler, debug pages): https://github.com/LukeNeedham/ci-workflows
        maven {
            setUrl("https://lukeneedham.github.io/ci-workflows/maven")
            content { includeGroup("com.lukeneedham") }
        }
    }
}

rootProject.name = "VideoDiary"
include(":app")
 