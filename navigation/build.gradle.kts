plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
}

android {
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures { compose = true }

    namespace = "com.imashnake.animite.navigation"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(libs.androidx.navigationCompose)
    implementation(libs.bundles.compose)
    implementation(libs.compose.material)
    implementation(libs.compose.animation.graphics)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)
    implementation(libs.kotlinx.serialization.core)
}
