plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.compose)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.koin.core)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
