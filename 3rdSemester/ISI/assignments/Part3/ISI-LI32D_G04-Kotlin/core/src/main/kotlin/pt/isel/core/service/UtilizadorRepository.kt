package pt.isel.core.service

import java.sql.Connection

/**
 * Repository responsible for all user-related database operations.
 *
 * Provides methods to retrieve users, query users who consulted all resources,
 * and find psychologists based on intervention criteria.
 *
 * @property connection Active JDBC connection to the database
 */
class UtilizadorRepository(private val connection: Connection) {

    /**
     * Retrieves all users with their IDs and nicknames.
     *
     * @return List of formatted strings showing user ID and nickname, with header row first
     */
    fun getUtilizadores(): List<String> {
        val sql = "SELECT idUtilizador, alcunha FROM utilizador"
        val result = mutableListOf<String>()
        result.add("ID | Alcunha")
        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val id = rs.getInt("idUtilizador")
                    val alcunha = rs.getString("alcunha")
                    result.add("$id | $alcunha")
                }
            }
        }
        return result
    }

    /**
     * Finds psychologists who made more than a specified number of interventions
     * on cases with severity above a given threshold.
     *
     * This query groups by psychologist and filters based on case severity and intervention count.
     *
     * @param aboveAVGGravidade Minimum severity level (grauDeGravidade) for cases
     * @param aboveIntervencao Minimum number of interventions required
     * @return List of formatted strings showing psychologist nicknames
     */
    fun getUtilizadoresWithConditions(aboveAVGGravidade: Int, aboveIntervencao: Int): List<String> {
        val sql = """
            SELECT 
                U.alcunha as alcunha
            FROM INTERVEM I
            JOIN UTILIZADOR U ON I.idPsicologo = U.idUtilizador
            JOIN CASO_DE_CYBERBULLYING C ON I.idCaso = C.idCaso
            JOIN AVALIA Av ON C.idCaso = Av.idCasoDeCyberbullying
            WHERE Av.grauDeGravidade > ?
            GROUP BY U.idUtilizador, U.alcunha
            HAVING COUNT(*) > ?;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("Utilizadores")

        connection.prepareStatement(sql).use { ps ->
            ps.setInt(1, aboveAVGGravidade)
            ps.setInt(2, aboveIntervencao)
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val alcunha = rs.getString("alcunha")
                    result.add("$alcunha")
                }
            }
        }

        return result
    }

    /**
     * Retrieves users who have consulted all available educational resources.
     *
     * Uses relational division: counts distinct resources consulted per user and compares
     * with the total number of resources. Only users who consulted every resource are returned.
     *
     * @return List of formatted strings showing user nicknames who viewed all resources
     */
    fun getUtilizadoresConsultaramTodosRecursos(): List<String> {
        val sql = """
            SELECT
                U.alcunha
            FROM
                CONSULTA C
            INNER JOIN
                UTILIZADOR U ON C.idUtilizador = U.idUtilizador
            GROUP BY
                U.idUtilizador, U.alcunha
            HAVING
                COUNT(DISTINCT C.idRecurso) = 
                (        
                    SELECT COUNT(idRecurso) FROM RECURSO_EDUCATIVO
                );  
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("Utilizadores")
        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val alcunha = rs.getString("alcunha")
                    result.add("$alcunha")
                }
            }
        }

        return result
    }
}
