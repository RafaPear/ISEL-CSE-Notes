package serie2.problema

import java.io.File
import kotlin.concurrent.thread
import kotlin.random.Random

/**
 * Função que processa os comandos introduzidos pelo utilizador. Retorna true caso a execução tenha sido bem sucedida.
 * False caso contrário.
 *
 * @param input A string de entrada que contém os comandos a serem processados.
 * @return true se a execução foi bem sucedida, false caso contrário.
 * */
fun cmdParser(input: String?): Boolean {
    if (input == null || input.isBlank()) return true

    val tokens = input.trim().split('|').map { it.trim().split(Regex("\\s+")) }

    var good = true

    for (token in tokens){
        if (!good) {
            println("${RED}App Error: Previous command failed. Aborting.$RESET")
            return false
        }
        var command: Command? = null
        Commands.entries.forEach { it ->
            if (it.aliases.contains(token[0].lowercase())) {
                command = it
            }
        }
        if (command == null) {

            println("${RED}App Error: Unknown command ${token[0]}$RESET")
            return false
        }
        good = command.run(token.drop(1)) // Pass the arguments to the command
    }
    return good
}

/**
 * Função que imprime uma árvore de diretórios plana a partir de um diretório especificado.
 * A função lista os arquivos e subdiretórios dentro do diretório, ordenando-os por nome.
 *
 * @param dir O diretório a ser listado.
 */
fun printFlatDirectoryTree(dir: File) {
    if (!dir.exists()) return
    val children = dir.listFiles()?.sortedBy { it.name.lowercase() } ?: return
    for (file in children) {
        if (file.isDirectory) {
            println("└── ${BLUE}${file.name}/$RESET")
        } else {
            println("└── ${GREEN}${file.name}$RESET")
        }
    }
}

/**
 * Função que imprime uma mensagem de boas-vindas e instruções para o utilizador.
 * A função exibe uma mensagem de boas-vindas e sugere que o utilizador digite 'help' para obter uma lista de comandos disponíveis.
 */
fun drawPrompt() {
    println("${CYAN}App: Welcome to the Point Processor CLI!$RESET")
    println("${CYAN}App: Type 'help' for a list of commands.$RESET")
}

/**
 * Função que limpa o ecrã e redesenha o prompt.
 * A função imprime 50 linhas em branco para limpar o ecrã e, em seguida, chama a função drawPrompt() para exibir o prompt novamente.
 */
fun clearAndRedrawPrompt() {
    repeat(50) { println() }
    drawPrompt()
}

/**
 * Função que limpa o ecrã sem redesenhar o prompt.
 * A função imprime 50 linhas em branco para limpar o ecrã.
 */
fun clearPrompt() {
    repeat(50) { println() }
}

/**
 * Função que gera um arquivo de grafo aleatório com um número especificado de nós.
 * O arquivo é salvo no diretório especificado e contém as coordenadas dos nós gerados aleatoriamente.
 *
 * @param fileName O nome do arquivo a ser gerado.
 * @param numNodes O número de nós a serem gerados.
 * @param xRange O intervalo para as coordenadas x dos nós (padrão: -90000000 a -86000000).
 * @param yRange O intervalo para as coordenadas y dos nós (padrão: 32400000 a 32800000).
 */
fun generateRandomGraphFile(
    fileName: String,
    numNodes: Int,
    xRange: IntRange = -90000000..-86000000,
    yRange: IntRange = 32400000..32800000
) {
    val file = File(fileName)

    // Certificar que o diretório existe
    file.parentFile?.mkdirs()

    file.printWriter().use { out ->
        // Header
        out.println("c Generated test file for $fileName")
        out.println("c")
        out.println("c graph contains $numNodes nodes")
        out.println("c")

        // Generate random nodes
        thread {
            for (i in 1..numNodes/4) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join()

        thread {
            for (i in numNodes/4 + 1..numNodes/2) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join()

        thread {
            for (i in numNodes/2 + 1..numNodes*3/4) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join()

        thread {
            for (i in numNodes*3/4 + 1..numNodes) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join()

        // Generate random nodes
        thread {
            for (i in 1..numNodes/4) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join( )

        thread {
            for (i in numNodes/4 + 1..numNodes/2) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join( )

        thread {
            for (i in numNodes/2 + 1..numNodes*3/4) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join( )

        thread {
            for (i in numNodes*3/4 + 1..numNodes) {
                val x = Random.nextInt(xRange.first, xRange.last + 1)
                val y = Random.nextInt(yRange.first, yRange.last + 1)
                out.println("v $i $x $y")
            }
        }.join( )

    }
}
