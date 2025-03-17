plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    id("androidx.room")
}

android {
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    namespace = "com.imashnake.animite.search"

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

kotlin {
    jvmToolchain(21)
}

configurations.implementation {
    exclude(group = "com.intellij", module = "annotations")
}

dependencies {
    implementation(projects.api.anilist)
    implementation(projects.api.mal)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.ksp)
}