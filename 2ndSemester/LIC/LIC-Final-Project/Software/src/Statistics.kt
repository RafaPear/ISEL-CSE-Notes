import java.util.logging.Level
import java.util.logging.Logger

object Statistics {
    private var TOTAL_GAMES = 0

    private var TOTAL_CREDITS = 0

    private data class Entry(val id: Int, var total: Int = 0, var creds : Int = 0)

    const val MAX_ID = 16

    private var SORTED = Array<Entry>(MAX_ID) { Entry(it) }

    fun init(){
        FileAccess.init()
        try {
            TOTAL_GAMES = FileAccess.fileALines[0].toInt()
            TOTAL_CREDITS = FileAccess.fileALines[1].toInt()
            for (i in FileAccess.fileBLines) {
                val values = i.split(';')
                SORTED[values[0].toInt()] =
                    Entry(
                        values[0].toInt(),
                        values[1].toInt(),
                        values[2].toInt()
                    )
            }
        }
        catch (e: Exception){
            Logger.getLogger("Statistics").log(Level.WARNING, "Error while reading from file")
            SORTED = Array(MAX_ID) { Entry(it) }
        }
    }

    fun getGames(): Int{
        return TOTAL_GAMES
    }

    fun getSortedList(): List<String> {
        val list = mutableListOf<String>()
        for (i in SORTED) {
            list += "${i.id.toCharId()};${i.total};${i.creds}"
        }
        return list
    }

    fun getCredits(): Int{
        return TOTAL_CREDITS
    }

    fun resetGames(){
        TOTAL_GAMES = 0
    }

    fun resetCredits(){
        TOTAL_CREDITS = 0
    }

    fun resetAll() {
        resetGames()
        resetCredits()
        for (i in SORTED) {
            i.total = 0
            i.creds = 0
        }
    }

    fun updateEntry(entry: Char, addTotal : Int = 0, addCreds : Int = 0) {
        val id = entry.toHexInt()
        if (id <= MAX_ID) {
            SORTED[id].apply { this.total += addTotal ; this.creds += addCreds }
        }
    }

    fun addTotal(amount: Int) {
        TOTAL_GAMES += amount
    }

    fun updateCredits(newValue: Int){
        TOTAL_CREDITS = newValue
    }

    fun writeToFile() {
        FileAccess.writeToFileA("$TOTAL_GAMES\n$TOTAL_CREDITS")
        for (entry in SORTED) {
            FileAccess.writeToFileB("${entry.id};${entry.total};${entry.creds}")
        }
    }

    fun closeFileB(){
        FileAccess.closeFileB()
    }
}