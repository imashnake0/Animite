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
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.navigation:navigation-compose:2.4.1")

    // Compose
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.material3:material3:1.0.0-alpha07")
    implementation("androidx.compose.runtime:runtime:1.2.0-alpha05")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.compose.foundation:foundation:1.2.0-alpha08")

    // Apollo Kotlin
    implementation("com.apollographql.apollo3:apollo-runtime:3.2.2")

    // Coil
    implementation("io.coil-kt:coil-compose:2.0.0-rc01")

    // Kotlin coroutines TODO: BoM?
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    // Kotlin
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.41")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-android-compiler:2.41")

    // Accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.7-alpha")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")

    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
}
