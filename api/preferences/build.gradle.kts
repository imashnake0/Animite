plugins {
    alias(libs.plugins.android.libraryMultiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.detekt)
}

kotlin {
    jvmToolchain(21)

    android {
        namespace = "com.imashnake.animite.api.preferences"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.datastore)
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    basePath = rootDir.absolutePath
}
