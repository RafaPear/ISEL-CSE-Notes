rootProject.name = "reversi"

include("reversi-core", "reversi-cli", "reversi-storage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

