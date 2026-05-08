// Trabalho 1 de PG-LEIC15D Ano Letivo 24/25
// Professor: Nuno Leite
// Grupo I: Francisco Mendes, Gustavo Costa e Rafael Pereira

// variable/constant set up
const val MAX_TRIES = 10 // in 5..20
const val SIZE_POSITIONS = 4 // in 2..6
const val SIZE_COLORS = 6 // in 2..10 and >= SIZE_POSITIONS
const val FIRST_COLOR = 'A' // ‘A’ or ‘a’ or ‘0’
val COLORS = FIRST_COLOR ..< FIRST_COLOR+SIZE_COLORS

fun main() {
    val secret: String = generateSecret()
    println("Find the code in $MAX_TRIES attempts.")
    println("$SIZE_POSITIONS positions and $SIZE_COLORS colors $COLORS")
    for (numTries in 1..MAX_TRIES) {
        val guess = readGuess(numTries)
        if (guess == secret) {
            println("Congratulations!\nYou got it right on your ${numTries}th try.")
            return
        }
        val corrects = getCorrects(guess, secret)
        val swapped = getSwapped(guess, secret)
        printTry(numTries, guess, corrects, swapped)
    }
    println("You missed $MAX_TRIES attempts.")
}