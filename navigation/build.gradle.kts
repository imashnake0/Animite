plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures { compose = true }

    namespace = "com.imashnake.animite.navigation"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(libs.bundles.nav3)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.android.compiler)

    implementation(libs.bundles.compose)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.toolingPreview)

    implementation(libs.kotlinx.serialization.core)

    debugImplementation(libs.compose.ui.tooling)
}
