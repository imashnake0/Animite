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
    }
}

rootProject.name = "Animite"
include(
    ":material-color-utilities",
    ":core",
    ":profile",
    ":rslash",
    ":app"
)
