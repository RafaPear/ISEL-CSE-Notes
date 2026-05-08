package pt.isel.reversi.cli

import java.io.File

fun cleanup(func: () -> Unit) {
    File("saves").deleteRecursively()
    func()
    File("saves").deleteRecursively()
}