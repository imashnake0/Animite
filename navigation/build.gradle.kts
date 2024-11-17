plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.detekt)
}

android {
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }
    composeCompiler.enableStrongSkippingMode = true

    namespace = "com.imashnake.animite.navigation"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api(libs.androidx.navigationCompose)
    implementation(libs.bundles.compose)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)
    implementation(libs.kotlinx.serialization.core)
}
