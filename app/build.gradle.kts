@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

// TODO: Remove this after https://youtrack.jetbrains.com/issue/KTIJ-19369 is resolved.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.agp)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.apolloKotlin)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.imashnake.animite"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

apollo {
    packageName.set("com.imashnake.animite")
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // AndroidX
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.navigationCompose)

    // Compose
    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.toolingPreview)

    // Apollo Kotlin
    implementation(libs.apolloKotlin.runtime)

    // Coil
    implementation(libs.coil.compose)

    // Kotlin
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.datetime)
    implementation(kotlin("reflect"))

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    kapt(libs.hilt.android.compiler)

    // Accompanist
    implementation(libs.accompanist.systemUiController)

    testImplementation(libs.test.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}
