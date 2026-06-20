plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.libraryMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.detekt)
}

kotlin {
    explicitApi()
    jvmToolchain(21)

    androidLibrary {
        namespace = "com.imashnake.animite.core.model"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.compose)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.core.ktx)
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    basePath = rootDir.absolutePath
}
