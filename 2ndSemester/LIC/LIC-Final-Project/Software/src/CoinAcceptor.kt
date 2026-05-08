import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object CoinAcceptor {
    var COIN_ID = pow(2,5) // ID do bit que indica a moeda
    var COIN_INSERTED_ID = pow(2,6) // ID do bit que indica se a moeda foi inserida
    var ACCEPT_ID = pow(2,6) // ID do bit que indica se a moeda foi aceita

    fun init() {
        HAL.init()

        try {
            val file = File(HAL.configPath)
            COIN_ID = 0
            COIN_INSERTED_ID = 0
            ACCEPT_ID = 0

            file.forEachLine { i ->
                if (i.contains("ca.Coin_id")) {
                    COIN_ID += getInputPins(i)
                } else if (i.contains("ca.Coin")){
                    COIN_INSERTED_ID += getInputPins(i)
                } else if (i.contains("ca.accept")) {
                    ACCEPT_ID += getOutputPins(i)
                }
            }
        } catch(e:Exception){
            Logger.getLogger("CoinAcceptor").log(Level.WARNING, "No config file found, using default values")
            println(e.message)
            COIN_ID = pow(2,5)
            COIN_INSERTED_ID = pow(2,6)
            ACCEPT_ID = pow(2,6)
        }
    }

    /**
     * Deteta se uma moeda foi introduzida
     * @return true se uma moeda foi introduzida, false caso contrário
     */
    fun isCoinInserted(): Boolean =
        HAL.isBit(COIN_INSERTED_ID)

    fun getCoin(): Int {
        var coin = -1
        if (isCoinInserted()) {
            coin = HAL.isBit(COIN_ID).toInt()
            // Espera até que a moeda seja retirada
            HAL.setBits(ACCEPT_ID)

            while (isCoinInserted()) { }

            HAL.clrBits(ACCEPT_ID)
        }
        return coin
    }
}