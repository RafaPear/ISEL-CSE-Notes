import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object M {

    private var idx_M = 7

    val password = "1234"

    val inM: Boolean
        get() = HAL.isBit(idx_M)

    fun init(){
        HAL.init()

        try {
            val file = File(HAL.configPath)
                idx_M = 0

            file.forEachLine { i ->
                if (i.contains("m.out")) {
                    idx_M += getInputPins(i)
                }
            }
        } catch(_:Exception){
            Logger.getLogger("M").log(Level.WARNING, "No config file found, using default values")
            idx_M = pow(2,7)
        }
    }
}