
import TUI.writeCenterLine
import TUI.writeRightLine
import isel.leic.utils.Time

object TUI {
    private var canWrite: Boolean = true

    const val NONE = KBD.NONE
    const val COLS = LCD.COLS
    const val LINES = LCD.LINES

    private var CGRAMaddr = 0

    fun init() {
        LCD.init()
        KBD.init()
        clear()
        Time.sleep(100)
    }

    /**
     * Pula para a para a segunda linha da tela LCD
     */
    fun nextLine() {
        LCD.cursor(1, 0)
    }

    /**
     * Escreve um [Char] no LCD
     * @param ch
     * @param wrap quando 'false' não utiliza o salto automático de linha
     */
    fun write(ch: Char,wrap: Boolean = true, line: Int = LCD.cursorPos.first,
              column: Int = LCD.cursorPos.second
    ){
        if (line !in 0..1 || column > LCD.COLS) error("invalid line or column")
        LCD.cursor(line,column)
        LCD.write(ch,wrap)
    }

    /**
     * Escreve uma [String] no LCD
     * @param line1
     * @param wrap quando 'false' não utiliza o salto automático de linha
     */
    fun write(line1: String, wrap: Boolean = true, line: Int = LCD.cursorPos.first,
              column: Int = LCD.cursorPos.second) =
        if (line1.length > LCD.COLS * 2)
            error("String very Big")
        else {
            LCD.cursor(line,column)
            LCD.write(line1, wrap)
        }

    /**
     * È um whilhe true que captura as teclas e as impremie na tela LCD
     */
    fun captureAndPrint() {
        while (true) {
            val key = KBD.getKey()
            if (key == '*' && canWrite) {
                LCD.clear()
                canWrite = false
            } else if (canWrite && key != NONE) {
                LCD.write(key)
                key == NONE
                canWrite = false
            } else if (key == NONE)
                canWrite = true
        }
    }

    /**
     * captura a tecla e retorna esta mesma
     * @return [Char]
     */
    fun capture(): Char {
        val key = KBD.getKey()
        return if (canWrite && key != NONE) {
            canWrite = false
            key
        } else if (key == NONE) {
            canWrite = true
            key
        }
        else NONE
    }

    /**
     * captura a tecla e retorna esta mesma
     * @return [Char]
     */
    fun waitForCapture(timeout: Long): Char {
        val key = KBD.waitKey(timeout)
        return key
    }

    /**
     * Quebra a [String] e escreve-a nas duas linhas da tela LCD
     * @param text
     */
    fun writeSplited(text: String) {
        if (text.length > LCD.COLS * 2) error("String verry Big")
        var count = 0
        val words = text.split(Regex("(?<=\\s)|(?=\\s)"))
        for (word in words) {
            if (count + word.length > LCD.COLS) {
                LCD.cursor(1, 0)
                count = 0
            }
            for (i in word)
                LCD.write(i)
            count += word.length
        }
    }

    /**
     * Quebra se necessário a [String] e chama [writeRightLine]
     * @param line1
     */
    fun writeRight(line1: String) {
        if (line1.length > LCD.COLS * 2) error("String very Big")
        if (line1.length > LCD.COLS) {
            // se a String for maior que a primeira linha ele quebra e escreve nas duas linhas
            writeRightLine(line1.substring(0, LCD.COLS),0)
            writeRightLine(line1.substring(LCD.COLS, line1.length),1)
        } else writeRightLine(line1)
    }

    /**
     * Escreve o [text] o mais deslocado para a direita possível
     * @param text
     */
    fun writeRightLine(text: String,line: Int = 0) {
        if (line !in 0..1 || text.length >= COLS) error("invalid line or text")
        var column = COLS - text.length
        refreshPixels(text,line,column)
    }

    /**
     * Se necessário quebra a [String] em dois para poder escrever centrado, chamando o método [writeCenterLine]
     * @param line1
     */
    fun writeCenter(line1: String) {
        if (line1.length > LCD.COLS * 2) error("String verry Big")
        if (line1.length > LCD.COLS) {
            // se a String for maior que a primeira linha ele quebra e escreve nas duas linhas
            writeCenterLine(line1.substring(0, LCD.COLS), 0)
            writeCenterLine(line1.substring(LCD.COLS, line1.length), 1)
        } else writeCenterLine(line1)
    }

    /**
     * Escreve a [String] centrada na linha definida em [line]
     * @param line1
     * @param line
     */
    fun writeCenterLine(str: String, line: Int = 0) {
        if (line !in 0..1) error("invalid line")
        if (str.length > COLS) error("very big")
        val column = COLS / 2 - str.length/2
        refreshPixels(str, line,column)
    }

    private fun moveStrInArray(line1: CharArray, dir: Int = 1) {
        if (dir != -1 && dir != 1) error("invalid direction")
        val lastCH = line1.indexOfLast { it != '\u0000' }
        if (lastCH != line1.lastIndex) {
            for (i in lastCH downTo 0) {
                line1[i + dir] = line1[i]
                line1[i] = '\u0000'
            }
        }
    }

    /**
     * Cria uma animação de loading screen. Para quando a condição for verdadeira
     * @param time
     * @param condition
     */
    fun loadingScreen(time: Long = 200, condition: () -> Boolean) {
        LCD.clear()
        writeCenter("Loading")

        var i = 0
        while (!condition()) {
            LCD.write('.', false)

            if (i == 3) {
                i = -1
                LCD.clear()
                writeCenter("Loading")
            }
            i++
            Time.sleep(time)
        }
        LCD.clear()
    }

