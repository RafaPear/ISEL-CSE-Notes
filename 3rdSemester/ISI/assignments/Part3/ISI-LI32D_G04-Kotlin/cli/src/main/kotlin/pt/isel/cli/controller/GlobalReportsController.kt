package pt.isel.cli.controller

import pt.isel.cli.controller.util.printTable
import pt.isel.core.service.DatabaseClient

/**
 * Controller for global reports and statistics.
 *
 * Executes pre-defined queries that don't require user parameters and display
 * aggregated statistics and insights about the cyberbullying monitoring platform.
 *
 * @param option User's menu selection (1-7 for specific reports, 0 to return)
 * @param dataBase Database client for executing report queries
 * @return The current menu (stays in GLOBAL_REPORTS_MENU) or navigates back
 */
fun globalReportsMenuController(option: Int?, dataBase: DatabaseClient): MenuType {
    /**
     * Helper function to execute a report query and display results.
     *
     * @param title Report title to display
     * @param reportFunc Lambda that executes the query and returns formatted results
     */
    fun executeReport(title: String, reportFunc: () -> List<String>) {
        println("\n--- $title ---")
        printTable(reportFunc())
    }

    when (option) {
        1 -> executeReport(
            title = "1. Todas as Interações e Sinalização",
        ) { dataBase.interacoes.getInteracoes() }

        2 -> executeReport(
            title = "2. Total de Interações Abusivas vs Não Abusivas"
        ) {dataBase.relatorios.getTotalAbusiveAndNoAbusive()}

        3 -> executeReport(
            title = "3. Casos e Média de Gravidade por Interação"
        ) {dataBase.relatorios.getCasosByInteracaoWithAvgGravidade()}

        4 -> executeReport(
            title = "4. Relatório de Casos por Psicólogo"
        ) {dataBase.relatorios.getRelatorioPsicologos()}

        5 -> executeReport(
            title = "5. Relatório de Casos por Área de Especialização"
        ) {dataBase.relatorios.getRelatorioCasosByAreaDeAtuacao()}

        6 -> executeReport(
            title = "6. Utilizadores que Consultaram Todos os Recursos"
        ) {dataBase.utilizadores.getUtilizadoresConsultaramTodosRecursos()}

        7 -> executeReport(
            title = "7. Casos com Gravidade Superior à Média Global"
        ) {dataBase.casos.getCasosAboveAvgGravidade()}

        0 -> return MenuType.QUERIES_AND_REPORTS_MENU

        else -> println("Opção inválida.")
    }
    return MenuType.GLOBAL_REPORTS_MENU
}

