import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
}

buildscript { dependencies { classpath("org.jetbrains.dokka:dokka-base:2.0.0") } }

group = "pt.isel.reversi"

version = "2.0.0"

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven { url = uri("https://jitpack.io") }
    }

    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dokka {
        moduleName.set(project.name)

        dokkaSourceSets.configureEach {
            documentedVisibilities(
                VisibilityModifier.Public,
                VisibilityModifier.Private,
                VisibilityModifier.Protected,
                VisibilityModifier.Internal
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
            footerMessage = "2025 ISEL - TDS"
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

val reversiCliJar =
    tasks.register<Copy>("copyReversiCliJar") {
        dependsOn(":reversi-cli:build")
        from(project(":reversi-cli").layout.buildDirectory.dir("libs"))
        into(layout.buildDirectory.dir("libs"))
        include("*.jar")
    }

val reversiAppJar = tasks.register<Copy>("copyReversiAppJar") {
    dependsOn(":reversi-app:build")

    from(project(":reversi-app").layout.buildDirectory.dir("libs")) {
        include("*.jar")
    }

    into(layout.buildDirectory.dir("libs"))
}

val reversiDistributions = tasks.register<Copy>("copyReversiDistributions") {
    dependsOn(":reversi-app:build")

    from(project(":reversi-app").layout.buildDirectory.dir("compose/binaries/main/app")) {
        include("**/*")
    }

    into(layout.buildDirectory.dir("distributions"))
}

tasks.named("build") {
    dependsOn(reversiCliJar, reversiAppJar, reversiDistributions)
    dependsOn(tasks.dokkaGenerate)
}