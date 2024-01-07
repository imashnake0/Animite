@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.apolloKotlin)
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    namespace = "com.imashnake.animite.api.anilist"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Apollo Kotlin
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.cache.memory)
    implementation(libs.apollo.cache.sqlite)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.android.compiler)

    // DataStore
    implementation(libs.datastore)
}

apollo {
    service("anilist") {
        packageName.set("com.imashnake.animite.api.anilist")
    }
}
