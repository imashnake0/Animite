plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.apolloKotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
}

android {
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    namespace = "com.imashnake.animite.api.anilist"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(projects.api.preferences)
    implementation(projects.core)

    // Apollo Kotlin
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.cache.memory)
    implementation(libs.apollo.cache.sqlite)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.kotlinx.collectionsImmutable)
    implementation(libs.compose.runtime.annotation)

    testImplementation(libs.test.junit)
}

apollo {
    service("anilist") {
        packageName = "com.imashnake.animite.api.anilist"
    }
}
