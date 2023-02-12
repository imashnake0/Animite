@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.50.2"
////                            # available:"0.51.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Animite"
include(
    ":api:anilist",
    ":material-color-utilities",
    ":core",
    ":profile",
    ":rslash",
    ":app"
)
