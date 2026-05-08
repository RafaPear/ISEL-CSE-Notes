package pt.isel.reversi.cli

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File

fun cleanup(func: suspend () -> Unit) {
    File(BASE_FOLDER).deleteRecursively()
    runBlocking { func() }
    File(BASE_FOLDER).deleteRecursively()
}