plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.compose)

    id("com.ncorti.ktfmt.gradle") version "0.18.0"
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

ktfmt {
    dropboxStyle()
    maxWidth.set(120)
}