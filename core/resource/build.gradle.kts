plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.libraryMultiplatform)
    alias(libs.plugins.detekt)
}

kotlin {
    explicitApi()
    jvmToolchain(21)

    androidLibrary {
        namespace = "com.imashnake.animite.core.resource"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)

        }
    }
}
