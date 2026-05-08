package serie2.problema

/**
 * Classe que representa um par único de elementos do tipo genérico [T].
 * Permite adicionar até dois valores distintos e operar sobre eles.
 */
class UniquePair<T>(var x: T? = null, var y: T? = null) {

    /**
     * Propriedade que indica se o par está completo, ou seja,
     * ambos os elementos [x] e [y] não são nulos.
     */
    val full: Boolean
        get() = x != null && y != null

    /**
     * Tenta adicionar um valor [value] ao par.
     * Retorna `true` se o valor foi adicionado com sucesso,
     * ou `false` se o par já está cheio ou o valor já existe.
     */
    fun add(value: T): Boolean {
        if (full) return false // Par já completo, não adiciona

        if (value == x || value == y) return false // Valor já presente, não adiciona
        else {
            // Adiciona em x se estiver vazio, senão em y
            if (x == null) x = value else y = value
        }
        return true
    }

    /**
     * Remove um valor [value] do par.
     * Retorna `true` se o valor foi removido, ou `false` se não estava presente.
     */
    fun remove(value: T): Boolean {
        return when (value) {
            x -> {
                x = null
                true
            }
            y -> {
                y = null
                true
            }
            else -> false
        }
    }

    /**
     * Verifica se um elemento [ele] está presente no par.
     * Retorna `true` se [ele] for igual a [x] ou [y], senão `false`.
     */
    fun contains(ele: T): Boolean {
        return (ele == this.x || ele == this.y)
    }

    fun isSimilar(pair: UniquePair<T>): Boolean {
        return this.contains(pair.x as T) && this.contains(pair.y as T)
    }
}
