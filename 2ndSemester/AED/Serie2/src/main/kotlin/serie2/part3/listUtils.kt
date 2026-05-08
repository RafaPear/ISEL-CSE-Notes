package serie2.part3

import java.util.Comparator

class Node<T> {
    var value: T
    var next: Node<T>?
    var previous: Node<T>?

    /**
     * Construtor para nó sentinela
     * */
    constructor(){
        this.value = Any() as T
        this.next = this
        this.previous = this
    }

    /**
     * Constructor para nó normal
     * */
    constructor(value: T){
        this.value = value
        this.next = null
        this.previous = null
    }

    /**
     * Constructor para nó normal
     * */
    constructor(value: T, next: Node<T>?, previous: Node<T>?){
        this.value = value
        this.next = next
        this.previous = previous
    }

    /**
     * Insere um novo nó após o nó atual e faz as devidas atualizações aos endereços
     * @param value valor a ser inserido
     * */
    fun insertionAfter(value: T) {
        next?.previous = Node(value,next,this)
        next = next?.previous
    }

    /**
     * Remove o nó atual da lista, atualizando os endereços dos nós adjacentes
     * */
    fun remove() {
        this.previous?.next = this.next
        this.next?.previous = this.previous
    }

    /**
     * Verifica se o nó atual é um nó sentinela (ou seja, se é o único nó na lista)
     * @return true se o nó atual for um nó sentinela, false caso contrário
     * */
    fun isEmptyNode(): Boolean = this.next === this && this.previous === this

    /**
     * Junta duas listas duplamente ligadas (sourceLeft e sourceRight) na lista atual
     * @param sourceLeft lista da esquerda
     * @param sourceRight lista da direita
     * */
    fun mergeTwoToThis(sourceLeft: Node<T>, sourceRight: Node<T>) {
        if (!sourceLeft.isEmptyNode()) {
            sourceRight.next?.previous = sourceLeft.previous
            sourceLeft.previous?.next = sourceRight.next

            sourceRight.previous?.next = this
            previous = sourceRight.previous

            sourceLeft.next?.previous = this
            next = sourceLeft.next
        } else {
            previous = sourceRight.previous
            sourceRight.previous?.next = this
        }
    }
}

// ---------- Versão 1 (mais complexa) ----------
// Nesta versão o processo é criar duas listas duplamente
// ligadas com sentinela, uma para pares e outra para ímpares.
// A função 'splitEvensAndOdds' percorre a lista original e
// insere os elementos pares na lista de pares e os ímpares
// na lista de ímpares. No final, as duas listas são unidas
// na lista original, com os pares à esquerda e os ímpares à
// direita.

//fun splitEvensAndOdds(list: Node<Int>) {
//    var current: Node<Int>? = list.next
//    var evens: Node<Int> = Node()
//    var odds: Node<Int> = Node()
//
//    if (current?.isEmptyNode() ?: true) return
//
//    while (current != list && current != null) {
//        if (current.value % 2 == 0) {
//            evens.insertionAfter(current.value)
//        } else {
//            odds.insertionAfter(current.value)
//        }
//        current = current.next
//    }
//
//    list.mergeTwoToThis(evens, odds)
//}

// ---------- Versão 2 (mais simples) ----------
// Nesta versão, a função `splitEvensAndOdds` percorre a lista
// original e, para cada elemento par, remove-o da lista
// original e o insere no início da lista. No final, a lista
// original contém todos os elementos pares à esquerda e os
// ímpares à direita.

/**
 * Função que separa os números pares e ímpares de uma lista duplamente ligada
 * Complexidade: O(n), onde n é o tamanho da lista
 * @param list lista a ser separada
 * */
fun splitEvensAndOdds(list: Node<Int>) {
    var current = list.next
    val first = list

    if (list.isEmptyNode()) return

    while (current != null && current != list) {
        if (current.value % 2 == 0) {
            first.insertionAfter(current.value)
            current.remove()
        }
        current = current.next
    }
}

/**
 * Função que retorna a interseção de duas listas duplamente ligadas
 * Complexidade: O(n + m), onde n e m são os tamanhos das listas
 * @param list1 primeira lista
 * @param list2 segunda lista
 * @param cmp comparador para comparar os valores das listas
 * @return nova lista com os elementos em comum entre as duas listas
 * */
fun <T> intersection(list1: Node<T>, list2: Node<T>, cmp: Comparator<T>): Node<T>? {
    var newNode = Node<T>()
    val tail = newNode
    var current1 = list1.previous
    var current2 = list2.previous

    while (current1 != list1 && current2 != list2) {
        val compasion = cmp.compare(current1?.value, current2?.value)
        if ( compasion == 0) {
            if (current1?.value != current1?.next?.value ) {
                newNode.previous = Node(current1!!.value)
                newNode.previous?.next = newNode
                newNode = newNode.previous!!
                val temp1 = current1.previous
                val temp2 = current2?.previous
                current1.remove()
                current2?.remove()
                current1 = temp1
                current2 = temp2
            }
        } else {
            if (current1 == list1) {
                current2 = current2?.previous
            } else if(current2 == list2) {
                current1 = current1?.previous
            }else {
                if (compasion > 0) {
                    current1 = current1?.previous
                } else {
                    current2 = current2?.previous
                }
            }
        }
    }


    if (newNode != tail ) {
        tail.previous?.next = null
        return newNode
    } else
        return null
}


