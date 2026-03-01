package pt.isel.cli.controller.util

/**
 * Validates if a date string follows the YYYY-MM-DD format.
 *
 * Performs basic validation:
 * - Checks for 3 parts separated by "-"
 * - Validates year (4 digits), month (2 digits), day (2 digits)
 * - Validates month range (01-12) and day range (01-31)
 *
 * **Note**: Does not validate actual calendar validity (e.g., February 30th would pass).
 *
 * @param dataStr The date string to validate
 * @return true if format is valid, false otherwise
 */
fun checkDateFormat(dataStr: String): Boolean {
    val split = dataStr.split("-")
    if (split.size != 3) return false

    val (yearStr, monthStr, dayStr) = split

    if (yearStr.length != 4 || monthStr.length != 2 || dayStr.length != 2) return false

    if (monthStr !in "01".."12") return false

    if (dayStr !in "01".."31") return false

    return true
}

/**
 * Extension function that provides a "help" command for user input prompts.
 *
 * When the user types "help" (case-insensitive), executes the provided query function,
 * displays results in a table, and prompts the user to select a value.
 *
 * ## Usage Example
 * ```kotlin
 * val input = readlnOrNull()?.trim() ?: return
 * val id = input.toIntOrNull()
 *     ?: input.showHelp { database.users.getAll() }?.toIntOrNull()
 *     ?: return
 * ```
 *
 * @param query Lambda function that returns a list of formatted strings (with header row)
 * @return The user's selection as a string, or null if cancelled or no results
 */
fun String.showHelp(query: () -> List<String>): String? {
    if (this.lowercase() != "help") return null
    val results = query()
    if (results.isEmpty()) {
        println("Nenhum resultado encontrado.")
        return null
    } else {
        printTable(results, "Selecione o valor desejado: ", wait = false)
    }
    val response = readlnOrNull()?.trim()
    return if (response.isNullOrEmpty()) {
        println("❌Inválido. Operação cancelada.")
        null
    } else response
}

/**
 * Executes a query and displays results in a formatted table.
 *
 * If the query returns no results, displays a "no results" message.
 * Otherwise, formats and displays the results, then waits for user to press Enter.
 *
 * @param title Title to display before the results
 * @param queryFunc Lambda that executes the query and returns formatted results
 */
fun executeQuery(title: String, queryFunc: () -> List<String>) {
    println("\n--- $title ---")
    val results = queryFunc()
    if (results.drop(1).isEmpty()) {
        println("Nenhum resultado encontrado.")
        println("----------------------------\n")
        println("Pressione Enter para continuar...")
        readln()
    } else {
        printTable(results)
    }
}

/**
 * Formats and prints a table from a list of pipe-delimited strings.
 *
 * The first row is treated as the header. Each row is split by "|" and formatted
 * with proper column alignment based on the maximum width of each column.
 *
 * ## Input Format
 * ```kotlin
 * listOf(
 *     "ID | Name | Status",
 *     "1 | John | Active",
 *     "2 | Jane | Inactive"
 * )
 * ```
 *
 * ## Output
 * ```
 * ID | Name | Status
 * ---|------|--------
 * 1  | John | Active
 * 2  | Jane | Inactive
 * ```
 *
 * @param table List of pipe-delimited strings (first row is header)
 * @param msg Message to display after the table (default: prompt to continue)
 * @param wait Whether to wait for user input after displaying (default: true)
 */
fun printTable(table: List<String>, msg: String = "Pressione Enter para continuar...\n", wait: Boolean = true) {
    if (table.isEmpty()) return
    //Convert in a list of rows (list of list of strings)
    val rows = table.map { row -> row.split("|").map { it.trim() } }
    val colCount = rows[0].size

    // Calculate the maximum width for each column
    val colWidths = IntArray(colCount) { col ->
        rows.maxOf { row -> row.getOrNull(col)?.length ?: 0 }
    }

    // Function to format a row based on column widths
    fun formatRow(row: List<String>): String =
        // padEnd each cell to the column width and join with " | "
        row.mapIndexed { idx, cell -> cell.padEnd(colWidths[idx], ' ') }
            .joinToString(" | ")

    val header = formatRow(rows.first())
    val separator = colWidths.joinToString("-+-") { "-".repeat(it) }

    println(header)
    println(separator)
    rows.drop(1).forEach { println(formatRow(it)) }
    print(msg)
    if (wait) readln()
}