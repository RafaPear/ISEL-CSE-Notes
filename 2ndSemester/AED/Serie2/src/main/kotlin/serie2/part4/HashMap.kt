package serie2.part4
import kotlin.math.absoluteValue

class HashMap<K, V> (initialCapacity: Int = 16, val loadFactor: Float = 0.75f): MutableMap<K, V> {
    private class HashNode<K, V>(
        override val key: K, override var value: V,
        var next: HashNode<K, V>? = null
    ) : MutableMap.MutableEntry<K, V> {
        var hc = key.hashCode()
        override fun setValue(newValue: V): V {
            val oldValue = value
            value = newValue
            return oldValue
        }
    }
    
    private var table: Array<HashNode<K, V>?> = arrayOfNulls(initialCapacity)

    override var size = 0
    override var capacity: Int = initialCapacity

    //devolve o valor correspondente à chave ou null se não existir

    /**
     * Retorna o valor associado à chave especificada.
     *
     * @param key A chave cuja associação deve ser retornada.
     * @return O valor associado à chave especificada, ou null se a chave não estiver presente na tabela.
     * */
    override operator fun get(key: K): V? {
        val index = (key.hashCode() % capacity).absoluteValue
        var current_node = table[index]

        if (current_node == null) return null

        while (current_node != null && current_node.key != key){
            current_node = current_node.next
        }
        return current_node?.value
    }

    /**
     * Insere uma nova associação chave-valor na tabela hash.
     * Se a chave já existir, o valor associado é atualizado.
     *
     * @param key A chave a ser inserida ou atualizada.
     * @param value O valor a ser associado à chave.
     * @return O valor anterior associado à chave, ou null se a chave não existia antes.
     */
    override fun put(key: K, value: V): V? {
        val index = (key.hashCode() % capacity).absoluteValue
        var current = table[index]

        while(current != null){
            if (current.key == key){
                return current.setValue(value)
            }
            current = current.next
        }

        current = table[index]

        val newNode = HashNode(key,value)
        newNode.next = current

        table[index] = newNode
        size++
        expand()
        return null
    }

    /**
     * Insere uma nova associação chave-valor na tabela hash, utilizando um lambda para resolver colisões.
     * Se a chave já existir, o valor associado é atualizado utilizando o lambda fornecido.
     *
     * @param key A chave a ser inserida ou atualizada.
     * @param value O valor a ser associado à chave.
     * @param add Um lambda que define como resolver colisões.
     * @return O valor anterior associado à chave, ou null se a chave não existia antes.
     */
    fun <T> putList(key: K, value: T, add: V?.(newValue: T) -> V): V? {
        val index = (key.hashCode() % capacity).absoluteValue
        var current = table[index]

        while(current != null){
            if (current.key == key){
                val oldValue = current.value
                current.value = current.value.add(value)
                return oldValue
            }
            current = current.next
        }

        current = table[index]

        val newNode = HashNode(key,null.add(value))
        newNode.next = current

        table[index] = newNode
        size++
        expand()
        return null
    }
    //se a condição for verdadeir
    //cria um HashMap novo com o dobro da capacidade
    //precorre o Map atual e insere os elementos no novo Map
    //"subestitui" o Map antigo pelo novo

    /**
     * Expande a tabela hash se o tamanho atual exceder o fator de carga.
     * Cria uma nova tabela com o dobro da capacidade e transfere os elementos existentes.
     */
    private fun expand() {
        if (size >= capacity*loadFactor) {
            capacity = capacity*2
            val newMap = HashMap<K, V>(capacity)

            for(ele in this){
                newMap.put(ele.key, ele.value)
            }
            table = newMap.table
        }
    }
    //    override fun iterator(): Iterator<MutableMap.MutableEntry<K, V>>{
//        val list = mutableListOf<MutableMap.MutableEntry<K, V>>()
//
//        for (ele in table) {
//            var node = ele
//            while(node != null) {
//                list.add(node)
//                node = node.next
//            }
//        }
//        return list.iterator()
//    }

    /**
     * Remove a associação chave-valor correspondente à chave especificada.
     *
     * @param key A chave cuja associação deve ser removida.
     * @return O valor associado à chave removida, ou null se a chave não estava presente na tabela.
     */
    override fun iterator(): Iterator<MutableMap.MutableEntry<K, V>> {
        return object : Iterator<MutableMap.MutableEntry<K, V>> {
            var table_index = 0
            var current_Node: HashNode<K, V>? = null

            init {//inicia a iteração num Node valido, diferente de null
                current_Node = table[0] ?: nextIndice()
            }
            //procura o proximo Node valido na tabela, ou seja != null
            private fun nextIndice(): HashNode<K, V>? {
                var next: HashNode<K, V>? = null
                for(i in table_index +1 until  table.size){
                    if (table[i] != null){
                        next = table[i]
                        table_index = i
                        break
                    }
                }
                return next
            }

            override fun hasNext(): Boolean = current_Node != null

            //retorna o Node atual (se este for null manda uma Exception),
            //e busca o próximo Node se este for null chama nextIndice
            override fun next(): MutableMap.MutableEntry<K, V> {
                val result = current_Node ?: throw NoSuchElementException()
                current_Node = current_Node?.next ?: nextIndice()
                return result
            }
        }
    }
}