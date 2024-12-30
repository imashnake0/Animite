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

    namespace = "com.imashnake.animite.core"
}

kotlin {
    jvmToolchain(21)

    androidTarget()

    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(compose.material3)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)

            // Compose Markdown
            implementation(libs.boswelja.composeMarkdown.material3)

            // Coil
            implementation(libs.bundles.coil)

            // Kotlin
            implementation(libs.kotlin.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.engine.android)
        }
        androidUnitTest.dependencies {
            implementation(libs.test.junit)
        }
        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.test.junit)
            implementation(libs.androidx.test.espressoCore)
            implementation(libs.compose.test.ui.testJunit4)
        }
    }
}

compose.resources {
    publicResClass = true
}
