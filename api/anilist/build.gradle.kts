plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.apollo.kotlin)
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

    // Apollo Kotlin
    api(libs.apollo.runtime)
    implementation(libs.apollo.cache.memory)
    implementation(libs.apollo.cache.sqlite)

    // Kotlin
    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlinx.collectionsImmutable)
    implementation(libs.compose.runtime.annotation)

    testImplementation(libs.test.junit)
}

apollo {
    service("anilist") {
        packageName = "com.imashnake.animite.api.anilist"

        plugin("${libs.plugins.apollo.cache.get().pluginId}:${libs.versions.apolloCache.get()}")
        pluginArgument("com.apollographql.cache.packageName", packageName.get())
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    basePath = rootDir.absolutePath
}
