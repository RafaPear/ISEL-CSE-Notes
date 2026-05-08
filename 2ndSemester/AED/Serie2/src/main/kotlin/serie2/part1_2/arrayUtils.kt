package serie2.part1_2

/**
 * Função que calcula o menor elemento de um heap máximo
 * Complexidade: O(n/2) - O(n)
 * @param maxHeap heap máximo
 * @param heapSize tamanho do heap
 * @return menor elemento do heap máximo
 * */
fun minimum(maxHeap: Array<Int>, heapSize: Int): Int {
    val l = (heapSize / 2)
    var min = maxHeap[l]

    for ( i in l + 1 until heapSize){
        if (maxHeap[i] < min) min = maxHeap[i]
    }
    return min
}
