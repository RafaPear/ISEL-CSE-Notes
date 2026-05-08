
import Statistics.MAX_ID
import java.io.File


/**
 * Função que lê um ficheiro e devolve uma lista de strings
 *
 * @param path caminho do ficheiro
 * @return [List] lista de strings
 * */
fun readFile(path: String): List<String>{
    return File(path).readLines()
}

fun isSimul(): Boolean {
    try {
        val file = File("USB_PORT.properties")
        for (p in file.readLines()){
            if (p.contains("simulation") && !p.contains('#')) {
                return p.split("=")[1].trim().toBoolean()
            }
        }
    }
    catch (e: Exception){
        return false
    }
    return false
}

fun capInside(value: Int, min: Int, max: Int): Int {
    val range = max - min + 1
    return Math.floorMod(value - min, range) + min
}

fun parsePins(pins: String): Int {
    val values = pins.split('-').map { it.trim().toInt() }
    val start = values.first()
    val end = values.getOrElse(1) { start }
    return (start..end).fold(0) { acc, i -> acc or pow(2, i) }
}

fun getInputPins(line: String): Int {
    val badPins = line.split("->")[1]
        .trim()
        .removePrefix("UsbPort.I")
        .removeSurrounding("[", "]")
    return parsePins(badPins)
}

fun getOutputPins(line: String): Int {
    val badPins = line.split("->")[0]
        .trim()
        .removePrefix("UsbPort.O")
        .removeSurrounding("[", "]")
    return parsePins(badPins)
}

/** Devolve a string com número par de caracteres, juntando um espaço se necessário. */
private fun String.even(): String =
    if (length.isEven()) this else "$this "

private fun Int.isEven() = this and 1 == 0

fun Char.toHexInt(): Int {
    return when (this) {
        in '0'..'9' -> this.digitToInt()
        in 'A'..'F' -> this.code - 'A'.code + 10
        else -> error("Invalid character for hex conversion")
    }
}

fun Int.toCharId(): Char{
    return when (this) {
        in 0..9 -> this.digitToChar()
        in 10..MAX_ID -> ('A' + (this - 10))
        else -> error("Invalid integer for char conversion")
    }
}