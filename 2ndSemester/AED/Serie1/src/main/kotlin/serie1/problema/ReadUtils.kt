package serie1.problema

import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter


fun createReader(fileName: String): BufferedReader {
    return BufferedReader(FileReader(fileName))
}

fun createWriter(fileName: String): PrintWriter {
    return PrintWriter(fileName)
}

/** Usage Example
 *  Files "input.txt" and "ouput.txt" are on the project Directory.
 *  This program copies the Input File to OutputFile.
 */
fun main() {
    val br = createReader("input.txt")
    val pw = createWriter("output.txt")
    var line: String?
    line = br.readLine()
    while (line != null) {
        pw.println(line)
        line = br.readLine()
    }
    // Close files
    br.close()
    pw.close()
}