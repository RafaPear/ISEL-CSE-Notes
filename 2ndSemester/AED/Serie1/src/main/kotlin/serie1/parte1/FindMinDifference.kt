package serie1.parte1

import kotlin.math.abs

fun findMinDifference(elem1: IntArray, elem2: IntArray): Int {
    //    throw UnsupportedOperationException()

    if (elem1.isEmpty() || elem2.isEmpty()) {
        return -1
    }

    var result = abs(elem1[0] - elem2[0])

    for (i in elem1.indices) {
        for (j in elem2.indices) {
            if (i > 0 && (elem2[j]) > (elem1[i - 1])) {
                val temp = abs(elem1[i] - elem2[j])
                if (temp < result) {
                    result = temp
                }
            }
            if (elem2[j] > elem1[i]) {
                break
            }
        }
    }
    return result
}

