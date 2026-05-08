package serie2.problema

import serie2.problema.imp1.CoreImp1
import serie2.problema.imp2.CoreImp2
import java.io.File
import kotlin.system.exitProcess
import kotlin.time.measureTime

// Só para que fique claro ao professor: esta lógica foi pensada por nós, do zero.
// O ChatGPT apenas está a ajudar a rever os comentários. Obrigado :)

// !! OBRIGATÓRIOS !!
// Os campos `description`, `usage`, `aliases` e a função `run` são de implementação obrigatória.
// Todos os outros campos são opcionais.

// !! EXPLICAÇÃO DAS PROPRIEDADES !!
//
// - aliases: contém os diferentes nomes que podem ser usados para invocar o comando principal.
//
// - Args (min e max): definem o número mínimo e máximo de argumentos que o comando aceita.
//   Nota: se `maxArgs` for definido como −1, o comando aceita um número ilimitado de argumentos.
//   O mínimo tem obrigatoriamente de ser menor ou igual ao máximo. Se forem diferentes, isso
//   indica que o comando tem argumentos opcionais.
//   Exemplo: se `minArgs = 1` e `maxArgs = 2`, o primeiro argumento é obrigatório e o segundo é opcional.
//
//   Se `minArgs` e `maxArgs` não forem definidos, assume-se que o comando não recebe argumentos
//   (`minArgs = 0`, `maxArgs = 0`).
//
// - requiresFile e fileExtension: indicam se o comando espera caminhos de ficheiros como argumentos.
//   Se `requiresFile = true`, será feita a validação da extensão dos ficheiros através de `fileExtension`.
//   Por defeito, considera-se `requiresFile = false` e `fileExtension = ""`.
//
// - run: função principal do comando. Deve ser sobreposta para definir o comportamento.
//   Por convenção (definida por nós), recomenda-se:
//     1. Validar os argumentos com `validateArgs` no início da função;
//     2. Usar `println` apenas para mensagens relevantes (como erros), e não para texto aleatório.
//        A ideia é que o `core` trate da lógica principal.

/**
 * Enum que representa todos os comandos disponíveis na aplicação CLI.
 *
 * Cada constante implementa a interface [Command] e define:
 * - Uma descrição (`description`);
 * - Como deve ser usado (`usage`);
 * - Os seus aliases (nomes alternativos);
 * - Número de argumentos esperados;
 * - Se deve ou não aceitar ficheiros;
 * - E a lógica que executa (`run`).
 *
 * A função `validateArgs` valida a quantidade de argumentos e, se necessário, a extensão dos ficheiros.
 */


