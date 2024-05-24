plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)

    kotlin("plugin.serialization") version "1.9.22"
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.koin.core)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}
