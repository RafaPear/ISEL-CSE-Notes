// Trabalho 1 de PG-LEIC15D Ano Letivo 24/25
// Professor: Nuno Leite
// Grupo I: Francisco Mendes, Gustavo Costa e Rafael Pereira

// Generates a random a String of 4 different Chars
fun generateSecret(): String {
    var returnStr = ""
    while (returnStr.length < SIZE_POSITIONS) {
        val randomChar = COLORS.random()
        if (randomChar !in returnStr) {
            returnStr += randomChar
        }
    }
    return returnStr
}

// Prompts the user to input their guess
// for the current attempt number and
// returns the guess as a string. the attempt
// input (numTries) is probably just for cosmetic printing
// purposes. the function must only allow an input equal to 4 Chars
fun readGuess(tries: Int): String {
    while (true){
        when (tries) {
            1 -> print("1st attempt: ")
            2 -> print("2nd attempt: ")
            3 -> print("3rd attempt: ")
            else -> print("${tries}th attempt: ")
        }

        var readValue: String = readln()

        if (FIRST_COLOR in 'a'..'z') readValue = toLower(readValue)
        else if (FIRST_COLOR in 'A'..'Z') readValue = toUpper(readValue)

        //if (FIRST_COLOR in 'a'..'z')
        if (!readCheck(readValue)) {
            println("Invalid attempt!")
        } else {
            return readValue
        }
    }
}

// Checks if a given string has repeated chars
// and returns True if any matches are found.
fun checkRepeated(str: String): Boolean {
    for (i in 0..<str.length)
        for (j in 0..<str.length)
            if (i != j)
                if (str[i] == str[j])
                    return true
    return false
}

// Checks if a String Argument (guess: String)
// matches all the requirements to be a
// valid input Guess. Returns true
// if the argument passes the check,
// false if it does not.
fun readCheck(guess: String): Boolean {
    if (guess.length != SIZE_POSITIONS)
        return false

    if (checkRepeated(guess)) return false

    for (eachChar in guess){
        if (eachChar !in COLORS)
            return false
    }
    return true
}

// Converts the given String to Capital if small
// to small if Capital
// (eg: input - abCD -> output - ABCD).
fun toUpper(strInput: String): String {
    var returnString = ""

    for (char in strInput){
        if (char in 'a'..'z') {
            returnString += char - 32
        } else if (char in 'A'..'Z'){
            returnString += char
        }
    }
    return returnString
}

fun toLower(strInput: String): String {
    var returnString = ""

    for (char in strInput){
        if (char in 'A'..'Z') {
            returnString += char + 32
        } else if (char in 'a'..'z'){
            returnString += char
        }
    }
    return returnString
}

// Checks how many guessed Chars are in the
// same position as the answer.
fun getCorrects(guess: String, answer: String): Int{
    var tempReturn = 0
    for (i in 0..<SIZE_POSITIONS)
        if (guess[i] == answer[i])
            tempReturn++
    return tempReturn
}

// Checks how many guessed Chars are in
// the answer but on the wrong place.
fun getSwapped(guess: String, answer: String): Int {
    var tempReturn = 0
    for (i in 0..<SIZE_POSITIONS)
        if (guess[i] in answer && guess[i] != answer[i])
            tempReturn++
    return tempReturn
}

// Prints the results in a nice and readable way
// shown as (example): "1st: ADFC -> 1C + 3T".
// the syntax is presented as:
// (numTries): (guess) -> (corrects) + (swapped)
fun printTry(numTries: Int, guess: String, corrects: Int, swapped: Int){
    var strOut: String = when(numTries) {
        1 -> "1st: "
        2 -> "2nd: "
        3 -> "3rd: "
        else -> "${numTries}th: "
    }

    strOut += "$guess -> ${corrects}C + ${swapped}T"
    println(strOut)
}