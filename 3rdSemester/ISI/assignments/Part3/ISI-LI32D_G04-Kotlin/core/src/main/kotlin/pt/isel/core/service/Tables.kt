package pt.isel.core.service

/**
 * Types of user interactions that can be monitored.
 * Maps to the `tipo` column in the INTERACAO table.
 *
 * @property valorSql The SQL string value stored in the database
 */
enum class TipoInteracao(val valorSql: String) {
    /** Direct message between users */
    MENSAGEM("Mensagem"),

    /** Comment on a post or content */
    COMENTARIO("Comentário"),

    /** Reply to another interaction */
    RESPOSTA("Resposta");
}

/**
 * Represents a user interaction that may need monitoring.
 *
 * @property idInteracao Unique identifier for the interaction (auto-generated if null)
 * @property idUtilizador ID of the user who created this interaction
 * @property data Date of the interaction in "YYYY-MM-DD" format
 * @property tipo Type of interaction (message, comment, or reply)
 * @property texto Optional text content of the interaction
 */
data class Interacao(
    val idInteracao: Int? = null,
    val idUtilizador: Int,
    val data: String,
    val tipo: TipoInteracao,
    val texto: String? = null
)