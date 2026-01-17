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
    id("com.android.settings") version "9.0.0"
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
    ":settings",
    ":social",
)

android {
    buildToolsVersion = "36.0.0"
    compileSdk = 36
    targetSdk = 36
    minSdk = 29
}

enableFeaturePreview(TYPESAFE_PROJECT_ACCESSORS.name)
