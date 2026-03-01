import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
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

    namespace = "com.imashnake.animite.media"
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.addAll(
        "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi"
    )
}

dependencies {
    implementation(projects.navigation)
    implementation(projects.core)
    implementation(projects.api.anilist)
    implementation(projects.api.preferences)

    // AndroidX
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycleRuntimeKtx)

    // Coil
    implementation(libs.bundles.coil)

    // Compose
    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.animation.graphics)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)
    implementation(libs.compose.material3.adaptive)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.android.compiler)

    // Kotlin
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.collectionsImmutable)

    implementation(libs.materialKolor)
}
