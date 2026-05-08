package serie2.problema

/**
 * Representa um comando que pode ser executado na ‘interface’ de linha de comandos (CLI).
 *
 * Esta ‘interface’ define a estrutura que todos os comandos devem seguir, incluindo:
 * - A descrição textual do comando, visível no menu de ajuda.
 * - A forma de utilização do comando e os seus atalhos (aliases).
 * - O número mínimo e máximo de argumentos aceites.
 * - Se o comando espera ficheiros como entrada e qual a extensão esperada.
 *
 * A função [run] é chamada sempre que o comando é executado no CLI, recebendo os argumentos como uma lista de strings
 * e devolvendo um valor booleano que indica o sucesso ou falha da execução.
 */

interface Command {
    /** Define a descrição do comando que será apresentada na última
     coluna do menu Help*/
    val description: String

    /** Define a descrição longa do comando que será apresentada quando
     * o comando help é chamado com argumentos*/
    val longDescription: String
        get() = description

    /**Define o modo de utilização do comando que será apresentado
    na primeira coluna do menu Help
    */val usage: String

    /**Define as diferentes formas de chamamento do comando que serão
    apresentadas na segunda coluna do menu Help*/
    val aliases: List<String>

    /**Define o número mínimo de argumentos.

    DEFAULT = 0*/
    val minArgs: Int
        get() = 0

    /**Define o número máximo de argumentos.

    DEFAULT = 0*/
    val maxArgs: Int
        get() = 0

    /**Define se o comando espera receber ficheiros.

    DEFAULT = false*/
    val requiresFile: Boolean
        get() = false

    /**Define se a extensão de ficheiro que o comando espera receber.

    DEFAULT = ""*/
    val fileExtension: String
        get() = ""

    /**Função run é executada quando o comando é chamado no CLI.
    Recebe como parâmetros os argumentos [args] do comando e retorna um
    [Boolean] que indica se a execução do comando foi bem sucedida ou não.*/
    fun run(args: List<String>): Boolean
}