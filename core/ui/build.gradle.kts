plugins {
    alias(libs.plugins.android.library)
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

    namespace = "com.imashnake.animite.core.ui"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.compose.animation.graphics)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)

    // Compose Markdown
    implementation(libs.boswelja.composeMarkdown.material3)

    // Coil
    implementation(libs.bundles.coil)

    // Kotlin
    implementation(libs.kotlinx.collectionsImmutable)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.test.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    basePath = rootDir.absolutePath
}
