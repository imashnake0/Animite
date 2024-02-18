plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)

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

    namespace = "com.imashnake.animite.compose.markdown"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.bundles.compose)

    implementation(libs.intellij.markdown)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)

    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.toolingPreview)
}
