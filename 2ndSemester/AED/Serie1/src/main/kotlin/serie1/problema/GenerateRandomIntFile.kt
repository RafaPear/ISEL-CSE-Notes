package serie1.problema

fun generateRandomIntFile(fileName: String, size: Int, max: Int) {
    val writer = createWriter(fileName)
    for (i in 0 until size) {
        writer.println((Math.random() * max).toInt())
    }
    writer.close()
}


/** Usage Example
 *  File "ints.txt" must be on the project Directory.
 *  This example shows how to generate a file containing random integers
 */
fun main() {
    val SIZE = 100000
    val MAX = 100000
    val pw = createWriter("testFolder/ints.txt")

    pw.use { f ->
        for (i in 0..< SIZE) {
            f.println(((Math.random() * MAX).toInt()))
        }
    }
    // Close file
    pw.close()
}
