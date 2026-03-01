package pt.isel.core.service

import java.sql.Connection
import java.sql.SQLException

/**
 * Repository responsible for all interaction-related database operations.
 *
 * Provides methods to insert new interactions, retrieve interaction data, and query interactions
 * with associated moderator analysis and cyberbullying cases.
 *
 * @property connection Active JDBC connection to the database
 */
class InteracaoRepository(private val connection: Connection) {

    /**
     * Calculates the next available interaction ID.
     *
     * Queries the database for the maximum existing ID and returns the next sequential value.
     * Returns 0 if no interactions exist yet.
     *
     * @return The next available interaction ID
     */
    private fun calculateIDInteracao(): Int {
        val sql = "SELECT MAX(idInteracao) AS max_id FROM interacao"
        var result = 0
        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                result = if (rs.next()) rs.getInt("max_id") + 1 else 0
            }
        }
        return result
    }

    /**
     * Inserts a new interaction into the database.
     *
     * Automatically generates a new ID for the interaction. Handles SQL exceptions gracefully
     * by printing an error message.
     *
     * @param interacao The interaction to insert (idInteracao will be auto-generated)
     * @throws SQLException if the insert operation fails
     */
    fun insertInteracao(interacao: Interacao) {
        val sql = "INSERT INTO interacao (idInteracao, idUtilizador, data, tipo, texto) VALUES (?, ?, ?, ?, ?)"
        // Convert String to java.sql.Date
        val date = java.sql.Date.valueOf(interacao.data)
        try {
            connection.prepareStatement(sql).use { ps ->
                ps.setInt(1, calculateIDInteracao())
                ps.setInt(2, interacao.idUtilizador)
                ps.setDate(3, date)
                ps.setString(4, interacao.tipo.valorSql)
                ps.setString(5, interacao.texto)
                ps.executeUpdate()
            }
        } catch (e: SQLException) {
            println("❌ Erro ao inserir a interação: ${e.message}")
        }
    }

    /**
     * Retrieves all interactions with moderator analysis information.
     *
     * Returns a formatted list showing moderator ID, flagging status (abusiva), user ID, and interaction ID.
     * Results are ordered by moderator ID, with unflagged interactions appearing first (moderator = null).
     *
     * @return List of formatted strings, with header row first
     */
    fun getInteracoes(): List<String> {
        val sql = """
            SELECT 
                I.idInteracao As interacao, 
                U.alcunha AS utilizador, 
                U_Mod.alcunha AS Moderador,
                A.abusiva as sinalizacao
            FROM INTERACAO I
            JOIN UTILIZADOR U ON I.idUtilizador = U.idUtilizador
            LEFT JOIN ANALISE A ON I.idInteracao = A.idInteracao
            LEFT JOIN UTILIZADOR U_Mod ON A.idModerador = U_Mod.idUtilizador
            ORDER BY 
                (A.idModerador IS NOT NULL),
                U_Mod.alcunha;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("moderador | sinalizacao | utilizador | interacao")
        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val idInteracao = rs.getInt("interacao")
                    val idUsuario = rs.getString("utilizador")
                    val idModerador = rs.getObject("moderador")
                    val abusiva = rs.getBoolean("sinalizacao")

                    result.add("$idModerador | $abusiva | $idUsuario | $idInteracao")
                }
            }
        }
        return result
    }

    /**
     * Retrieves all interactions created by a specific user.
     *
     * @param idUser The user ID to filter by
     * @return List of formatted strings showing interaction details, with header row first
     */
    fun getInteracaoesThisUser(idUser: Int): List<String> {
        val sql = """
            SELECT 
                I.idInteracao as interacao, 
                C.idCaso as caso, 
                C.estado as estado,
                I.texto as texto,
                I.data as data
            FROM INTERACAO I
            JOIN UTILIZADOR U ON I.idUtilizador = U.idUtilizador
            LEFT JOIN ANALISE A ON I.idInteracao = A.idInteracao
            LEFT JOIN CRIA Cr ON A.idAnalise = Cr.idAnalise
            LEFT JOIN CASO_DE_CYBERBULLYING C ON Cr.idCaso = C.idCaso
            WHERE U.idutilizador = ?;
        """.trimIndent()
        val result = mutableListOf<String>()
        result.add("Interação | caso | estado | texto | data")
        connection.prepareStatement(sql).use { ps ->
            ps.setInt(1, idUser)
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val id = rs.getInt("interacao")
                    val caso = rs.getString("caso")
                    val estado = rs.getString("estado")
                    val texto = rs.getString("texto")
                    val data = rs.getDate("data")

                    result.add("$id | $caso | $estado | $texto | $data" )
                }
            }
        }
        return result
    }

    /**
     * Retrieves all interactions from a specific user along with any associated cyberbullying cases.
     *
     * Uses a LEFT JOIN so interactions without cases are still included (with null case ID).
     *
     * @param idUser The user ID to filter by
     * @return List of formatted strings showing interaction ID and associated case IDs
     */
    fun getInteracoesThisUserWithCases(idUser: Int): List<String> {
        val sql = """
            SELECT 
                I.idInteracao as interacao, 
                I.texto, 
                C.idCaso as caso, 
                C.estado
            FROM INTERACAO I
            JOIN UTILIZADOR U ON I.idUtilizador = U.idUtilizador
            LEFT JOIN ANALISE A ON I.idInteracao = A.idInteracao
            LEFT JOIN CRIA Cr ON A.idAnalise = Cr.idAnalise
            LEFT JOIN CASO_DE_CYBERBULLYING C ON Cr.idCaso = C.idCaso
            WHERE U.idUtilizador = ?;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("Interação | texto | caso | estado")
        connection.prepareStatement(sql).use { ps ->
            ps.setInt(1, idUser)
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val interacaoId = rs.getInt("interacao")
                    val texto = rs.getString("texto")
                    val casoId = rs.getInt("caso")
                    val estado = rs.getString("estado")

                    result.add("$interacaoId | $texto | $casoId | $estado")
                }
            }
        }
        return result
    }
}

