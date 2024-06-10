val kotlinxCoroutinesVersion: String by project
val neo4jDriverVersion: String by project
val composeVersion: String by project
val junitVersion: String by project

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
}

dependencies {
    implementation(compose.desktop.currentOs)
    testImplementation("org.jetbrains.compose.ui:ui-test-junit4:$composeVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$kotlinxCoroutinesVersion")

    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    implementation("org.neo4j.driver:neo4j-java-driver:$neo4jDriverVersion")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}
