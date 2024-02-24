plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    namespace = "com.imashnake.animite.profile"
}

kotlin {
    jvmToolchain(17)
}

ksp {
    arg("compose-destinations.mode", "navgraphs")
    arg("compose-destinations.moduleName", project.name)
}

dependencies {
    implementation(projects.core)
    implementation(projects.api.anilist)
    implementation(projects.api.preferences)

    // AndroidX
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.lifecycleRuntimeKtx)

    // Compose
    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)

    // Compose Markdown
    implementation(libs.boswelja.compose.core)
    implementation(libs.boswelja.compose.markdown)

    // Coil
    implementation(libs.coil.compose)

    // Kotlin
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigationCompose)
    ksp(libs.hilt.android.compiler)

    // Compose Destinations
    implementation(libs.compose.destinations)
    ksp(libs.compose.destinations.ksp)

    testImplementation(libs.test.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}
