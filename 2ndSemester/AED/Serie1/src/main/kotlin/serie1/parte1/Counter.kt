package serie1.parte1

fun counter(array: IntArray, k: Int, lower: Int, upper: Int): Pair<Int, Int> { // O(N)
    if (k > array.size) return Pair(0, 0)
    var lowerCount = 0
    var upperCount = 0
    var sum = 0

    for (i in 0 until k) sum += array[i]
    if (sum < lower) lowerCount++
    if (sum > upper) upperCount++

    for (i in k until array.size) {
        sum += array[i]
        sum -= array[i - k]
        if (sum < lower) lowerCount++
        if (sum > upper) upperCount++
    }
    return Pair(lowerCount,upperCount)
}

/*fun counter(array: IntArray, k: Int, lower: Int, upper: Int): Pair<Int, Int> { // O(N*K)
    var allLower: Int = 0
    var allupper: Int = 0


    for(i in 0 until array.size){
        var temp = 0
        if (i + k - 1 >= array.size) break
        for (j in 0..k - 1) {
            temp += array[i + j]
        }
        if (temp < lower) allLower++
        if (temp > upper) allupper++

    }
    return Pair(allLower,allupper)
}*/