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
    id("com.android.settings") version("8.1.1")
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

configure<SettingsExtension> {
    buildToolsVersion = "34.0.0"
    compileSdk = 34
    minSdk = 26
}
