val neo4jDriverVersion: String by settings
val composeVersion: String by settings
val kotlinVersion: String by settings
val junitVersion: String by settings

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
    versionCatalogs {
        create("libs") {
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version(kotlinVersion)
            plugin("compose", "org.jetbrains.compose").version(composeVersion)
            library("junit-jupiter", "org.junit.jupiter:junit-jupiter:$junitVersion")
        }
    }
}

rootProject.name = "graphs-2"
include("app")
