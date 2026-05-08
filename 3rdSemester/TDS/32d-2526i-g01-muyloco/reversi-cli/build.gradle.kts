plugins {
    application
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.ktflag)
    implementation(libs.coroutines)
    implementation(project(":reversi-core"))
    implementation(project(":reversi-utils"))
    testImplementation(libs.coroutines.test)
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("pt.isel.reversi.cli.MainKt")
}

// === Fat JAR Task ===
tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Assembles a fat jar including all dependencies."

    // Nome final
    archiveBaseName.set(project.name)
    archiveVersion.set(rootProject.version.toString())
    archiveClassifier.set("") // sem "-all" → substitui o jar padrão

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Inclui classes do projeto
    from(sourceSets.main.get().output)

    // Inclui dependências (todas as libs do classpath)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith(".jar") }
            .map { zipTree(it) }
    })

    // Manifest com Main-Class
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

kotlin {
    jvmToolchain(21)
}

// === Tornar o fatJar o padrão ===
tasks {
    build {
        dependsOn("fatJar")
    }

    jar {
        enabled = false // desativa o jar “magro” padrão
    }
}