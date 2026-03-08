plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.libraryMultiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

val detektMerge by tasks.registering(io.gitlab.arturbosch.detekt.report.ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.sarif"))
}

subprojects {
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        finalizedBy(detektMerge)
    }

    detektMerge {
        input.from(tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().map { it.sarifReportFile })
    }
}
