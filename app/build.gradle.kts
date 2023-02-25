@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

// TODO: Remove this after https://youtrack.jetbrains.com/issue/KTIJ-19369 is resolved.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp) version libs.versions.ksp.get()
    kotlin("kapt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.imashnake.animite"
        minSdk = 26
        targetSdk = 33
        versionCode = 6
        versionName = "0.0.1-alpha06"

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
        isCoreLibraryDesugaringEnabled = true
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
    namespace = "com.imashnake.animite"

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}

ksp {
    arg("compose-destinations.mode", "destinations")
}

dependencies {
    implementation(project(":api:anilist"))
    implementation(project(":core"))
    implementation(project(":profile"))
    implementation(project(":rslash"))

    // AndroidX
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleRuntimeKtx)

    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.text.googleFonts)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)

    // Coil
    implementation(libs.coil.compose)

    // Kotlin
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    kapt(libs.hilt.android.compiler)

    // Accompanist
    implementation(libs.accompanist.systemUiController)
    implementation(libs.accompanist.placeholder)

    coreLibraryDesugaring(libs.android.desugaring)

    // Compose Destinations
    implementation(libs.compose.destinations)
    ksp(libs.compose.destinations.ksp)

    // kotlinx.serialization JSON
    implementation(libs.kotlinx.serialization.json)

    // Compose Navigation
    implementation(libs.navigation.compose)

    // Safer Navigation Compose
    // TODO: Resolve Safer Navigation Compose from JitPack when it's up again
    implementation(project(":safer-navigation-compose:core"))
    implementation(project(":safer-navigation-compose:navigation-compose"))


    testImplementation(libs.test.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}
