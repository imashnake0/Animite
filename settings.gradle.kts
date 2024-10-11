@file:Suppress("UnstableApiUsage")

import org.gradle.api.internal.FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.settings") version "8.7.0"
}

rootProject.name = "Animite"
include(
    ":api:anilist",
    ":api:preferences",
    ":material-color-utilities",
    ":core",
    ":profile",
    ":rslash",
    ":app"
)

android {
    buildToolsVersion = "35.0.0"
    compileSdk = 35
    minSdk = 29
}

enableFeaturePreview(TYPESAFE_PROJECT_ACCESSORS.name)
