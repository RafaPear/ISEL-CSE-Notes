plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.shadow) apply false
}

buildscript { dependencies { classpath("org.jetbrains.dokka:dokka-base:2.0.0") } }

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dokka {
        moduleName.set(project.name)

        dokkaSourceSets.configureEach {
            documentedVisibilities(
                org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier.Public, // Same for both Kotlin and Java
                org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier.Private, // Same for both Kotlin and Java
                org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier.Protected, // Same for both Kotlin and Java
                org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier.Internal // Kotlin-specific internal modifier
            )
            val moduleDoc = file("MODULE.md")
            if (moduleDoc.exists()) {
                includes.from(moduleDoc)
            }
        }

        dokkaPublications.html {
            suppressInheritedMembers.set(true)
            suppressObviousFunctions.set(true)
        }

        pluginsConfiguration.html {
            footerMessage = "2025 ISEL - ISI"
            separateInheritedMembers = false
            mergeImplicitExpectActualDeclarations = true

            val imagesDir = file("images")
            if (imagesDir.exists()) {
                customAssets.from(fileTree(imagesDir) { include("**/*.png") })
            }
        }
    }
}

dokka {
    moduleName.set(project.name)

    dokkaPublications.html {
        val packageDoc = rootProject.file("README.md")
        if (packageDoc.exists()) {
            includes.from(packageDoc)
        }
    }
}

dependencies {
    for (project in subprojects) {
        dokka(project)
    }
    dokka(rootProject)
}

tasks.named("build") {
    dependsOn(tasks.dokkaGenerate)
}

group = "pt.isel"
version = "1.0"

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
        
    apply(plugin = "org.jetbrains.kotlin.jvm")
}

val cliJar =
    tasks.register<Copy>("copycliJar") {
        dependsOn(":cli:build")
        from(project(":cli").layout.buildDirectory.dir("libs"))
        into(layout.buildDirectory.dir("libs"))
    }

tasks.named("build") {
    dependsOn(cliJar)
}