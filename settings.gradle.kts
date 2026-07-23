@file:Suppress("UnstableApiUsage")

import org.gradle.api.internal.FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS

pluginManagement {
    repositories {
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("com.android.settings") version "9.3.1"
}

rootProject.name = "Animite"
include(
    ":anime",
    ":api:anilist",
    ":api:mal",
    ":api:preferences",
    ":app",
    ":banner",
    ":core:model",
    ":core:resource",
    ":core:ui",
    ":manga",
    ":media",
    ":navigation",
    ":profile",
    ":settings",
    ":social",
    ":explore"
)

android {
    buildToolsVersion = "37.0.0"
    compileSdk = 37
    targetSdk = 37
    minSdk = 29
}

enableFeaturePreview(TYPESAFE_PROJECT_ACCESSORS.name)
