plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.libraryMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.detekt)
}

kotlin {
    jvmToolchain(21)

    androidLibrary {
        namespace = "com.imashnake.animite.core.ui"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.compose)
            implementation(libs.compose.animation.graphics)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.ui.toolingPreview)
            implementation(compose.components.resources)

            // Compose Markdown
            implementation(libs.boswelja.composeMarkdown.material3)

            // Coil
            implementation(libs.bundles.coil)

            // Kotlin
            implementation(libs.kotlinx.collectionsImmutable)
            implementation(libs.kotlinx.datetime)

        }
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    basePath = rootDir.absolutePath
}
