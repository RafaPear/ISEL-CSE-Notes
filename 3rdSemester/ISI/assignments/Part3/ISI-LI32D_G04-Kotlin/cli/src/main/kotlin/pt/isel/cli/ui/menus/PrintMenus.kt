package pt.isel.cli.ui.menus

/**
 * Menu display functions for the CLI user interface.
 *
 * Each function prints a formatted menu with available options. These are pure UI functions
 * with no business logic - they only display information to the user.
 */

/**
 * Displays the main menu - the entry point of the application.
 */
fun printMainMenu() {
    println("\n==================================================")
    println("| PLATAFORMA DE MONITORIZAÇÃO DE CYBERBULLYING |")
    println("| (MODO ADMINISTRADOR)                             |")
    println("==================================================")
    println("[1] GESTÃO E INSERÇÃO DE DADOS")
    println("---")
    println("[2] CONSULTAS E RELATÓRIOS")
    println("---")
    println("[0] Sair do Programa")
    println("--------------------------------------------------")
    print("Introduza a opção: ")
}

/**
 * Displays the data management menu for inserting and modifying data.
 */
fun printDataManagementMenu() {
    println("\n==================================================")
    println("|        GESTÃO E INSERÇÃO DE DADOS              |")
    println("==================================================")
    println("[1] Reportar Nova Interação (Inserir Interação)")
    println("---")
    println("[0] Voltar ao Menu Principal")
    println("--------------------------------------------------")
    print("Introduza a opção: ")
}

/**
 * Displays the queries and reports menu - navigation to query types.
 */
fun printQueriesAndReportsMenu() {
    println("\n==================================================")
    println("|          CONSULTAS E RELATÓRIOS                |")
    println("==================================================")
    println("[1] Consultas por Parâmetro")
    println("[2] Relatórios Globais e Estatísticas")
    println("---")
    println("[0] Voltar ao Menu Principal")
    println("--------------------------------------------------")
    print("Introduza a opção: ")
}

/**
 * Displays the parameter queries menu - queries that require user input.
 */
fun printParameterQueriesMenu() {
    println("\n==================================================")
    println("|          CONSULTAS POR PARÂMETRO               |")
    println("==================================================")
    println("[1] Mostrar Interações de um Utilizador")
    println("[2] Mostrar Casos associados a uma Interação")
    println("[3] Mostrar toda a informação de um Caso")
    println("[4] Interações de um Utilizador com Casos Originados")
    println("[5] Psicólogos com intervenções complexas (X intervenções > Y gravidade)")
    println("---")
    println("[0] Voltar ao Menu Anterior")
    println("--------------------------------------------------")
    print("Introduza a opção: ")
}

/**
 * Displays the global reports menu - statistical and aggregated reports.
 */
fun printGlobalReportsMenu() {
    println("\n==================================================")
    println("|       RELATÓRIOS GLOBAIS E ESTATÍSTICAS       |")
    println("==================================================")
    println("[1] Mostrar todas as Interações e Sinalização")
    println("[2] Contagem de Interações Abusivas e Não Abusivas")
    println("[3] Casos e Média de Gravidade por Interação")
    println("[4] Casos por Psicólogo (Atribuídos, Analisados, Méd./Máx. Gravidade)")
    println("[5] Casos por Área de Especialização")
    println("[6] Utilizadores que Consultaram Todos os Recursos")
    println("[7] Casos com Gravidade Superior à Média Global")
    println("---")
    println("[0] Voltar ao Menu Anterior")
    println("--------------------------------------------------")
    print("Introduza a opção: ")
}

