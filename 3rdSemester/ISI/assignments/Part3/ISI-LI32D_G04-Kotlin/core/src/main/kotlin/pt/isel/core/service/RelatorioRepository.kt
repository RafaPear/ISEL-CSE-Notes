package pt.isel.core.service

import java.sql.Connection

/**
 * Repository responsible for generating global reports and statistical queries.
 *
 * Provides aggregated views and analytics across interactions, cases, psychologists,
 * and specialization areas. These methods support the reporting requirements of the
 * cyberbullying monitoring platform.
 *
 * @property connection Active JDBC connection to the database
 */
class RelatorioRepository(private val connection: Connection) {

    /**
     * Generates a summary of total abusive and non-abusive interactions.
     *
     * Counts interactions from the ANALISA table grouped by their flagging status.
     *
     * @return List with formatted counts for "Abusivas" and "Não Abusivas" interactions
     */
    fun getTotalAbusiveAndNoAbusive(): List<String> {
        val sql = """
            SELECT 
                abusiva as Tipo_Interacao, 
                COUNT(*) AS Total
            FROM ANALISE
            GROUP BY abusiva;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("Tipo de Interação | Total")

        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val tipo = rs.getString("Tipo_Interacao")
                    val total = rs.getInt("Total")
                    val tipoDescricao = if (tipo == "t") "Abusiva" else "Não Abusiva"
                    result.add(" $tipoDescricao | $total")
                }
            }
        }
        return result
    }

    /**
     * Generates a report showing case counts and average severity per interaction.
     *
     * Only includes interactions that have at least one associated case. The average
     * severity is calculated from psychologist evaluations (grauDeGravidade).
     *
     * @return List of formatted strings with interaction ID, total cases, and average severity
     */
    fun getCasosByInteracaoWithAvgGravidade(): List<String> {
        val sql = """
            SELECT 
                I.idInteracao as Interacao, 
                COUNT(C.idCaso) AS Total_Casos, 
                ROUND(AVG(Av.grauDeGravidade),2) AS Media_Gravidade
            FROM INTERACAO I
            JOIN ANALISE A ON I.idInteracao = A.idInteracao
            JOIN CRIA Cr ON A.idAnalise = Cr.idAnalise
            JOIN CASO_DE_CYBERBULLYING C ON Cr.idCaso = C.idCaso
            LEFT JOIN AVALIA Av ON C.idCaso = Av.idCasoDeCyberbullying
            GROUP BY I.idInteracao
            ORDER BY I.idInteracao;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("Interação | Total de Casos | Média de Gravidade")
        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val idInteracao = rs.getInt("Interacao")
                    val totalCasos = rs.getInt("Total_Casos")
                    val mediaGravidade = rs.getObject("Media_Gravidade")

                    result.add("$idInteracao | $totalCasos | $mediaGravidade")
                }
            }
        }

        return result
    }

    /**
     * Generates a comprehensive report for each psychologist showing:
     * - Total cases assigned
     * - Cases already analyzed (with severity rating)
     * - Average and maximum severity ratings given
     *
     * @return List of formatted strings with psychologist statistics
     */
    fun getRelatorioPsicologos(): List<String> {
        val sql = """
            SELECT 
                U.alcunha AS Psicologo,
                COUNT(Av.idCasoDeCyberbullying) AS Casos_Atribuidos,
                COUNT(CASE WHEN Av.graudegravidade IS NOT NULL THEN Av.idcasodecyberbullying ELSE NULL END) as Casos_analisados,
                ROUND(AVG(Av.grauDeGravidade),2) AS Media_Gravidade,
                MAX(Av.grauDeGravidade) AS Max_Gravidade
            FROM PSICOLOGO P
            JOIN UTILIZADOR U ON P.idUtilizador = U.idUtilizador
            LEFT JOIN AVALIA Av ON P.idUtilizador = Av.idUtilizador
            GROUP BY U.alcunha, P.idUtilizador;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("Psicólogo | Casos Atribuídos | Casos Analisados | Valor Médio de Gravidade | Máxima Gravidade")
        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val idPsicologo = rs.getString("psicologo")
                    val casosAtribuidos = rs.getInt("Casos_atribuidos")
                    val casosAnalisados = rs.getInt("Casos_analisados")
                    val valorMedioGravidade = rs.getObject("Media_Gravidade")
                    val maxGravidade = rs.getObject("Max_Gravidade")

                    result.add("$idPsicologo | $casosAtribuidos | $casosAnalisados | $valorMedioGravidade | $maxGravidade")
                }
            }
        }

        return result
    }

    /**
     * Generates a report grouped by specialization area (areaDeAtuacao) showing:
     * - Number of psychologists in that area
     * - Case counts by state (iniciado, avaliado, fechado)
     * - Average severity for cases in that area
     *
     * @return List of formatted strings with specialization area statistics
     */
    fun getRelatorioCasosByAreaDeAtuacao(): List<String> {
        val sql = """
            SELECT
                C.areaDeAtuacao AS Especializacao,
                (
                    SELECT COUNT(idUtilizador)
                    FROM PSICOLOGO
                    WHERE especializacao = C.areaDeAtuacao
                ) AS Num_Psicologos_Area,
            
                COUNT(CASE WHEN C.estado = 'iniciado' THEN 1 ELSE NULL END) AS Casos_Iniciados,
                COUNT(CASE WHEN C.estado = 'avaliado' THEN 1 ELSE NULL END) AS Casos_Avaliados,
                COUNT(CASE WHEN C.estado = 'fechado' THEN 1 ELSE NULL END) AS Casos_Fechados,
            
                ROUND(AVG(A.grauDeGravidade), 2) AS Media_Gravidade
            FROM
                CASO_DE_CYBERBULLYING C
            LEFT JOIN
                AVALIA A ON C.idCaso = A.idcasodecyberbullying
            GROUP BY
                C.areaDeAtuacao
            ORDER BY
                C.areaDeAtuacao;
        """.trimIndent()

        val result = mutableListOf<String>()
        result.add("Especialização | Nº Psicólogos na Área | Casos Iniciados | Casos Avaliados | Casos Fechados | Média de Gravidade")
        connection.prepareStatement(sql).use { ps ->
            ps.executeQuery().use { rs ->
                while (rs.next()) {
                    val especializacao = rs.getString("Especializacao")
                    val numPsicologosArea = rs.getInt("Num_Psicologos_Area")
                    val casosIniciados = rs.getInt("Casos_Iniciados")
                    val casosAvaliados = rs.getInt("Casos_Avaliados")
                    val casosFechados = rs.getInt("Casos_Fechados")
                    val mediaGravidade = rs.getObject("Media_Gravidade")

                    result.add("$especializacao | $numPsicologosArea | $casosIniciados | $casosAvaliados | $casosFechados | $mediaGravidade")
                }
            }
        }

        return result
    }
}

