plugins {
    id("com.android.application") version ("7.1.3") apply false
    id("com.android.library") version ("7.1.3") apply false
    id("org.jetbrains.kotlin.android") version ("1.6.10") apply false
    id("com.google.dagger.hilt.android") version ("2.41") apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
