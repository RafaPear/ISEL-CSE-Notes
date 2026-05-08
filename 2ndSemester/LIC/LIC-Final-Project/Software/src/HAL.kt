import isel.leic.UsbPort

/**
 * Objeto responsável pela comunicação com o hardware, através do [UsbPort]
 */
object HAL {

    /**
     * Guarda o valor do array de LEDs
     */
    var light = 0b0000_0000

    var configPath = "rouletteGame.simul"

    /**
     * Inicia o objeto, escrevendo no [UsbPort] os bits do [light]
     */
    fun init(){
        writeBits(0b1111_1111,light)
    }

    /**
     * Retorna 'true' se o bit definido pela [mask] está com o valor lógico '1' no [UsbPort],
     * se a [mask] tiver com mais que um bit com valor lógico '1' retorna 'false'
     * @param mask recebe uma máscara com apenas um bit a '1'
     * @return [Boolean]
     */
    fun isBit(mask: Int): Boolean{
        if (mask.countOneBits() == 1){
            if (UsbPort.read().and(mask).countOneBits() == 1)
                return true
        }
        return false
    }
    /**
     * Retorna os valores dos bits representados por [mask] presentes no [UsbPort]
     * @param mask define os bits desejados
     * @return [Int]
     */
    fun readBits(mask: Int): Int{
        return UsbPort.read().and(mask)
    }

    /**
     * Envia para o [UsbPort] os bits representados por [mask] correspondentes em value
     * @param mask define os bits desejados
     * @param value bits pretende escrever no [UsbPort]
     */
    fun writeBits(mask: Int, value: Int){
        light = (mask.inv().and(light)).or(value.and(mask))
        UsbPort.write(light)
    }

    /**
     * Coloca os bits representados por [mask] no valor lógico '1'
     * @param mask define os bits desejados
     */
    fun setBits(mask: Int){
        light = mask.or(light)
        UsbPort.write(light)
    }

    /**
     * Coloca os bits representados por [mask] no valor lógico '0'
     * @param mask define os bits desejados
     */
    fun clrBits(mask: Int){
        light = mask.inv().and(light)
        UsbPort.write(light)
    }
}