import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

dependencies {
    // COMPOSE MODULES
    implementation(compose.desktop.currentOs)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.components.resources)
    implementation(compose.components.uiToolingPreview)
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(compose.preview)

    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.uiTest)
    implementation(compose.materialIconsExtended)

    // REVERSI MODULES
    implementation(project(":reversi-core"))
    implementation(project(":reversi-utils"))
    implementation(libs.ktflag)
    implementation(libs.coroutines)

    // TEST MODULE
    testImplementation(kotlin("test"))
}



compose.desktop {
    application {
        mainClass = "pt.isel.reversi.app.MainKt"

        nativeDistributions {

            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )

            packageName = "reversi"
            packageVersion = rootProject.version.toString()

            macOS {
                dockName = "Reversi"
                bundleID = "pt.isel.reversi.app"

                // Ícone da aplicação
                iconFile.set(project.file("src/main/resources/reversi.icns"))

                // Configurações adicionais para o ícone funcionar
                packageBuildVersion = rootProject.version.toString()

                // Info.plist customizado para forçar o ícone
                infoPlist {
                    extraKeysRawXml = """
                        <key>CFBundleIconFile</key>
                        <string>reversi.icns</string>
                        <key>LSApplicationCategoryType</key>
                        <string>public.app-category.games</string>
                    """.trimIndent()
                }
            }

            // Disable ProGuard for release builds
            buildTypes.release.proguard {
                isEnabled.set(false)
            }
        }
    }
}


// === Fat Jar executável ===
tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Assembles an executable fat jar including all dependencies."

    archiveBaseName.set("reversi-app")
    archiveVersion.set("v1.0.1")
    archiveClassifier.set("")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith(".jar") }
            .map { zipTree(it) }
    })

    manifest {
        attributes["Main-Class"] = "pt.isel.reversi.app.MainKt"
    }
}

kotlin {
    jvmToolchain(21)
}

tasks {
    build {
        dependsOn(
            "fatJar",
            "createDistributable"
        )
    }
}