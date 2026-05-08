package serie2.problema

import serie2.problema.imp2.CoreImp2

// ANSI Colors
const val RESET = "\u001B[0m"
const val BLUE = "\u001B[34m"
const val GREEN = "\u001B[32m"
const val RED = "\u001B[31m"
const val YELLOW = "\u001B[33m"
const val CYAN = "\u001B[36m"
const val GRAY = "\u001B[90m"
const val ORANGE = "\u001B[38;5;214m"

const val version = "1.0"

/**
 * Define o código para os comentários.
*/
const val commentCode = "//"

/**
Guarda globalmente o núcleo que está a ser utilizado.
*/
var core: CoreInterface = CoreImp2()

/**Guarda globalmente o caminho base que está a ser utilizado*/
var root = System.getProperty("user.dir") + '\\'
    get() = field.dropLastWhile { it == '\\' || it == '/' } + '\\'