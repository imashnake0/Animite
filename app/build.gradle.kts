import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
}

android {
    defaultConfig {
        applicationId = "com.imashnake.animite"
        versionCode = 13
        versionName = "0.1.1-alpha01"
    }

    signingConfigs {
        register("release") {
            val storeFilePath: String? by project
            val storePass: String? by project
            val key: String? by project
            val keyPass: String? by project
            this.storeFile = storeFilePath?.let { file(it) }
            this.storePassword = storePass
            this.keyAlias = key
            this.keyPassword = keyPass
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName(
                if (System.getenv("Animite") == "true") "release" else "debug"
            )
        }
    }

    buildFeatures { compose = true }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    namespace = "com.imashnake.animite"
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
    implementation(projects.core)
    implementation(projects.media)
    implementation(projects.navigation)
    implementation(projects.profile)
    implementation(projects.social)

    // AndroidX
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.navigationCompose)

    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.text.googleFonts)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)

    // Coil
    implementation(libs.bundles.coil)

    // Kotlin
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlinx.serialization.core)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.android.compiler)

    implementation(libs.materialKolor)

    coreLibraryDesugaring(libs.android.desugaring)

    testImplementation(libs.test.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}
