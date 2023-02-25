@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

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
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Animite"
include(
    ":api:anilist",
    ":material-color-utilities",
    ":core",
    ":profile",
    ":rslash",
    ":app",
    ":safer-navigation-compose:core",
    ":safer-navigation-compose:navigation-compose"
)
