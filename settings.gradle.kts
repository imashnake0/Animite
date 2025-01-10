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
    id("com.android.settings") version "8.8.0"
}

rootProject.name = "Animite"
include(
    ":api:anilist",
    ":api:mal",
    ":api:preferences",
    ":app",
    ":core",
    ":media",
    ":navigation",
    ":profile",
    ":social",
)

android {
    buildToolsVersion = "35.0.0"
    compileSdk = 35
    targetSdk = 35
    minSdk = 29
}

enableFeaturePreview(TYPESAFE_PROJECT_ACCESSORS.name)
