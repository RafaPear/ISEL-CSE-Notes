import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    kotlin("jvm") version "2.1.20" apply false
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    application
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:2.0.0")
    }
}

group = "pt.isel.reversi"
version = "v0.1.0"

allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
    }

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.github.johnrengelman.shadow")

    /*tasks.withType<Test>().configureEach {
        ignoreFailures = true
    }*/

    tasks.withType<DokkaTaskPartial>().configureEach {
        dokkaSourceSets {
            configureEach {
                moduleName.set(project.name)
                includes.from("MODULE.md")
                suppressObviousFunctions.set(true)
                pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
                    footerMessage = "(c) 2025 ISEL"
                    separateInheritedMembers = false
                    mergeImplicitExpectActualDeclarations = true
                }
            }
        }
    }
}

tasks.dokkaHtmlMultiModule {
    moduleName.set("Reversi Docs")
    includes.from("README.md")
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "(c) 2025 ISEL"
        // Add all images png from the docs/assets folder to the final documentation
        customAssets += fileTree("images") {
            include("**/*.png")
        }
        separateInheritedMembers = false
        mergeImplicitExpectActualDeclarations = true
    }
}

fun createFatJarTask(
    taskName: String,
    subprojectPath: String,
    mainClass: String,
    jarName: String
) {
    tasks.register<ShadowJar>(taskName) {
        group = "build"
        description = "Cria o fat JAR $jarName do submódulo $subprojectPath"

        archiveBaseName.set(jarName)
        archiveVersion.set(rootProject.version.toString())
        archiveClassifier.set("")

        // Define a Main class
        manifest {
            attributes["Main-Class"] = mainClass
        }

        // Inclui classes e recursos do submódulo
        from(project(subprojectPath).sourceSets["main"].output)

        // Inclui dependências do submódulo
        configurations = listOf(project(subprojectPath).configurations.getByName("runtimeClasspath"))

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        // Garante que o submódulo é compilado antes
        dependsOn("${subprojectPath}:build")
    }
}

application {
    // Define a main class padrão (pode ser sobrescrito por submódulo)
    mainClass.set("pt.isel.reversi.cli.MainKt")
}

createFatJarTask(
    taskName = "reversiCliJar",
    subprojectPath = ":reversi-cli",
    mainClass = "pt.isel.reversi.cli.MainKt",
    jarName = "reversi-cli"
)

// Depois de criar as tasks com createFatJarTask
val reversiCliJar = tasks.named("reversiCliJar")

// Faz com que a task build do root dependa delas
tasks.named("build") {
    dependsOn(reversiCliJar)
}