    /**
     * Cria uma animação de um texto que se desloca da direita para a esquerda.
     * @param line1
     * @param time velocidade a qual o texto irá andar
     * @param breakKey tecla a qual irá quebra o loop
     */
    fun writeWalkText(line1: String, time: Long = 200, breakKey: Char) {
        var window = ""
        line1.padEnd(LCD.COLS, ' ')

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        window += line1

        for (i in 0 until LCD.COLS - 1) {
            window += " "
        }
        var l = 0
        var r = LCD.COLS - 1

        while (true) {
            if (KBD.getKey() == breakKey) return
            if (r == window.length) {
                r = LCD.COLS - 1
                l = 0
            }
            for (i in l..r) {
                LCD.write(window[i], false)
            }
            l++
            r++
            Time.sleep(time)
            LCD.clear()
        }
    }

    fun confirmMenu(
        question: String = "Continue?",
        yes: String = "Yes",
        no: String = "No",
        yesKey: Char = 'A',
        noKey: Char = 'B'
    ): Boolean {
        clear()
        writeCenterLine(question, 0)
        writeCenterLine("$yes($yesKey)  $no($noKey)", 1)

        while (true) {
            val key = capture()
            if (key == yesKey) return true
            else if (key == noKey) return false
        }
    }

    fun switchScreen(list: Array<String>, sleep: Long, wrt: (l: String) -> Unit) {
        for (line in list) {
            wrt(line)
            Time.sleep(sleep)
            clear()
        }
    }

    fun clearWrite(line: String, wrap: Boolean = true) {
        clear()
        writeSplited(line)
    }

    /**
     * limpa a tela LCD
     */
    fun clear(){
        LCD.clear()
    }

    /**
     * valida a tecla
     * @param key
     */
    fun isValid(key: Char): Boolean = key != NONE

    /**
     * Limpa a tela LCD, e em seguida executa o lambda [write1] para escrever na tela, mesma coisa para o [write2]
     * ambos em linhas diferentes
     * @param write1 uma função de extensão do TUI
     * @param write2 uma função de extensão do TUIa
     */
    fun refresh(write1: TUI.()-> Unit = {},write2: TUI.()-> Unit = {}) {
        clear()
        write1()
        nextLine()
        write2()
    }

    fun refreshPixel(ch: Char, line: Int = LCD.cursorPos.first, column: Int = LCD.cursorPos.second
    ){
        if (line !in 0..< LCD.LINES || column !in 0..< LCD.COLS) error("invalid line or column")
        val temp_cursor = LCD.cursorPos
        LCD.cursor(line,column)
        write(ch)
        LCD.cursor(temp_cursor.first,temp_cursor.second)
    }

    fun refreshPixels(ch: String, line: Int = LCD.cursorPos.first, column: Int = LCD.cursorPos.second) {
        if (line !in 0..< LCD.LINES || column !in 0..< LCD.COLS) error("invalid line or column")
        if (!hasSpace(ch,column)) error("Very Big")
        val temp_cursor = LCD.cursorPos
        LCD.cursor(line,column)
        write(ch)
        LCD.cursor(temp_cursor.first,temp_cursor.second)
    }

    fun clearChar() {
        refreshPixel(' ',LCD.cursorPos.first, LCD.cursorPos.second-1)
        LCD.cursor(LCD.cursorPos.first,LCD.cursorPos.second-1)
    }

    fun showCursor(on: Boolean) {
        LCD.showCursor(on)
    }

    fun hasSpace(ch: String,column: Int = LCD.cursorPos.second): Boolean = ch.length + column <= COLS

    fun clearLine(line: Int, start: Int = 0, end : Int = COLS) {
        if (line !in 0..1) error("invalid line")
        val clean = ' '
        val cursor = LCD.cursorPos
        LCD.cursor(line,start)
        for (i in 0..< end){
            LCD.write(clean)
        }
        LCD.cursor(cursor.first,cursor.second)
    }

    fun createCustomChar(index: Int, bitmap: Array<IntArray>) {
        require(bitmap.size == 8 && bitmap.all { it.size == 5 }) { "Bitmap tem de ser 5×8" }

        // Apontar a CGRAM
        val addr = 0x40 or (index shl 3)            // cada char ocupa 8 bytes
        LCD.sendCMD(addr)                           // RS = 0 → comando

        // Enviar 8 linhas (RS = 1 → dados)
        for (row in bitmap) {
            var byte = 0
            for (col in 0 until 5) {
                byte = byte or ((row[col] and 1) shl col)
            }
            LCD.write(byte, wrap = false)           // envia byte à CGRAM
        }

        // Voltar à DDRAM (por ex. cursor home)
        //LCD.cursor(0, 0)
    }

    fun animtest() {
        val b = intArrayOf(0x13,0x15,0x10)
        val o = intArrayOf(0x12,0x14,0x16)
        val n = intArrayOf(0x1D,0x10,0x1B)
        val u = intArrayOf(0x1B,0x1C,0x1D)
        val s = intArrayOf(0x12,0x10,0x15)

        var i = 0
        while (true){
            i = i % (b.size)
            val q = (i+1)%(b.size)
            val w = (i+2)%(b.size)
            val e = (i+3)%(b.size)
            val r = (i+4)%(b.size)
            val t = (i+5)%(b.size)
            RouletteDisplay.set(0x0b,5)
            RouletteDisplay.set(0x00,4)
            RouletteDisplay.set(n[e],3)
            RouletteDisplay.set(u[r],2)
            RouletteDisplay.set(0x05,1)
            RouletteDisplay.update()

            Time.sleep(100)
            i++
        }
    }

}