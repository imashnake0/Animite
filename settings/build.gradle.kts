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

    namespace = "com.imashnake.animite.settings"
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
    implementation(projects.api.anilist)
    implementation(projects.api.preferences)
    implementation(projects.core.model)
    implementation(projects.core.ui)
    implementation(projects.banner)
    implementation(projects.media)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycleRuntimeKtx)

    // Compose
    implementation(libs.compose.animation)
    implementation(libs.compose.components.resources)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)

    implementation(libs.materialKolor)

    // Kotlin
    implementation(libs.kotlinx.collectionsImmutable)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.android.compiler)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    basePath = rootDir.absolutePath
}
