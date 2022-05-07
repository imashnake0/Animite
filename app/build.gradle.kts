@file:Suppress("SpellCheckingInspection")

plugins {
    id("com.android.application") version ("7.1.3")
    id("org.jetbrains.kotlin.android") version ("1.6.10")
    id("com.apollographql.apollo3") version ("3.2.2")
    id("com.google.dagger.hilt.android") version ("2.41")
    kotlin("kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.imashnake.animite"
        minSdk = 26
        targetSdk = 31
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
        kotlinCompilerExtensionVersion = "1.1.1"
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
    implementation(deps.androidx.activityCompose)
    implementation(deps.androidx.coreKtx)
    implementation(deps.androidx.lifecycle.runtimeKtx)
    implementation(deps.androidx.navigation.compose)

    // Compose
    implementation(deps.compose.material)
    implementation(deps.compose.material3)
    implementation(deps.compose.runtime)
    implementation(deps.compose.ui)
    implementation(deps.compose.uiToolingPreview)
    implementation(deps.compose.foundation)

    // Apollo Kotlin
    implementation(deps.apolloKotlin.runtime)

    // Coil
    implementation(deps.coil.compose)

    // Kotlin
    implementation(deps.kotlin.coroutines.android)
    implementation(deps.kotlin.coroutines.core)
    implementation(deps.kotlin.datetime)
    implementation(kotlin("reflect"))

    // Hilt
    implementation(deps.hilt.android)
    implementation(deps.hilt.navigationCompose)
    kapt(deps.hilt.android.compiler)

    // Accompanist
    implementation(deps.accompanist.systemUiController)

    testImplementation(deps.test.junit)

    androidTestImplementation(deps.androidx.test.junit)
    androidTestImplementation(deps.androidx.test.espresso.core)
    androidTestImplementation(deps.compose.test.uiTestJunit4)

    debugImplementation(deps.compose.test.uiTooling)
}
