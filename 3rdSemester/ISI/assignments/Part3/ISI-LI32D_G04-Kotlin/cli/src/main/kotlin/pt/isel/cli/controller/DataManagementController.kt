package pt.isel.cli.controller

import pt.isel.core.service.DatabaseClient
import pt.isel.core.service.Interacao
import pt.isel.core.service.TipoInteracao
import pt.isel.cli.controller.util.checkDateFormat
import pt.isel.cli.controller.util.showHelp

/**
 * Controller for data management and insertion operations.
 *
 * Currently handles interaction reporting (inserting new interactions into the database).
 *
 * @param option User's menu selection (1 = Insert Interaction, 0 = Return to main menu)
 * @param dataBase Database client for executing operations
 * @return The current menu (stays in DATA_MANAGEMENT_MENU) or navigates back to MAIN_MENU
 */
fun dataManagementMenuController(option: Int?, dataBase: DatabaseClient): MenuType {
    when (option) {
        1 -> insertNewInteracao(dataBase)
        0 -> return MenuType.MAIN_MENU
        else -> println("Opção inválida.")
    }
    return MenuType.DATA_MANAGEMENT_MENU
}

/**
 * Interactive function to insert a new interaction into the database.
 *
 * Prompts the user for:
 * - User ID (with "help" command to list available users)
 * - Interaction date (YYYY-MM-DD format)
 * - Optional text content
 * - Interaction type (message, comment, or reply)
 *
 * Validates all inputs and provides clear error messages on failure.
 *
 * @param dataBase Database client for user lookup and interaction insertion
 */
private fun insertNewInteracao(dataBase: DatabaseClient) {
    println("\n--- 1.1. Inserir Nova Interação  ---")

    println("ID do Utilizador que reporta a Interação (help para listar Utilizadores): ")

    val input = readlnOrNull()?.trim() ?: return
    val idUtilizadorInput = input.toIntOrNull()
        ?: input.showHelp { dataBase.utilizadores.getUtilizadores() }?.toIntOrNull()
        ?: return

    println("Data da Interação (YYYY-MM-DD): ")
    val data = readlnOrNull()
    if (data == null || !checkDateFormat(data)) {
        println("❌ Data inválida. Operação cancelada.")
        return
    }

    println("Texto da Interação (opcional): ")
    val texto = readlnOrNull()

    println("Tipos disponíveis: (1) MENSAGEM, (2) COMENTARIO, (3) RESPOSTA")
    println("Escolha o Tipo (1/2/3): ")
    val tipoInt = readlnOrNull()?.toIntOrNull()

    val tipo = when (tipoInt) {
        1 -> TipoInteracao.MENSAGEM
        2 -> TipoInteracao.COMENTARIO
        3 -> TipoInteracao.RESPOSTA
        else -> {
            println("❌ Tipo inválido. Operação cancelada.")
            return
        }
    }

    try {
        val novaInteracao = Interacao(
            idUtilizador = idUtilizadorInput,
            data = data,
            tipo = tipo,
            texto = texto
        )
        dataBase.interacoes.insertInteracao(novaInteracao)
        println("✅ Sucesso: Interação reportada e inserida.")
    } catch (e: Exception) {
        println("❌ Erro ao tentar inserir no DB: ${e.message}")
    }
}


