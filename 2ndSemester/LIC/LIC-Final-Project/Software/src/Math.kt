import kotlin.countOneBits

/**
 * Função que retorna o valor da [base] elevada a [exponent]
 *
 * @param base base
 * @param exponent expoente
 * @return [Int] Resultado
 * */
fun pow(base: Int, exponent: Int): Int{
    var temp = 1
    for (i in 1..exponent){
        temp = temp * base
    }
    return temp
}

/**
 * Função que transforma o inteiro para um valor
 * com o bit de índice [this] a '1'.
 * Ex: 3.toBitPlace() = 0b0000_1000
 * Ex: 4.toBitPlace() = 0b0001_0000
 *
 * @return [Int] Resultado
 * */
fun Int.toBitPlace(): Int{
    return pow(2, this)
}

/**
 * Função que verifica se o bit de índice [index] do inteiro [this]
 * é igual a '1'.
 * Ex: 0b0000_1000.isBit(3) = true
 * Ex: 0b0000_1000.isBit(2) = false
 *
 * @param index índice do bit a verificar
 * @return [Boolean] Resultado
 * */
fun Int.isBit(index: Int) : Boolean{
    return and(1.shl(index)).countOneBits() == 1
}

/**
 * Função que transforma o booleano [this] em inteiro.
 * Ex: true.toInt() = 1
 * Ex: false.toInt() = 0
 *
 * @return [Int] Resultado
 * */
fun Boolean.toInt(): Int{
    return if (this) 1 else 0
}

/**
 * Função que transforma o booleano [this] em inteiro
 * e desloca-o para a esquerda [idx] vezes.
 * Ex: true.toBin(3) = 0b0000_1000
 * Ex: false.toBin(2) = 0b0000_0000
 *
 * @param idx número de deslocações
 * @return [Int] Resultado
 * */
fun Boolean.toBin(idx: Int): Int {
    return (this.toInt())shl(idx)
}
