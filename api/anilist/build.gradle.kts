@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

// TODO: Remove this after https://youtrack.jetbrains.com/issue/KTIJ-19369 is resolved.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.apolloKotlin)
    kotlin("kapt")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    namespace = "com.imashnake.animite.api.anilist"

    // Workaround for KSP generated sources not being indexable by the IDE
    libraryVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    // Apollo Kotlin
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.cache.memory)
    implementation(libs.apollo.cache.sqlite)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    kapt(libs.hilt.android.compiler)
}

kapt {
    correctErrorTypes = true
}

apollo {
    service("anilist") {
        packageName.set("com.imashnake.animite.api.anilist")
    }
}
