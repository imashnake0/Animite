plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
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

    namespace = "com.imashnake.animite.components"
}

kotlin {
    jvmToolchain(21)

    androidTarget()
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.uiTooling)
            implementation(libs.bundles.coil)
        }
        androidMain.dependencies {
            implementation(libs.ktor.engine.android)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.engine.java)
        }
    }
}
