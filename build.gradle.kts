// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(deps.plugins.application) version(deps.plugins.versions.agp) apply false
    id(deps.plugins.kotlinAndroid) version(deps.plugins.versions.kotlin) apply false
    id(deps.plugins.kotlinCompose) version(deps.plugins.versions.kotlin) apply false
}