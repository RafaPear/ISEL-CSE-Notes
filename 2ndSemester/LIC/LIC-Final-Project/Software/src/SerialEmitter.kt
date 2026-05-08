
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Envia tramas para os diferentes módulos Serial Receiver.
 */
object SerialEmitter {
    /**
     * Contem os modulos que podem receber uma trama em serie
     */
    enum class Destination {
        LCD, ROULETTE
    }

    private val SIMUL = isSimul()

    /**
     * Define o índice do sinal SS do LCD
     */
    var SS_LCD_ID = 0

    /**
     * Define o índice do sinal SS do Roulette Display
     */
    var SS_RD_ID = 1

    /**
     * Define o índice do sinal SDX
     */
    var SDX_ID = 3

    /**
     * Define o índice do sinal SCLK
     */
    var SCLK_ID = 4


    /**
     * Inicia a class
     */
    fun init() {
        HAL.init()

        try {
            val file = File(HAL.configPath)
            SS_LCD_ID = 0
            SS_RD_ID = 0
            SDX_ID = 0
            SCLK_ID = 0

            file.forEachLine { i ->
                if (i.contains("srl./SS")) {
                    SS_LCD_ID += getOutputPins(i)
                } else if (i.contains("srr./SS")){
                    SS_RD_ID += getOutputPins(i)
                } else if (i.contains("srl.SDX")) {
                    SDX_ID += getOutputPins(i)
                } else if (i.contains("srl.SCLK")) {
                    SCLK_ID += getOutputPins(i)
                }
            }
        } catch(_:Exception){
            Logger.getLogger("SerialEmitter").log(Level.WARNING, "No config file found, using default values")
            SS_LCD_ID = pow(2,0)
            SS_RD_ID = pow(2,1)
            SDX_ID = pow(2,3)
            SCLK_ID = pow(2,4)
        }
    }

    /**
     * Chama a função responsável por enviar cada tipo de trama
     * @param addr define o destinatário da trama
     * @param data
     * @param size
     */
    fun send(addr: Destination, data: Int, size: Int) {
        if (addr == Destination.LCD) {
            parseAndSend(data, size, SS_LCD_ID)
        } else if (addr == Destination.ROULETTE) {
            parseAndSend(data, size, SS_RD_ID)
        }
    }

    /**
     *Efetua o envio final dos dados em serie e do SDLK, e o calculo do bit de paridade
     * @param data
     * @param size
     * @param time
     */
    private fun parseAndSend(data: Int, size: Int, addr: Int) {
        rst()
        HAL.clrBits(addr)

        val p = if (data.countOneBits() % 2 == 0) 1 else 0

        for (i in 0..size){
            if (i == size) {
                if (p.isBit(0))
                    HAL.setBits(SDX_ID)
                else
                    HAL.clrBits(SDX_ID)

            }
            else {
                if (data.isBit(i)) {
                    HAL.setBits(SDX_ID)
                }
                else {
                    HAL.clrBits(SDX_ID)
                }
            }
            if (!SIMUL)
                HAL.setBits(addr)

            HAL.setBits(SCLK_ID)

            HAL.clrBits(SCLK_ID)

            HAL.clrBits(SDX_ID)
        }
        if (SIMUL)
            HAL.setBits(addr)
        rst()
    }

    fun rst(){
        HAL.setBits(SS_LCD_ID + SS_RD_ID)
        HAL.clrBits(SDX_ID + SCLK_ID)
    }
}
