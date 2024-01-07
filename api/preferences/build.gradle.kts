plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
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

    namespace = "com.imashnake.animite.api.preferences"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.android.compiler)

    // DataStore
    implementation(libs.datastore)
}
