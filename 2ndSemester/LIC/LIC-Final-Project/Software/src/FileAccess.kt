
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object FileAccess {
    const val FILE_A_NAME = "info.txt"
    const val FILE_B_NAME = "statistics.txt"

    lateinit var fileA : File
    lateinit var fileB : File

    lateinit var fileALines : List<String>
    lateinit var fileBLines : List<String>

    var fileAbuffer = ""
    var fileBbuffer = ""

    fun init() {
        fileA = File(FILE_A_NAME)
        fileB = File(FILE_B_NAME)

        if (!fileA.exists()) {
            Logger.getLogger("File").log(Level.WARNING, "File not found: $FILE_A_NAME, creating it")
            fileA.createNewFile()
        }

        if (!fileB.exists()) {
            Logger.getLogger("File").log(Level.WARNING, "File not found: $FILE_B_NAME, creating it")
            fileB.createNewFile()
        }

        fileALines = fileA.readLines()
        fileBLines = fileB.readLines()
    }

    fun writeToFileA(str: String) {
        fileAbuffer += (str + "\n")
    }

    fun writeToFileB(str: String) {
        fileBbuffer += (str + "\n")
    }

    fun closeFileA(){
        fileA.printWriter().use { out ->
            out.print(fileAbuffer)
        }
    }

    fun closeFileB(){
        fileB.printWriter().use { out ->
            out.print(fileBbuffer)
        }
    }
}