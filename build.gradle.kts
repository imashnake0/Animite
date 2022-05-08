// TODO: Remove this after https://youtrack.jetbrains.com/issue/KTIJ-19369 is resolved.
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(deps.plugins.agp) apply false
    alias(deps.plugins.kotlin) apply false
    alias(deps.plugins.hilt) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
