import java.util.logging.Level
import java.util.logging.Logger

object CoinDeposit {
    private var coins = arrayOf(0, 0) // COIN0, COIN1

    fun init(){
        FileAccess.init()
        try {
            coins[0] = FileAccess.fileALines[1].toInt()
            coins[1] = FileAccess.fileALines[2].toInt()
        }
        catch (e:Exception){
            Logger.getLogger("CoinDeposit").log(Level.WARNING, "Error while reading file")
            coins = arrayOf(0, 0)
        }
    }

    fun updateTotal(coin: Int,value: Int) {
        when (coin) {
            0 -> coins[0] += value
            1 -> coins[1] += value
            else -> error("Invalid coin type")
        }
    }

    fun getTotal(coin: Int): Int {
        return when (coin) {
            0 -> coins[0]
            1 -> coins[1]
            else -> error("Invalid coin type")
        }
    }

    fun resetTotal(coin: Int) {
        when (coin) {
            0 -> coins[0] = 0
            1 -> coins[1] = 0
            else -> error("Invalid coin type")
        }
    }

    fun writeToFile() {
        FileAccess.writeToFileA(
            "${coins[0]}\n${coins[1]}"
        )
    }

    fun closeFileA(){
        FileAccess.closeFileA()
    }
}