plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("com.google.dagger.hilt.android") apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
