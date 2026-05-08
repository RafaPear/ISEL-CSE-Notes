import isel.leic.utils.Time

/**
 * Objeto responsável pela escrita no LCD através do [HAL] e do [SerialEmitter], usando a interface a 4 bits
 */
object LCD {
    /**
     * Posição do cursor, no formato [Pair] (linha, coluna)
     */
    var cursorPos: Pair<Int, Int> = Pair(0, 0) // (linha, coluna)

    /**
     * Número de linhas da tela LCD
     */
    const val LINES = 2

    /**
     * Número de colunas da tela LCD
     */
    const val COLS = 16

    /**
     * tamanho de um Nibble
     */
    private const val NIBBLE = 4

    /**
     * Define se a interface é Série ou Paralela.
     */
    private const val SERIAL_INTERFACE = true

    private val SIMUL = isSimul()

    /**
     * Define a posição, no [isel.leic.UsbPort], do bit que E
     */
    var E_MASK = if (SERIAL_INTERFACE) 1 else 0b0010_0000
    /**
     * Define a posição, no [isel.leic.UsbPort], do bit RS
     */
    var RS_MASK = if (SERIAL_INTERFACE) 1 else 0b0001_0000
    /**
     * Define a posição, no [isel.leic.UsbPort], de um nibble
     */
    var NIBBLE_MASK = if (SERIAL_INTERFACE) 1 else 0b0000_1111

    /**
     * Escreve um nibble de comando/dados no LCD em paralelo.
     * @param rs bit RS
     * @param data
     */
    private fun writeNibbleParallel(rs: Boolean, data: Int) {
        // Envia rs
        if(rs.toInt() == 1) HAL.setBits(RS_MASK)
        else HAL.clrBits(RS_MASK)

        // Envia E On
        HAL.setBits(E_MASK)

        // Envia data
        HAL.writeBits(NIBBLE_MASK, data)

        // Envia E Off
        HAL.clrBits(E_MASK)

    }

    /**
     * Escreve um nibble de comando/dados no LCD em série.
     * @param rs bit RS
     * @param data
     */
    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        SerialEmitter.send(SerialEmitter.Destination.LCD, data.shl(1)+rs.toInt(), NIBBLE+1)
    }

    /**
     * Escreve um nibble de comando/dados no LCD.
     * @param rs bit RS
     * @param data
     */
    private fun writeNibble(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE)
            writeNibbleSerial(rs, data)
        else
            writeNibbleParallel(rs, data)
    }

    /**
     * Escreve um byte de comando/dados no LCD.
     * @param rs bit RS
     * @param data
     */
    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, data.shr(NIBBLE))
        writeNibble(rs, data and 0b0000_1111)
        Time.sleep(1)
    }

    /**
     * Escreve um comando no LCD.
     * @param data
     */
    private fun writeCMD(data: Int) {
        writeByte(false, data)
    }

    /**
     * Escreve um dado no LCD.
     */
    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }

    /**
     * Envia a sequência de iniciação da tela LCD, para comunicação a 4 bits.
     */
    fun init() {
        SerialEmitter.init()

        val timeList = longArrayOf(15, 5, 1)

        /**
         * Sequencia de comandos para inicializar a tela LCD
         */
        val initCode =
            if(!SIMUL)
                intArrayOf(
                    0b0000_0011,
                    0b0010_0011,

                    0b0000_0010, // Return home
                    0b0000_1100, /// Cursor On / Blinking On
                )
            else
                intArrayOf(
                    0b0000_0011,
                    0b0000_0010,
                    0b0000_0010,
                    0b0000_1000,
                    0b0000_0000,
                    0b0000_1000,
                    0b0000_0000,
                    0b0000_0001,
                    0b0000_0000,
                    0b0000_0110,

                    0b0000_0001, // Clear display
                    0b0000_0010, // Return home
                    0b0000_1100 /// Cursor On/Blinking On
                )

        for (time in timeList) {
            Time.sleep(time)
            writeNibble(false, initCode[0])
        }

        writeNibble(false, initCode[1])

        for (i in 2 until initCode.size) {
            writeByte(false, initCode[i])
        }
    }

    /**
     * Escreve um [Char] no LCD
     * @param c
     * @param wrap quando 'false' não utiliza o salto automático de linha
     */
    fun write(c: Char, wrap: Boolean = true) {
        writeDATA(c.code)
        autoCursor(wrap)
    }

    fun write(c: Int, wrap: Boolean = true) {
        writeDATA(c)
        autoCursor(wrap)
    }

    fun sendCMD(data: Int) {
        writeCMD(data)
    }

    /**
     * Escreve um [String] no LCD
     * @param text
     * @param wrap quando 'false' não utiliza o salto automático de linha
     */
    fun write(text: String, wrap: Boolean = true) {
        for (c in text) {
            write(c, wrap)
        }
    }

    /**
     * Altera a posição do cursor
     * @param line
     * @param column
     */
    fun cursor(line: Int, column: Int) {
        if (line in 0 until LINES && column in 0 until COLS) {
            val address = when (line) {
                0 -> (column + 0b1000_0000)
                1 -> (column + 0b1100_0000)
                else -> 0
            }
            cursorPos = Pair(line, column)
            writeCMD(address)
        } else {
            throw IllegalArgumentException("Posição Inválida.")
        }
    }

    /**
     * Limpa o ecrã e posiciona o cursor em (0,0)
     */
    fun clear() { /* Implementação */
        writeCMD(1)
        cursorPos = Pair(0, 0)
    }

    /**
     * Atualiza a posição do cursor automáticamente, incrementando 1, se [wrap] for 'true'
     * muda de linha automáticamente quando ultrapassa as colunas
     * @param wrap
     */
    fun autoCursor(wrap: Boolean) {
        if (wrap && cursorPos.second >= COLS -1 && cursorPos.first == 0) cursor(1, 0)
        else cursorPos = cursorPos.copy(second =
            if (cursorPos.second + 1 >= COLS) COLS-1 else cursorPos.second + 1
        )
    }

    fun showCursor(on: Boolean) {
        if(on) writeCMD(0b0000_1111) else writeCMD(0b0000_1100)
    }
}