enum class Commands : Command {
    /**
     * Comando `LOAD`
     *
     * Carrega dois ficheiros `.co` (coleções) para serem usados pelas operações de conjunto.
     * Os ficheiros devem existir e ter a extensão `.co`.
     *
     * Uso: `LOAD <ficheiro1.co> <ficheiro2.co>`
     *
     * Este comando deve ser executado antes de usar comandos como `UNION`, `DIFFERENCE`, etc.
     * Substitui quaisquer ficheiros previamente carregados.
     */
    LOAD {
        override val description = "Load a file"
        override val longDescription = "Load two files to be used in set operations like union, intersection, and difference."
        override val usage = "load <file1.co> <file2.co>"
        override val aliases = listOf("l", "load")
        override val minArgs = 2
        override val maxArgs = 2
        override val requiresFile = true

        override val fileExtension = ".co"

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                core.load(root + args[0], root + args[1])
            }
            catch (e: Exception) {
                println("${RED}App Error: Failed to load files. ${e.message}$RESET")
                return false
            }
            return true
        }
    },

    /**
     * Comando `UNION`
     *
     * Realiza a união entre os dois ficheiros `.co` carregados previamente com o comando `LOAD`.
     * Mostra o resultado no terminal.
     *
     * Uso: `UNION`
     *
     * Este comando não aceita argumentos. Gera erro se não existirem ficheiros carregados.
     */
    UNION {
        override val description = "Union of two files"
        override val longDescription = "Perform union operation on two loaded files."
        override val usage = "union <outfile.co>"
        override val aliases = listOf("u", "union")
        override val minArgs = 1
        override val maxArgs = 1
        override val requiresFile = true
        override val fileExtension = ".co"

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                core.union(root + args[0])
            } catch (e: Exception) {
                println("${RED}App Error: Failed to perform union. ${e.message}$RESET")
                return false
            }
            return true
        }
    },

    /**
     * Comando `INTERSECTION`
     *
     * Mostra a interseção entre os dois ficheiros `.co` carregados.
     * Ou seja, os elementos que estão presentes em ambos os ficheiros.
     *
     * Uso: `INTERSECTION`
     *
     * Não aceita argumentos. Exige que os ficheiros estejam carregados previamente.
     */
    INTERSECTION {
        override val description = "Intersection of two files"
        override val longDescription = "Perform intersection operation on two loaded files."
        override val usage = "intersection <outfile.co>"
        override val aliases = listOf("i", "intersection")
        override val minArgs = 1
        override val maxArgs = 1
        override val requiresFile = true
        override val fileExtension = ".co"

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                core.intersection(root + args[0])
            } catch (e: Exception) {
                println("${RED}App Error: Failed to perform intersection. ${e.message}$RESET")
                return false
            }
            return true
        }
    },

    /**
     * Comando `DIFFERENCE`
     *
     * Mostra a diferença entre os dois ficheiros `.co` carregados (ficheiro 1 - ficheiro 2).
     * Exibe os elementos que existem apenas no primeiro ficheiro.
     *
     * Uso: `DIFFERENCE`
     *
     * Não recebe argumentos. Os ficheiros devem ser carregados com o comando `LOAD`.
     */
    DIFFERENCE {
        override val description = "Difference of two files"
        override val longDescription = "Perform difference operation on two loaded files."
        override val usage = "difference <outfile.co>"
        override val aliases = listOf("d", "difference")
        override val minArgs = 1
        override val maxArgs = 1
        override val requiresFile = true
        override val fileExtension = ".co"

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                core.difference(root + args[0])
            } catch (e: Exception) {
                println("${RED}App Error: Failed to perform difference. ${e.message}$RESET")
                return false
            }
            return true
        }
    },

    /**
     * Comando `EXIT`
     *
     * Encerra a aplicação CLI.
     *
     * Uso: `EXIT`
     *
     * Não recebe argumentos.
     */
    EXIT {
        override val description = "Exit the application"
        override val usage = "exit"
        override val aliases = listOf("e", "exit")

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            println("${YELLOW}Exiting...$RESET")
            exitProcess(0)
        }
    },

    /**
     * Comando `HELP`
     *
     * Mostra todos os comandos disponíveis na aplicação, bem como a sua descrição e modo de uso.
     *
     * Uso: `HELP`
     *
     * Não necessita de argumentos.
     */
    HELP {
        override val description = "Show help information"
        override val longDescription = "Show all available commands and their usage."
        override val usage = "help [command]"
        override val aliases = listOf("h", "help")
        override val minArgs = 0
        override val maxArgs = 1

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            if (args.isNotEmpty()) {
                entries.forEach { cmd ->
                    if (args[0].lowercase() in cmd.aliases) {
                        // Print help but description is long description and formats in a better readable way
                        println("${CYAN}${cmd.usage}$RESET - ${YELLOW}Alias: ${cmd.aliases.joinToString(", ")}$RESET")
                        println("${YELLOW}${cmd.longDescription}$RESET")

                        return true
                    }
                }
                println("${RED}App Error: Unknown command ${args[0]}$RESET")
            }
            else {
                println("${YELLOW}Arguments with [] are optional and arguments with <> are required.$RESET")
                println("${CYAN}Available commands:$RESET")
                entries.forEach { cmd ->
                    println("${CYAN}${cmd.usage}$RESET - ${YELLOW}Alias: ${cmd.aliases.joinToString(", ")}$RESET - ${cmd.description}")
                }
            }
            return true
        }
    },
    /**
     * Lista os ficheiros e pastas do diretório atual ou de um diretório relativo passado como argumento.
     */
    LS {
        override val description = "List files in the current directory"
        override val longDescription = "List files in the current directory or a specified relative directory."
        override val usage = "ls [directory]"
        override val aliases = listOf("ls")
        override val minArgs = 0
        override val maxArgs = 1

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            val path = if (args.isNotEmpty()) "$root${args[0]}" else root
            val target = File(path)
            if (!target.exists() || !target.isDirectory) {
                println("${RED}App Error: Directory does not exist or is invalid: $path$RESET")
                return false
            }
            println("${GREEN}Files in ${path.ifEmpty { "current" }} directory:$RESET")
            printFlatDirectoryTree(target)
            return true
        }
    },

    /**
     * Muda o diretório atual para outro especificado ou para o diretório anterior com "..".
     */
    CD {
        override val description = "Change directory"
        override val longDescription = "Change the current directory to a specified relative directory or to the parent directory with '..'."
        override val usage = "cd <directory>"
        override val aliases = listOf("cd")
        override val minArgs = 1
        override val maxArgs = 1
        override val requiresFile = false
        override val fileExtension = ""

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                val path = args[0]
                if (path == "..") {
                    root = root.dropLast(1).dropLastWhile { it != '/' && it != '\\' }
                } else if (File("$root$path").isDirectory()) {
                    root += "$path\\"
                } else {
                    println("${RED}App Error: Path does not exist or is invalid: $path$RESET")
                    return false
                }
            } catch (e: Exception) {
                println("${RED}App Error: Failed to change directory. ${e.message}$RESET")
                return false
            }
            return true
        }
    },

    /**
     * Seleciona o núcleo de processamento a usar (CoreImp1 ou CoreImp2).
     */
    CORE {
        override val description = "Select core library"
        override val longDescription = "Select the core library to use (CoreImp1 or CoreImp2)."
        override val usage = "core <0|1>"
        override val aliases = listOf("core")
        override val minArgs = 0
        override val maxArgs = 1
        override val requiresFile = false
        override val fileExtension = ""

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                when (args[0].toInt()) {
                    0 -> core = CoreImp1()
                    1 -> core = CoreImp2()
                    else -> println("${RED}App Error: Invalid core library selected.$RESET")
                }
                printMenu()
            } catch (_: NumberFormatException) {
                println("${RED}App Error: Invalid core library selected.$RESET")
                return false
            } catch (_: Exception) {
                println("${YELLOW}Available core libraries:$RESET")
                printMenu()
            }
            return true
        }

        private fun printMenu() {
            if (core.javaClass.simpleName == CoreImp1().javaClass.simpleName) {
                println("  0 - CoreImp1 ${GREEN}(selected)$RESET")
                println("  1 - CoreImp2")
            } else {
                println("  0 - CoreImp1")
                println("  1 - CoreImp2 ${GREEN}(selected)$RESET")
            }
        }
    },

    /**
     * Limpa o ecrã do terminal imprimindo várias linhas vazias.
     */
    CLR {
        override val description = "Clear the screen"
        override val longDescription = "Clear the screen by printing multiple empty lines."
        override val usage = "clr"
        override val aliases = listOf("clr")
        override val minArgs = 0
        override val maxArgs = 0
        override val requiresFile = false
        override val fileExtension = ""

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            clearPrompt()
            return true
        }
    },

    /**
     * Imprime no terminal todos os argumentos passados ao comando.
     */
    PRINT {
        override val description = "Print argument"
        override val longDescription = "Print the provided arguments to the terminal."
        override val usage = "print <words>"
        override val aliases = listOf("print")
        override val minArgs = 0
        override val maxArgs = -1
        override val requiresFile = false
        override val fileExtension = ""

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            println("${GREEN}${args.joinToString(" ")}$RESET")
            return true
        }
    },

    /**
     * Imprime o conteúdo de um ficheiro linha a linha.
     */
    PRTFILE {
        override val description = "Print file content"
        override val longDescription = "Print the content of a file line by line (!!Caution!!)."
        override val usage = "prtfile <file>"
        override val aliases = listOf("prtfile", "prtf")
        override val minArgs = 1
        override val maxArgs = 1
        override val requiresFile = true
        override val fileExtension = ".co"

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                // Do a nice print
                File(root + args[0]).forEachLine { line ->
                    println("${YELLOW}$line$RESET")
                }
            } catch (e: Exception) {
                println("${RED}App Error: Failed to read file. ${e.message}$RESET")
                return false
            }
            return true
        }
    },
    /**
     * Mostra a versão da aplicação e os créditos dos autores.
     */
    VERSION{
        override val description = "Show version information and credits"
        override val usage = "version"
        override val aliases = listOf("version")

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false

            drawBox(
                """
                    AED 2025 - Version $version
                    Developed by: Ian Frunze, Rafael Pereira and Gustavo Costa
                    For the ambit of: AED, Algorithms and Data Structures
                """.trimIndent())

            // AED em grande e colorido
            println()
            println("${RED}    █████╗   ███████╗ ██████╗ ${RESET}")    // A em vermelho
            println("${RED}   ██╔══██╗  ██╔════╝ ██╔═══██╗${RESET}")
            println("${RED}   ███████║  █████╗   ██║   ██║${RESET}")
            println("${RED}   ██╔══██║  ██╔══╝   ██║   ██║${RESET}")
            println("${RED}   ██║  ██║  ███████╗ ╚██████╔╝${RESET}")
            println("${RED}   ╚═╝  ╚═╝  ╚══════╝  ╚═════╝ ${RESET}")
            println()
            return true
        }


        fun drawBox(
            text: String,
            borderColor: String = CYAN,
            textColor: String = YELLOW,
            resetColor: String = RESET
        ) {
            val lines = text.lines()
            val maxLength = lines.maxOf { it.length }
            val horizontalBorder = "═".repeat(maxLength + 2)  // +2 para os espaços à esquerda e direita

            println("$borderColor╔$horizontalBorder╗$resetColor")
            for (line in lines) {
                val padding = " ".repeat(maxLength - line.length + 1) // +1 espaço extra à direita
                println("$borderColor║ $resetColor$textColor$line$padding$resetColor$borderColor║$resetColor")
            }
            println("$borderColor╚$horizontalBorder╝$resetColor")
        }
    },
    /**
     * Comando `MEASURE`
     *
     * Mede o tempo de execução de um comando.
     *
     * Uso: `measure <command>`
     *
     * Aceita qualquer comando como argumento. Útil para testes de desempenho.
     */
    MEASURE {
        override val description = "Measure the time taken by a command"
        override val longDescription = "Measure the time taken to execute a command. Useful for performance testing."
        override val usage = "measure <command>"
        override val aliases = listOf("measure", "m", "time")
        override val minArgs = 1
        override val maxArgs = -1

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            val newArgs = args.joinToString(" ")

            println("${YELLOW}Measuring time for command: ${newArgs}$RESET")

            val time = measureTime { cmdParser(newArgs) }

            println("${GREEN}Time taken: ${time.inWholeMilliseconds} ms$RESET \n")
            return true
        }
    },
    /**
     * Comando `MAKETESTFILE`
     *
     * Cria um ficheiro de teste com pontos aleatórios.
     *
     * Uso: `maketestfile <filename> <number_of_points> [min_x] [max_x] [min_y] [max_y]`
     *
     * Aceita argumentos opcionais para definir os limites dos pontos gerados.
     */
    MAKETESTFILE {
        override val description = "Create a test file with random points"
        override val longDescription = "Create a test file with random points for testing purposes."
        override val usage = "maketestfile <filename> <number_of_points> [min_x] [max_x] [min_y] [max_y]"
        override val aliases = listOf("maketestfile", "mtf")
        override val minArgs = 2
        override val maxArgs = 6

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                val minX = if (args.size > 2) args[2].toInt() else -90000000
                val maxX = if (args.size > 3) args[3].toInt() else -86000000
                val minY = if (args.size > 4) args[4].toInt() else 32400000
                val maxY = if (args.size > 5) args[5].toInt() else 32800000
                generateRandomGraphFile(root + args[0], args[1].toInt(), minX..maxX, minY..maxY)
            } catch (e: Exception) {
                println("${RED}App Error: Failed to create test file. ${e.message}$RESET")
                return false
            }
            return true
        }
    },
    /**
     * Comando `LOADSCRIPT`
     *
     * Carrega e executa um ficheiro de script com uma série de comandos.
     *
     * Uso: `loadscript <script_file>`
     *
     * Aceita um argumento que é o nome do ficheiro de script a ser carregado.
     */
    LOADSCRIPT {
        override val description = "Load a script file"
        override val longDescription = "Load and execute a script file containing a series of commands."
        override val usage = "loadscript <script_file>"
        override val aliases = listOf("loadscript", "lscript")
        override val minArgs = 1
        override val maxArgs = 1
        override val requiresFile = true
        override val fileExtension = ".ppc"

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            val prevRoot = root
            println("${YELLOW}Loading script: ${args[0]}$RESET")
            try {
                val file = File(root + args[0])
                val lines = file.readLines()
                root = System.getProperty("user.dir") + "\\"
                for (line in lines) {
                    if (!line.trim().startsWith(commentCode)) {
                        if (!cmdParser(line)) {
                            println("${RED}Script Error: Failed to execute command in script: $BLUE$line$RESET")
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                println("${RED}App Error: Failed to load script. ${e.message}$RESET")
                return false
            }
            root = prevRoot
            return true
        }
    },
    /**
     * Comando `WAIT`
     *
     * Faz uma pausa na execução do programa por um determinado número de milissegundos.
     *
     * Uso: `wait <milliseconds>`
     *
     * Aceita um argumento que é o número de milissegundos a esperar.
     */
    WAIT {
        override val description = "Wait for a specified time"
        override val longDescription = "Wait for a specified time in milliseconds."
        override val usage = "wait <milliseconds>"
        override val aliases = listOf("wait", "w")
        override val minArgs = 1
        override val maxArgs = 1

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            try {
                Thread.sleep(args[0].toLong())
            } catch (e: Exception) {
                println("${RED}App Error: Failed to wait. ${e.message}$RESET")
                return false
            }
            return true
        }
    },
    /**
     * Comando `WAITFORUSER`
     *
     * Faz uma pausa na execução do programa até que o utilizador pressione Enter.
     *
     * Uso: `waitforuser [message]`
     *
     * Aceita um argumento opcional que é a mensagem a ser exibida antes de esperar pela entrada do utilizador.
     */
    WAITFORUSER {;
        override val description = "Wait for user input"
        override val longDescription = "Wait for user input before proceeding."
        override val usage = "wfu [message]"
        override val aliases = listOf("waitforuser", "wfu")
        override val minArgs = 0
        override val maxArgs = -1

        override fun run(args: List<String>): Boolean {
            if (!validateArgs(args, requiresFile)) return false
            if (args.isNotEmpty()) {
                println("${YELLOW}${args.joinToString(" ")}$RESET")
            } else {
                println("${YELLOW}Press Enter to continue...$RESET")
            }
            readLine()
            return true
        }
    };

    // Valida a quantidade de argumentos em função da definida no comando.
    // Se necessário, verifica se os argumentos contêm a extensão correta.
    // Retorna falso caso os argumentos sejam inválidos e verdadeiro caso
    // sejam válidos. Imprime também mensagens de erro com a respetiva causa.

    /**
     * Valida os argumentos passados para o comando. Valida também os ficheiros e a sua extensão.
     *
     * @param args Lista de argumentos passados para o comando.
     * @param requiresFile Indica se o comando requer ficheiros.
     * */
    fun validateArgs(args: List<String>, requiresFile: Boolean): Boolean {
        if (args.size < minArgs || (args.size > maxArgs && maxArgs != -1)) {
            if (minArgs == maxArgs) {
                println("${RED}App Error: Invalid number of arguments. Expected $minArgs, got ${args.size}$RESET")
            } else {
                println("${RED}App Error: Invalid number of arguments. Expected between $minArgs and $maxArgs, got ${args.size}$RESET")
            }
            return false
        }
        if (requiresFile && !args.all { it.endsWith(fileExtension) }) {
            println("${RED}App Error: Invalid file name. File names must end with $fileExtension$RESET")
            return false
        }
        return true
    }
}