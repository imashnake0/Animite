@file:Suppress("SpellCheckingInspection")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

plugins {
    // TODO: This is for testing https://github.com/jmfayard/refreshVersions/pull/509.
    id("de.fayard.refreshVersions") version "0.41.0-SNAPSHOT"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Animite"
include(":app")
