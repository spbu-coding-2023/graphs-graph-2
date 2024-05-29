plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.compose") version "1.6.2"
}

repositories {
    google() // to ensure dependencies like androidx.annotation can be resolved.
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("io.insert-koin:koin-core:3.4.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

    testImplementation("org.jetbrains.compose.ui:ui-test-junit4:1.6.2")
}


compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
