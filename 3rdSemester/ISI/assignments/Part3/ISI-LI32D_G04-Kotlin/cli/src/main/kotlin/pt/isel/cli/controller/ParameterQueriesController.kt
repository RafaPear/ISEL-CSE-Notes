package pt.isel.cli.controller

import pt.isel.cli.controller.util.executeQuery
import pt.isel.cli.controller.util.showHelp
import pt.isel.core.service.DatabaseClient

/**
 * Controller for parameter-based queries - queries that require user input.
 *
 * Handles queries that filter or search by specific IDs or criteria provided by the user.
 * Each query function prompts for parameters and executes the corresponding database query.
 *
 * @param option User's menu selection (1-5 for specific queries, 0 to return)
 * @param dataBase Database client for executing queries
 * @return The current menu (stays in PARAMETER_QUERIES_MENU) or navigates back
 */
fun parameterQueriesMenuController(option: Int?, dataBase: DatabaseClient): MenuType {

    when (option) {
        1 -> executeQuery1(dataBase)
        2 -> executeQuery2(dataBase)
        3 -> executeQuery3(dataBase)
        4 -> executeQuery4(dataBase)
        5 -> executeQuery5(dataBase)
        0 -> return MenuType.QUERIES_AND_REPORTS_MENU
        else -> println("Opção inválida.")
    }
    return MenuType.PARAMETER_QUERIES_MENU
}

/**
 * Query 1: Shows all interactions from a specific user.
 * Prompts for user ID with "help" command to list available users.
 *
 * @param dataBase Database client for user lookup and interaction query
 */
private fun executeQuery1(
    dataBase: DatabaseClient,
) {
    print("Al do Utilizador (help para listar Utilizadores): ")
    val input = readlnOrNull()?.trim() ?: return
    val id = input.toIntOrNull()
        ?: input.showHelp { dataBase.utilizadores.getUtilizadores() }?.toIntOrNull()
        ?: return

    executeQuery("1. Interações de Utilizador") { dataBase.interacoes.getInteracaoesThisUser(id) }
}

/**
 * Query 2: Shows all cases associated with a specific interaction.
 * Prompts for interaction ID with "help" command to list available interactions.
 *
 * @param dataBase Database client for interaction lookup and case query
 */
private fun executeQuery2(
    dataBase: DatabaseClient
) {
    print("ID da Interação (help para listar Interações): ")
    val input = readlnOrNull()?.trim() ?: return
    val id = input.toIntOrNull()
        ?: input.showHelp { dataBase.interacoes.getInteracoes() }?.toIntOrNull()
        ?: return

    executeQuery("2. Casos de uma Interação") { dataBase.casos.getCasosThisInteracao(id) }

}

/**
 * Query 3: Shows comprehensive information for a specific case.
 *
 * @param dataBase Database client
 */
private fun executeQuery3(
    dataBase: DatabaseClient
) {
    print("ID do Caso (help para listar os casos): ")
    val input = readlnOrNull()?.trim() ?: return
    val id = input.toIntOrNull()
        ?: input.showHelp { dataBase.casos.getCasos() }?.toIntOrNull()
        ?: return


    executeQuery("3. Caso por ID") { dataBase.casos.getCaso(id) }
}

/**
 * Query 4: Shows all interactions from a specific user along with associated cases.
 * Uses a LEFT JOIN to show interactions even if they have no cases.
 *
 * @param dataBase Database client for user lookup and combined query
 */
private fun executeQuery4(
    dataBase: DatabaseClient,
) {
    print("ID do Utilizador (help para listar os utilizadores): ")
    val input = readlnOrNull()?.trim() ?: return
    val id = input.toIntOrNull()
        ?: input.showHelp { dataBase.utilizadores.getUtilizadores() }?.toIntOrNull()
        ?: return


    executeQuery("4. Interações c/ Casos de um Utilizador") {
        dataBase.interacoes.getInteracoesThisUserWithCases(id)
    }
}

/**
 * Query 5: Finds psychologists with complex interventions.
 *
 * Prompts for two parameters:
 * - X: Minimum number of interventions
 * - Y: Minimum severity level
 *
 * Returns psychologists who made more than X interventions on cases with severity > Y.
 *
 * @param dataBase Database client for executing the filtered query
 */
private fun executeQuery5(dataBase: DatabaseClient) {
    print("Número MÍNIMO de intervenções (X): ")
    val x = readlnOrNull()?.toIntOrNull()
    print("Gravidade MÍNIMA (Y): ")
    val y = readlnOrNull()?.toIntOrNull()

    if (x == null || y == null) {
        println("❌ Entradas X ou Y inválidas.")
        return
    }

    executeQuery("\n--- 5. Psicólogos com Intervenções Complexas ---") {
        dataBase.utilizadores.getUtilizadoresWithConditions(y, x)
    }
}


