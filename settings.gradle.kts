rootProject.name = "graphs-2"
include("app")


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
            version("kotlin", "1.9.20")
            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("compose", "org.jetbrains.compose").version("1.6.2")
            library("koin-core", "io.insert-koin:koin-core:3.5.3")
            library("junit-jupiter", "org.junit.jupiter:junit-jupiter:5.10.2")
        }
    }
}
