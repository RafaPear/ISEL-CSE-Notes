
import TUI.capture
import TUI.clearWrite
import TUI.createCustomChar
import TUI.writeCenterLine
import isel.leic.utils.Time
import kotlin.system.exitProcess

object APP {
    private val invalidBets = charArrayOf('*','#',TUI.NONE)
    private val validBets = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D'
    )

    private var BETS = mutableMapOf<Char, Int>()

    private var CREDS: Int = 0
    private var SORTED : Char = TUI.NONE
    private var WON: Int = 0

    private var BET_LIMIT = 9 // Limite de apostas por jogo

    private var lastCoin: Int = 0 // Guarda o último valor do coin acceptor

    /**
     * Corresponde à primeira linha da tela LCD
     */
    private var line1 = "Roulette Game"

    /**
     * Corresponde à segunda linha da tela do LCD
     */
    private var line2 = "Start the Game"

    private const val CONFIRM_KEY = '*'
    private const val KEY_COIN_M = 'A'
    private const val SORTED_NUM_M = 'C'
    private const val GAME_OFF_M = 'D'
    private const val BACK_OR_CLEAR = '#'

    private var COST = 1 // Custo de cada aposta

    private const val BONUSBETS = 3
    private const val TIMEBONUS = 5 // tem que ser < 10

    private var sudoMode = false

    // ⬆️  Seta para cima
    val arrowUp = arrayOf(
        intArrayOf(0,0,0,0,0),
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(1,0,1,0,1), // 0x15
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,0,0,0) // 0x04
    )

    // ⬇️  Seta para baixo
    val arrowDown = arrayOf(
        intArrayOf(0,0,0,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(1,0,1,0,1), // 0x15
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,0,0,0)  // 0x00
    )

    val settings = arrayOf(
        intArrayOf(0,1,0,1,0), // 0x04
        intArrayOf(1,1,0,1,1), // 0x04
        intArrayOf(0,1,1,1,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x04
        intArrayOf(0,0,1,0,0), // 0x15
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(1,1,0,1,1), // 0x04
        intArrayOf(0,1,0,1,0)  // 0x00
    )

    val coin4BIG_A = arrayOf(
        intArrayOf(0,0,0,0,0),
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(1,1,1,1,1), // 0x11
        intArrayOf(1,0,1,0,1), // 0x1B
        intArrayOf(1,0,0,0,1), // 0x1F
        intArrayOf(1,0,1,1,1), // 0x19
        intArrayOf(1,1,1,1,1), // 0x11
        intArrayOf(0,1,1,1,0)  // 0x00
    )

    val coin4or2BIG_B = arrayOf(
        intArrayOf(0,0,0,0,0),
        intArrayOf(0,1,1,0,0), // 0x0E
        intArrayOf(1,0,0,1,0), // 0x11
        intArrayOf(1,0,1,0,1), // 0x1B
        intArrayOf(1,0,1,1,1), // 0x1F
        intArrayOf(1,0,0,1,1), // 0x19
        intArrayOf(0,1,0,0,1), // 0x11
        intArrayOf(0,0,1,1,0)  // 0x00
    )

    val coin4or2BIG_C = arrayOf(
        intArrayOf(0,1,1,0,0), // 0x0E
        intArrayOf(1,0,0,1,0), // 0x11
        intArrayOf(1,0,1,0,1), // 0x1B
        intArrayOf(1,0,1,1,1), // 0x1F
        intArrayOf(1,0,0,1,1), // 0x19
        intArrayOf(0,1,0,0,1), // 0x11
        intArrayOf(0,0,1,1,0),  // 0x00
        intArrayOf(0,0,0,0,0)
    )

    val coin4or2BIG_D = arrayOf(
        intArrayOf(0,0,1,0,0),
        intArrayOf(0,0,1,0,0), // 0x0E
        intArrayOf(0,0,1,0,0), // 0x11
        intArrayOf(0,0,1,0,0), // 0x1B
        intArrayOf(0,0,1,0,0), // 0x1F
        intArrayOf(0,0,1,0,0), // 0x19
        intArrayOf(0,0,1,0,0), // 0x11
        intArrayOf(0,0,1,0,0)  // 0x00
    )

    val coin4or2BIG_E = arrayOf(
        intArrayOf(0,1,1,0,0).reversed().toIntArray(), // 0x0E
        intArrayOf(1,0,0,1,0).reversed().toIntArray(), // 0x11
        intArrayOf(1,0,1,0,1).reversed().toIntArray(), // 0x1B
        intArrayOf(1,0,1,1,1).reversed().toIntArray(), // 0x1F
        intArrayOf(1,0,0,1,1).reversed().toIntArray(), // 0x19
        intArrayOf(0,1,0,0,1).reversed().toIntArray(), // 0x11
        intArrayOf(0,0,1,1,0).reversed().toIntArray(),  // 0x00
        intArrayOf(0,0,0,0,0).reversed().toIntArray()
    )

    val coin4or2BIG_F = arrayOf(
        intArrayOf(0,0,0,0,0).reversed().toIntArray(),
        intArrayOf(0,1,1,0,0).reversed().toIntArray(), // 0x0E
        intArrayOf(1,0,0,1,0).reversed().toIntArray(), // 0x11
        intArrayOf(1,0,1,0,1).reversed().toIntArray(), // 0x1B
        intArrayOf(1,0,1,1,1).reversed().toIntArray(), // 0x1F
        intArrayOf(1,0,0,1,1).reversed().toIntArray(), // 0x19
        intArrayOf(0,1,0,0,1).reversed().toIntArray(), // 0x11
        intArrayOf(0,0,1,1,0).reversed().toIntArray()  // 0x00
    )

    val coin2BIG_A = arrayOf(
        intArrayOf(0,0,0,0,0),
        intArrayOf(0,1,1,1,0), // 0x0E
        intArrayOf(1,0,0,1,1), // 0x11
        intArrayOf(1,0,1,0,1), // 0x1B
        intArrayOf(1,1,0,1,1), // 0x1F
        intArrayOf(1,0,0,0,1), // 0x19
        intArrayOf(1,1,1,1,1), // 0x11
        intArrayOf(0,1,1,1,0)  // 0x00
    )

    fun init() {
        CoinAcceptor.init()
        Statistics.init()
        CoinDeposit.init()
        M.init()

        RouletteDisplay.init()
        TUI.init()

        createCustomChar(0, arrowUp)
        createCustomChar(1, arrowDown)
        createCustomChar(2, settings)
        createCustomChar(3, coin2BIG_A)
        createCustomChar(4, coin4BIG_A)
        createCustomChar(5, coin4or2BIG_B)
        createCustomChar(6, coin4or2BIG_C)
        createCustomChar(7, coin4or2BIG_D)

        CREDS = Statistics.getCredits()

    }

    fun run(loop: Boolean = true){
        while (true) {
            lobby()
            game()
            updateStats()
            if (!loop) break
        }
    }

    fun lobby() {
        fun writeLobbyCreds(){
            if (sudoMode) return
            val creds = " $$CREDS"
            TUI.refreshPixels(creds, 1, TUI.COLS-creds.length)

        }

        fun writeLobby(doCreds : Boolean = false, doCoins : Boolean = false) {
            writeCenterLine("Roulette Game!")
            if (doCreds) writeLobbyCreds()
            if (doCoins) animateCoins()
        }

        TUI.clear()
        RouletteDisplay.clrAll()
        writeLobby(true, true)

        var col: Int

        while (true){

            if (M.inM && !sudoMode){
                m()
                writeLobby(true, true)
            }

            if (updateCreds()) {
                writeLobbyCreds()
                val list = if(lastCoin == 0) {
                    col = 4
                    listOf(5, 6, 7, 3)
                }
                else {
                    col = 8
                    listOf(5, 6, 7, 4)
                }

                for (i in list) {
                    Time.sleep(80)
                    if (updateCreds()) {
                        writeLobbyCreds()
                    }
                    TUI.refreshPixel(i.toChar(), 1, col)
                }
            }

            if (capture() == CONFIRM_KEY) break

        }
        TUI.clear()
        doBets()
    }

    fun animateCoins(){
        var col : Int
        for (i in 0..1) {
            val list = if(i == 0) {
                col = 4
                listOf(5, 6, 7, 3)
            }
            else {
                col = 8
                listOf(5, 6, 7, 4)
            }

            for (i in list) {
                Time.sleep(80)
                TUI.refreshPixel(i.toChar(), 1, col)
            }
        }
    }

    private fun doBets(){
        RouletteDisplay.clrAll()
        val tempLine2 = validBets.joinToString("")

        fun writeBets(writeBottom: Boolean = false){
            TUI.clear()
            for (bet in BETS) {
                TUI.refreshPixel(
                    if(bet.value != 0) bet.value.digitToChar() else ' ',
                    0, bet.key.toHexInt() + 1
                )
            }
            if (writeBottom) writeCenterLine(tempLine2, 1)
            displayCredits()
        }

        fun writeBet(key: Char){
            val bet = BETS[key]
            TUI.refreshPixel(
                if(bet != 0 && bet != null) bet.digitToChar() else ' ',
                0, key.toHexInt()  + 1
            )
            displayCredits()
        }

        RouletteDisplay.clrAll()
        writeBets(true)

        while (true){

            if (M.inM && !sudoMode){
                m()
                writeBets(true)
            }

            if (updateCreds()) {
                RouletteDisplay.animationD(if (lastCoin == 0) 1 else 2)
                displayCredits()
            }

            val key = TUI.waitForCapture(100)
            when (key) {
                BACK_OR_CLEAR -> { if (canStartGame()) break; writeBets() }
                CONFIRM_KEY -> continue
                TUI.NONE -> continue
                else -> {
                    if(updateBets(key)) writeBet(key) else writeBets(true)
                }
            }
        }
        TUI.clear()
        doLobbyEndAnimation()
    }

    fun updateBets(key: Char): Boolean {
        if (!canUpdateBets(key = key)) return false

        BETS.compute(key) { _, v -> (v ?: 0) + 1 }

        if (!sudoMode) CREDS -= COST

        return true
    }

    private fun displayCredits() {
        if (sudoMode) return
        RouletteDisplay.printIntList(
            CREDS.toString().map { it.toHexInt() }.reversed()
        )
    }

    private fun showError(msg: String) {
        clearWrite(msg)
        RouletteDisplay.animationB()
        RouletteDisplay.animationB()
        TUI.clear()
    }

    fun doLobbyEndAnimation() {
        val sum = BETS.values.sum()
        when (sum) {
            in 1..5 -> {
                writeCenterLine("Really?", 0)
                Time.sleep(2000)
                TUI.clear()
            }
            in 6..10 -> {
                writeCenterLine("Only That?", 0)
                RouletteDisplay.animationD(1)
                Time.sleep(2000)
                TUI.clear()
            }
            in 11..15 -> {
                writeCenterLine("Ok, $sum is fine", 0)
                RouletteDisplay.animationD(2)
                Time.sleep(2000)
                TUI.clear()
            }
            in 16..20 -> {
                writeCenterLine("Lets Go!", 0)
                RouletteDisplay.animationB()
                TUI.clear()
            }
            in 21..25 -> {
                writeCenterLine("OMG OMG!!", 0)
                RouletteDisplay.animationB()
                TUI.clear()
            }
            else -> {
                writeCenterLine("HERE WE GO!", 0)
                RouletteDisplay.animationC()
                RouletteDisplay.animationB()
                TUI.clear()
            }
        }
    }

    fun game() {
        RouletteDisplay.clrAll()
        for (i in bonusBets()){
            BETS.computeIfPresent(i) { k, v ->
                v + 1
            }
        }
        RouletteDisplay.clrAll()

        var timeRoll = (3..9).random()
        TUI.refresh {
            writeCenterLine("ROLL!",0)
            writeCenterLine("${timeRoll}s",1)
        }
        timeRoll--
        val sleep = 1000
        var endTime = Time.getTimeInMillis() + sleep
        while (true) {
            if (timeRoll <= 0) break
            if (Time.getTimeInMillis() >= endTime) {
                writeCenterLine("${timeRoll}s",1)
                timeRoll--
                endTime = Time.getTimeInMillis() + sleep
            }
            RouletteDisplay.animationA()
        }
        val sorted = validBets.random()
        var wonCredits = 0
        BETS.forEach { if(it.key == sorted) wonCredits += COST*2 else wonCredits -= COST}
        if (wonCredits < 0) wonCredits = 0
        CREDS += wonCredits
        TUI.refresh {
            TUI.writeCenterLine("Sorted: $sorted",0)
            TUI.write("Won: $$wonCredits")
        }
        SORTED = sorted
        WON = wonCredits
        BETS.replaceAll { k, v -> 0 }
        endTime = Time.getTimeInMillis() + sleep*3
        while(Time.getTimeInMillis() < endTime) {
            RouletteDisplay.setAll(sorted)
            Time.sleep(100)
            RouletteDisplay.clrAll()
            Time.sleep(100)
        }
        RouletteDisplay.clrAll()
    }

    private fun bonusBets(): List<Char>{
        BET_LIMIT += BONUSBETS // Limite de apostas para o bonus
        var timeRoll = TIMEBONUS
        writeGame(timeRoll)
        val bonus = mutableListOf<Char>()
        val sleep = 1000
        TUI.showCursor(true)

        var endTime = Time.getTimeInMillis() + sleep
        timeRoll--
        while(true){
            if (bonus.size >= BONUSBETS) break
            if (updateCreds()) TUI.refreshPixels("$$CREDS",1,TUI.COLS-"$$CREDS".length)
            val key = TUI.waitForCapture(100)

            if (key !in invalidBets && canUpdateBets(false, key)) {
                if (bonus.isNotEmpty()) {
                    TUI.write(',')
                    TUI.write(key, false, 1)
                }
                else TUI.write(key, false, 1)

                if (sudoMode) bonus += key
                else {
                    bonus += key; CREDS -= COST
                }
                if (!sudoMode)
                    TUI.writeRightLine(" $$CREDS",1)
            }
            if (timeRoll < 0) break
            if (Time.getTimeInMillis() >= endTime) {
                TUI.writeRightLine("${timeRoll}s")
                timeRoll--
                endTime = Time.getTimeInMillis() + sleep
            }
        }
        TUI.showCursor(false)
        Time.sleep(500)
        doBonusEndAnimation(bonus)
        Time.sleep(1000)
        BET_LIMIT -= BONUSBETS // Reseta o limite de apostas
        return bonus
    }

    fun doBonusEndAnimation(list: List<Char>) {
        TUI.clear()
        when (list.size) {
            0 -> {
                writeCenterLine("Meh..", 0)
            }
            BONUSBETS -> {
                writeCenterLine("OH YEAH!", 0)
                RouletteDisplay.animationC()
                RouletteDisplay.animationC()
            }
            else -> {
                writeCenterLine("Ok Ok", 0)
                RouletteDisplay.animationB()
            }
        }
    }

    private fun writeGame(timeRoll: Int) {
        line1 = "Bonus bets!"
        TUI.clear()
        TUI.write(line1,false,0)

        TUI.writeRightLine("${timeRoll}s",0)

        TUI.write("bets:",false,1,0)
        if (!sudoMode)
            TUI.writeRightLine("$$CREDS",1)
    }

    private fun m() {
        loginM()

        var id = 0

        if (M.inM) drawM(id)
        val delay = 2500
        var expected = Time.getTimeInMillis() + delay

        while (M.inM){

            val key = TUI.waitForCapture(100)
            when(key){
                CONFIRM_KEY ->  { runMockGame() ; drawM(id, refresh = true) }
                KEY_COIN_M ->   { coinPage()    ; drawM(id, refresh = true, icon = false) }
                SORTED_NUM_M -> { statsPage()   ; drawM(id, refresh = true, icon = false) }
                GAME_OFF_M ->   { shutdown()    ; drawM(id, refresh = true) }
            }
            if (Time.getTimeInMillis() >= expected) {
                id = (id + 1) % 2
                expected = Time.getTimeInMillis() + delay
                drawM(id, refresh = false, icon = false, line1 = false)
            }
        }
        TUI.clear()
        sudoMode = false
    }

    private fun drawM(id: Int,refresh: Boolean = false,line1: Boolean = true, line2: Boolean = true, icon:Boolean = true){
        when(id){
            0 -> writeMA(refresh, line1, line2, icon)
            1 -> writeMB(refresh, line1, line2, icon)
        }
    }

    private fun runMockGame() {
        sudoMode = true
        val lastCreds = CREDS
        lobby()
        game()
        CREDS = lastCreds
        sudoMode = false
    }

    private fun shutdown() {
        if (TUI.confirmMenu("Are you sure?")) {
            TUI.clear()
            writeCenterLine("Shutting down...", 0)
            Time.sleep(1000)
            writeAllStats()
            resetAll()
            TUI.clear()
            RouletteDisplay.clrAll()
            exitProcess(0)
        }
    }

    private fun statsPage(){
        val page = Page(listOf("# to exit","* to reset") + Statistics.getSortedList())
        page.run {key, clear ->
            when (key) {
                CONFIRM_KEY -> {
                    if (TUI.confirmMenu("Reset All?")) {
                        Statistics.resetAll()
                        clearWrite("Reset Completed")
                        Time.sleep(1000)
                        page.lines = Statistics.getSortedList()
                    }
                    clear()
                    M.inM
                }
                BACK_OR_CLEAR -> {
                    false
                }
                else -> M.inM
            }
        }
    }

    private fun coinPage() {
        val lines = listOf(
            "# to exit",
            "* to reset",
            "2 Coins: ${CoinDeposit.getTotal(0)}",
            "4 Coins: ${CoinDeposit.getTotal(1)}",
            "Total: ${CoinDeposit.getTotal(0) + CoinDeposit.getTotal(1)}",
            "Games: ${Statistics.getGames()}",
            "Credits: ${Statistics.getCredits()}"
        )

        val page = Page(lines)

        page.run{ key, reset ->
            when (key) {
                CONFIRM_KEY -> {
                    if (TUI.confirmMenu("Reset All?")) {
                        resetAll()
                        Time.sleep(1000)
                        page.lines = listOf(
                            "2 Coins: ${CoinDeposit.getTotal(0)}",
                            "4 Coins: ${CoinDeposit.getTotal(1)}",
                            "Total: ${CoinDeposit.getTotal(0) + CoinDeposit.getTotal(1)}",
                            "Games: ${Statistics.getGames()}",
                            "# to exit",
                            "* to reset"
                        )
                    }
                    reset()
                    M.inM
                }
                BACK_OR_CLEAR -> {
                    false
                }
                else -> M.inM
            }
        }
    }

    private fun writeMA(refresh: Boolean = false,line1: Boolean = true, line2: Boolean = true, icon:Boolean = true) {
        if (!M.inM) return
        if (refresh) TUI.clear()
        if (line1) TUI.refreshPixels("M Active", 0)
        if(line2) TUI.refreshPixels("A-Coins  C-Stats", 1)
        if(icon) writeSettingsIcon()
    }

    private fun writeMB(refresh: Boolean = false,line1: Boolean = true, line2: Boolean = true, icon:Boolean = true){
        if (!M.inM) return
        if (refresh) TUI.clear()
        if (line1) TUI.refreshPixels("M Active", 0)
        if(line2) TUI.refreshPixels("*-Game     D-Off", 1)
        if(icon) writeSettingsIcon()
    }

    private fun writeSettingsIcon(){
        TUI.refreshPixel(2.toChar(), 0, TUI.COLS-1)
    }

    private fun loginM() {
        var input = ""
        var inputCripted = ""
        line1 = "Login"
        line2 = "Password: "

        TUI.refresh(
            { writeCenterLine(line1, 0) },
            { write(line2) }
        )

        TUI.showCursor(true)

        while(input != M.password){
            if (!M.inM) break


            val key = TUI.waitForCapture(100)

            if (key == '#' && input != ""){
                input = input.dropLast(1)
                inputCripted = inputCripted.dropLast(1)
                TUI.clearChar()
            }
            else if (TUI.isValid(key) && key !in invalidBets ) {
                val tempInput = input + key
                if (tempInput.length <= M.password.length) {
                    input = tempInput
                    inputCripted += '*'
                    TUI.write('*')
                }
                if (input == M.password) break
            }
            //importante evita TUI.refresh desnecessários
            if (input.length == M.password.length) {
                if (line1 == "Login"){
                    line1 = "Invalid password"
                    TUI.refresh(
                        { writeCenterLine(line1, 0) },
                        { write(line2 + inputCripted) }
                    )
                }
            } else {
                if (line1 != "Login"){
                    line1 = "Login"
                    TUI.refresh(
                        { writeCenterLine(line1, 0) },
                        { write(line2 + inputCripted) }
                    )
                }
            }
            Time.sleep(10)
        }
        TUI.clear()
        TUI.showCursor(false)
    }

    private fun resetAll(){
        BETS.replaceAll { k, v -> 0 }
        CREDS = 0
        CoinDeposit.resetTotal(0)
        CoinDeposit.resetTotal(1)
        Statistics.resetAll()
        showError("Reset Completed")
    }

    private fun canStartGame(): Boolean{
        if (BETS.isEmpty()){
            clearWrite("You need at least 1 Bet")
            RouletteDisplay.animationB()
            return false
        }
        return true
    }

    private fun canUpdateBets(print : Boolean = true, key: Char): Boolean {
        if ((BETS[key] ?: 0) >= BET_LIMIT) {
            if (print) showError("Bet Limit reached")
            return false
        }

        if(sudoMode) return true

        if(CREDS <= 0){
            if (print) showError("Not enough Credits")
            return false
        }
        return true
    }

    private fun updateCreds(): Boolean {
        if (sudoMode) return true
        val coin = CoinAcceptor.getCoin()
        if (coin != -1) {
            CoinDeposit.updateTotal(coin, 1)
            CREDS += when (coin) {
                0 -> 2
                1 -> 4
                else -> error("Invalid coin type")
            }
            lastCoin = coin
            Statistics.updateCredits(CREDS)
            return true
        }
        return false
    }

    private fun updateStats(){
        if (sudoMode) return
        Statistics.addTotal(1)
        Statistics.updateCredits(CREDS)
        if (SORTED != TUI.NONE) {
            Statistics.updateEntry(SORTED, 1, WON)
        }
        SORTED = TUI.NONE
        WON = 0
    }

    private fun writeAllStats(){
        Statistics.writeToFile()
        CoinDeposit.writeToFile()
        Statistics.closeFileB()
        CoinDeposit.closeFileA()
    }

    private class Page(
        var lines: List<String>,
        val upKey : Char = 'A',
        val downKey : Char = 'B',
        val upSym : Int = 0,
        val downSym : Int = 1,
    ) {

        val rTop = upSym.toChar()+upKey.toString() + ' ' + 2.toChar()
        val rBottom = downSym.toChar()+downKey.toString() + "  "

        val line1MAX = TUI.COLS-rTop.length
        val line2MAX = TUI.COLS-rBottom.length

        private fun write(idx: Int, idx2: Int){
            if (line1.length > line1MAX) error("Very Big, line length should be less than $line1MAX")
            if (line2.length > line2MAX) error("Very Big, line length should be less than $line2MAX")

            val line1 = lines[idx].padEnd(line1MAX, ' ') + rTop
            val line2 = lines[idx2].padEnd(line2MAX, ' ') + rBottom

            TUI.refresh (
                {TUI.refreshPixels(line1, 0, 0)},
                {TUI.refreshPixels(line2, 1, 0)}
            )

            //TUI.refreshPixels (line1, 0, 0)
            //TUI.refreshPixels (line2, 1, 0)

            //TUI.refreshPixels(rTop, 0, line1MAX)
            //TUI.refreshPixels(rBottom, 1, line2MAX)
        }

        fun run(func: (key: Char, reset: () -> Unit)->Boolean) {

            TUI.clear()
            writeSettingsIcon()
            var idx = 0
            var idx2 = 1

            write(idx, idx2)

            while (true) {
                val key = TUI.waitForCapture(100)
                if (!func(key){ write(idx, idx2)}) break
                when(key){
                    upKey -> {
                        idx2 = idx
                        idx = capInside(idx-1, 0, lines.size - 1)

                        write(idx, idx2)
                    }
                    downKey -> {
                        idx = idx2
                        idx2 = capInside(idx+1, 0, lines.size - 1)
                        write(idx, idx2)
                    }
                }
            }
        }
    }
}
