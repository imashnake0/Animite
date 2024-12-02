plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
}

android {
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    namespace = "com.google.material3.color"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.compose.material3)
    implementation(libs.androidx.coreKtx)
}
