package pt.isel.core.service

import java.sql.Connection

/**
 * Main database client that aggregates all repositories and manages the database connection.
 *
 * This class serves as the entry point for all database operations, providing access to specialized
 * repositories for different entities. It implements AutoCloseable to ensure proper connection cleanup.
 *
 * ## Usage Example
 * ```kotlin
 * val connection = DriverManager.getConnection(url, props)
 * DatabaseClient(connection).use { db ->
 *     val users = db.utilizadores.getUtilizadores()
 *     val cases = db.casos.getCasos()
 * }
 * ```
 *
 * @property connection The JDBC connection to the PostgreSQL database
 * @property interacoes Repository for interaction-related operations
 * @property casos Repository for cyberbullying case operations
 * @property utilizadores Repository for user management operations
 * @property relatorios Repository for generating reports and statistics
 */
class DatabaseClient(val connection: Connection) : AutoCloseable {
    val interacoes = InteracaoRepository(connection)
    val casos = CasoRepository(connection)
    val utilizadores = UtilizadorRepository(connection)
    val relatorios = RelatorioRepository(connection)

    /**
     * Closes the database connection if it's still open.
     * Prints a confirmation message when successfully closed.
     */
    override fun close() {
        if (!connection.isClosed) {
            connection.close()
            println("Conexão fechada.")
        }
    }
}

