package pt.isel.cli.controller

import pt.isel.core.service.DatabaseClient

/**
 * Controller for the queries and reports menu - routes to parameter queries or global reports.
 *
 * This is a navigation menu that doesn't execute queries directly, but delegates to specialized sub-menus.
 *
 * @param option User's menu selection (1 = Parameter Queries, 2 = Global Reports, 0 = Return to main menu)
 * @param dataBase Database client (passed through to sub-controllers)
 * @return Next menu to display, or navigates back to MAIN_MENU
 */
fun queriesAndReportsMenuController(option: Int?, dataBase: DatabaseClient): MenuType? {
    dataBase.interacoes // to suppress unused parameter warning
    return when (option) {
        1 -> MenuType.PARAMETER_QUERIES_MENU
        2 -> MenuType.GLOBAL_REPORTS_MENU
        0 -> MenuType.MAIN_MENU
        else -> {
            println("Opção inválida.")
            MenuType.QUERIES_AND_REPORTS_MENU
        }
    }
}