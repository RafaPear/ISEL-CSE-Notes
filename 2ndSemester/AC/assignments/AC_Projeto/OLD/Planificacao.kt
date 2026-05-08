val DICES = arrayOf(
    4U,
    6U,
    8U,
    12U
)

val TOTAL_DICES = 4U

var tiks = 0

fun main(){
    game()
}

fun isr(){
    TODO()
}

fun game(){
    TODO()
}


// -------------------------------------
//          Utility functions
// -------------------------------------

// DONE
fun sevenSeg(num: UInt) {
    // Simulate a 7-segment display
    println("Displaying number: $num")
}

// O STOR VAI DAR ig
fun randGen(max: UInt): UInt {
    return (0U..max).random()
}

fun getDice(dice: UInt): UInt = DICES[dice.toInt()]
