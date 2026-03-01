package pt.isel.cli

import pt.isel.cli.controller.MenuType
import pt.isel.core.service.DatabaseClient
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

/**
 * Entry point for the CLI application.
 *
 * Establishes a database connection, initializes the DatabaseClient, and runs the interactive
 * menu system in a loop until the user exits. Ensures proper cleanup of database resources.
 *
 * ## Required Environment Variables
 * - `DB_URL`: PostgreSQL connection URL (e.g., "jdbc:postgresql://localhost:5432/dbname")
 * - `DB_USER`: Database username
 * - `DB_PASSWORD`: Database password
 *
 * @throws IllegalStateException if any required environment variable is not set
 * @throws SQLException if database connection or operations fail
 */
fun main() {
    var dataBase: DatabaseClient? = null

    // TODO: data base does not create the table structure?
    val dbPassword = System.getenv("DB_PASSWORD")
        ?: throw IllegalStateException("A variável DB_PASSWORD não foi definida no ambiente.")
    val dbUser = System.getenv("DB_USER")
        ?: throw IllegalStateException("A variável DB_USER não foi definida no ambiente.")
    val url = System.getenv("DB_URL")
        ?: throw IllegalStateException("A variável DB_URL não foi definida no ambiente.")

    try {
        val connection = createConnection(
            dbUser = dbUser,
            dbPassword = dbPassword,
            url = url
        )
        dataBase = DatabaseClient(connection)
        var currentMenu = MenuType.MAIN_MENU

        do {
            currentMenu.print()
            val option = readlnOrNull()?.trim()?.toIntOrNull() ?: -1
            currentMenu = currentMenu.controller(option, dataBase) ?: break
        } while (true)
    } catch (e: SQLException) {
        e.printStackTrace()
    } finally {
        try {
            dataBase?.close()
            println("Ligação terminada")
        } catch (_: SQLException) {
        }
    }
}

/**
 * Creates a JDBC connection to the PostgreSQL database.
 *
 * @param dbUser Database username
 * @param dbPassword Database password
 * @param url JDBC connection URL
 * @return Active database Connection
 * @throws SQLException if the connection cannot be established
 */
fun createConnection(dbUser: String, dbPassword: String, url: String): Connection {
    val props = Properties().apply {
        put("user", dbUser)
        put("password", dbPassword)
    }
    return DriverManager.getConnection(url, props)
}
