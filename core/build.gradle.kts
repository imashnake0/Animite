plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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

    namespace = "com.imashnake.animite.core"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // AndroidX
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleRuntimeKtx)

    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)

    // Compose Markdown
    implementation(libs.boswelja.composeMarkdown.material3)

    // Coil
    implementation(libs.bundles.coil)

    // Kotlin
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)

    testImplementation(libs.test.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}
