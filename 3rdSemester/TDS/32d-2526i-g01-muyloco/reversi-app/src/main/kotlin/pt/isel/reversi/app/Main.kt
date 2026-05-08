package pt.isel.reversi.app

import pt.isel.reversi.app.app.App

/**
 * Entry point for the desktop Reversi application. Initializes app dependencies
 * and launches the Compose window with the current `AppState`.
 *
 * @param args Optional command-line arguments forwarded to initialization.
 */
fun main(args: Array<String>) {
    val app = App(args)
    app.start()
}