package pt.isel.cli.controller

import pt.isel.core.service.DatabaseClient

/**
 * Controller for the main menu - handles navigation to primary sub-menus.
 *
 * Routes the user to either data management or queries/reports based on their selection.
 *
 * @param option User's menu selection (1 = Data Management, 2 = Queries/Reports, 0 = Exit)
 * @param dataBase Database client (not used in main menu but required for consistency)
 * @return Next menu to display, or null to exit the application
 */
fun mainMenuController(option: Int?, dataBase: DatabaseClient): MenuType? {
    dataBase.interacoes // to suppress unused parameter warning
    return when (option) {
        1 -> MenuType.DATA_MANAGEMENT_MENU
        2 -> MenuType.QUERIES_AND_REPORTS_MENU
        0 -> null
        else -> {
            println("Opção inválida.")
            MenuType.MAIN_MENU
        }
    }
}

