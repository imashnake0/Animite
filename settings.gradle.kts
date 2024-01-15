@file:Suppress("UnstableApiUsage")
import com.android.build.api.dsl.SettingsExtension

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.settings") version("8.2.0-beta01")
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

configure<SettingsExtension> {
    buildToolsVersion = "34.0.0"
    compileSdk = 34
    minSdk = 29
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
