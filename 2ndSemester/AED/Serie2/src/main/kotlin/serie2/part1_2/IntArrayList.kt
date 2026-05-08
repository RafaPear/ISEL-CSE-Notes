package serie2.part1_2

/**
 * Classe para uma lista de inteiros com tamanho fixo
 * Complexidade: O(1) para todas as operações
 * @param size tamanho da lista
 * @constructor Cria uma lista de inteiros com tamanho fixo
 * @property size tamanho da lista
 * @property list lista de inteiros
 * @property count número de elementos na lista
 * @property first índice do primeiro elemento da lista
 * @property last índice do último elemento da lista
 * @property backpack valor a ser adicionado a todos os elementos da lista
 * */
class IntArrayList : Iterable<Int> {
    var size: Int
    private var list: IntArray
    private var count = 0
    private var first = 0
    private var last = 0
    private var backpack = 0

    constructor(size: Int) {
        this.size = size
        this.list = IntArray(size)
    }

    /**
     * Adiciona um elemento à lista
     * Complexidade: O(1)
     *
     * @param x elemento a ser adicionado
     * @return true se o elemento foi adicionado, false se a lista está cheia
     * */
    fun append(x:Int):Boolean {
        if (count >= size) return false
        list[last] = x - backpack
        last = (last + 1) % size
        count++
        return true
    }

    /**
     * Retorna o elemento no índice n da lista. Retorna null se o índice for inválido
     *
     * Complexidade: O(1)
     * @param n índice do elemento a ser retornado
     * @return elemento no índice n da lista ou null se o índice for inválido
     * */
    fun get(n:Int):Int?  {
        if (n < 0 || n >= count) return null
        return list[(n + first) % size] + backpack
    }

    /**
     * Adiciona um valor a todos os elementos da lista
     * Complexidade: O(1) pois não percorre a lista
     * @param x valor a ser adicionado
     * */
    fun addToAll(x:Int)   {
        backpack += x
    }

    /**
     * Remove o primeiro elemento da lista
     * Complexidade: O(1)
     * @return true se o elemento foi removido, false se a lista está vazia
     * */
    fun remove():Boolean {
        if (count <= 0) return false
        first = (first + 1) % size
        count--
        return true
    }

    /**
     * Retorna o iterador da lista
     * Complexidade: O(1)
     * @return iterador da lista
     * */
    override fun iterator(): IntIterator {
        return list.iterator()
    }
}
