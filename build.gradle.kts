import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

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

tasks.register<Delete>("clean") {
    description = "Clean"
    delete(rootProject.layout.buildDirectory)
}

val detektMerge = tasks.register<ReportMergeTask>("detekt merge") {
    description = "Detekt Merge"
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.sarif"))
}

subprojects {
    tasks.withType<Detekt>().configureEach {
        finalizedBy(detektMerge)
    }

    detektMerge {
        input.from(tasks.withType<Detekt>().map { it.sarifReportFile })
    }
}
