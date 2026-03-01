package pt.isel.cli.controller

import pt.isel.cli.ui.menus.*
import pt.isel.core.service.DatabaseClient

/**
 * Enumeration of available menu types in the CLI application.
 *
 * Each menu type associates a print function (to display the menu) with a controller function
 * (to handle user input and navigation). This design pattern allows for clean separation
 * between UI presentation and business logic.
 *
 * ## Menu Hierarchy
 * ```
 * MAIN_MENU
 * ├── DATA_MANAGEMENT_MENU (insert interactions)
 * └── QUERIES_AND_REPORTS_MENU
 *     ├── PARAMETER_QUERIES_MENU (queries with user input)
 *     └── GLOBAL_REPORTS_MENU (statistical reports)
 * ```
 *
 * @property print Function that displays the menu to the user
 * @property controller Function that processes user input and returns the next menu (or null to exit)
 */
enum class MenuType(
    val print: () -> Unit,
    val controller: (Int?, DatabaseClient) -> MenuType?
) {
    /** Main menu - entry point of the application */
    MAIN_MENU(
        ::printMainMenu,
        ::mainMenuController
    ),

    /** Data management menu - for inserting and modifying data */
    DATA_MANAGEMENT_MENU(
        ::printDataManagementMenu,
        ::dataManagementMenuController
    ),

    /** Queries and reports parent menu - navigation to query types */
    QUERIES_AND_REPORTS_MENU(
        ::printQueriesAndReportsMenu,
        ::queriesAndReportsMenuController
    ),

    /** Parameter queries menu - queries that require user input */
    PARAMETER_QUERIES_MENU(
        ::printParameterQueriesMenu,
        ::parameterQueriesMenuController
    ),

    /** Global reports menu - statistical and aggregated reports */
    GLOBAL_REPORTS_MENU(
        ::printGlobalReportsMenu,
        ::globalReportsMenuController
    );
}

