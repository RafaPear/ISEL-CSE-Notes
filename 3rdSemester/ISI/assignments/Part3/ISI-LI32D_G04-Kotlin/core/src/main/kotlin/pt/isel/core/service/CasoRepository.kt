package pt.isel.core.service

import java.sql.Connection
import java.sql.ResultSet

/**
 * Repository responsible for all cyberbullying case-related database operations.
 *
 * Provides methods to retrieve, filter, and query cyberbullying cases including those
 * with severity ratings above average and cases associated with specific interactions.
 *
 * @property connection Active JDBC connection to the database
 */
class CasoRepository(private val connection: Connection) {

    /**
     * Parses a ResultSet containing case data into a formatted list of strings.
     *
     * @param rs ResultSet positioned before the first row
     * @return List with header row followed by formatted case data rows
     */
    private fun parserCaso(rs: ResultSet): List<String> {
        val result: MutableList<String> = mutableListOf()
        result.add("ID | Estado | Data de Abertura | Descrição")
        while (rs.next()) {
            val id = rs.getInt("idCaso")
            val estado = rs.getString("estado")
            val dataAbertura = rs.getString("dataDeAbertura")
            val descricao = rs.getString("descricao") ?: "Sem descrição"

            result.add("$id | $estado | $dataAbertura | $descricao")
        }
        return result
    }

    /**
     * Retrieves all cyberbullying cases from the database.
     *
     * @return List of formatted strings showing case details, with header row first
     */
    fun getCasos(): List<String> {
        val sql = "SELECT * FROM caso_de_cyberbullying"
        var result: List<String>

        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                result = parserCaso(rs)
            }
        }

        return result
    }

    /**
     * Retrieves all cyberbullying cases associated with a specific interaction.
     *
     * @param idInteracao The interaction ID to filter cases by
     * @return List of formatted strings showing case details for the specified interaction
     */
    fun getCasosThisInteracao(idInteracao: Int): List<String> {
        val sql = """SELECT 
                    C.idCaso as caso,
                    C.estado,
                    C.descricao,
                    Av.grauDeGravidade,
                    Av.anotacao AS AnalisePsicologo
                FROM ANALISE A
                JOIN CRIA Cr ON A.idAnalise = Cr.idAnalise
                JOIN CASO_DE_CYBERBULLYING C ON Cr.idCaso = C.idCaso
                LEFT JOIN AVALIA Av ON C.idCaso = Av.idCasoDeCyberbullying
                WHERE A.idInteracao = ?;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("ID | Estado | Descrição | Grau de Gravidade | Análise do Psicólogo")
        connection.prepareStatement(sql).use { ps ->
            ps.setInt(1, idInteracao)
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val id = rs.getInt("caso")
                    val estado = rs.getString("estado")
                    val descricao = rs.getString("descricao") ?: "Sem descrição"
                    val grauDeGravidade = rs.getObject("grauDeGravidade") ?: "N/A"
                    val analisePsicologo = rs.getString("AnalisePsicologo") ?: "N/A"

                    result.add("$id | $estado | $descricao | $grauDeGravidade | $analisePsicologo")
                }
            }
        }
        return result
    }

    fun getCaso(id: Int): List<String> {
        val sql = """
            SELECT 
                C.idCaso as caso,
                I.idInteracao as interacao,
                I.texto AS TextoInteracao,
                A.abusiva,
                ModUser.alcunha AS NomeModerador,
                PsiUser.alcunha AS PsicologoAvaliador,
                Av.anotacao AS AnaliseFeita,
                IntPsi.alcunha AS PsicologoIntervencao,
                Intv.data AS DataIntervencao,
                Intv.tipo AS TipoIntervencao
            FROM CASO_DE_CYBERBULLYING C
            JOIN CRIA Cr ON C.idCaso = Cr.idCaso
            JOIN ANALISE A ON Cr.idAnalise = A.idAnalise
            JOIN INTERACAO I ON A.idInteracao = I.idInteracao
            JOIN UTILIZADOR ModUser ON A.idModerador = ModUser.idUtilizador
            LEFT JOIN AVALIA Av ON C.idCaso = Av.idCasoDeCyberbullying
            LEFT JOIN UTILIZADOR PsiUser ON Av.idUtilizador = PsiUser.idUtilizador
            LEFT JOIN INTERVEM Intv ON C.idCaso = Intv.idCaso
            LEFT JOIN UTILIZADOR IntPsi ON Intv.idPsicologo = IntPsi.idUtilizador
            WHERE C.idCaso = ?;
        """.trimIndent()

        val result = mutableListOf<String>()

        connection.prepareStatement(sql).use { ps ->
            ps.setInt(1, id)
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val idCaso = rs.getInt("caso")
                    val idInteracao = rs.getInt("interacao")
                    val textoInteracao = rs.getString("TextoInteracao")
                    val abusiva = rs.getBoolean("abusiva")
                    val nomeModerador = rs.getString("NomeModerador")
                    val psicologoAvaliador = rs.getString("PsicologoAvaliador")
                    val analiseFeita = rs.getString("AnaliseFeita")
                    val psicologoIntervencao = rs.getString("PsicologoIntervencao")
                    val dataIntervencao = rs.getString("DataIntervencao")
                    val tipoIntervencao = rs.getString("TipoIntervencao")

                    result.add("Caso ID: $idCaso")
                    result.add("Interação ID: $idInteracao")
                    result.add("Texto da Interação: $textoInteracao")
                    result.add("Sinalizada como Abusiva: $abusiva")
                    result.add("Moderador: $nomeModerador")
                    result.add("Psicólogo Avaliador: $psicologoAvaliador")
                    result.add("Análise Feita: $analiseFeita")
                    result.add("Psicólogo de Intervenção: $psicologoIntervencao")
                    result.add("Data de Intervenção: $dataIntervencao")
                    result.add("Tipo de Intervenção: $tipoIntervencao")
                }
            }
        }
        return result
    }

    /**
     * Retrieves all cases with severity ratings above the global average.
     *
     * Calculates the average severity (grauDeGravidade) across all evaluated cases,
     * then returns cases that exceed this average.
     *
     * @return List of formatted strings showing case IDs that are above average severity
     */
    fun getCasosAboveAvgGravidade(): List<String> {
        val sql = """ 
            SELECT
                c.idCaso as casos
            FROM caso_de_cyberbullying c
            INNER JOIN avalia a ON a.idcasodecyberbullying = c.idCaso
            WHERE
                a.graudegravidade > (
                    SELECT
                        AVG(graudegravidade)
                    FROM avalia
                    WHERE graudegravidade IS NOT NULL
                    
                )
        """.trimIndent()

        val result = mutableListOf<String>()

        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val idCaso = rs.getInt("casos")
                    result.add("Caso de Cyberbullying: $idCaso")
                }
            }
        }
        return result
    }
}

