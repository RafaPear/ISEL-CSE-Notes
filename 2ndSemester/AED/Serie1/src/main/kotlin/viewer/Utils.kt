package viewer

import pt.isel.canvas.WHITE

enum class Size(val arrSize: Int, val width: Int, val height: Int, val time: Long){
    SMALL(100,1500,500,20),
    MEDIUM(250,1500,500,10),
    LARGE(500,1500,500,10),
    HUGE(1000,1000,1000,5)
}

fun IntArray.toRectArray(viewer: Viewer): Array<Rect> {
    if (isEmpty()) return emptyArray<Rect>()
    var spacing = viewer.width / this.size

    val maxElement = this.max()
    val array = Array<Rect>(size) { Rect() }

    for (i in this.indices){
        val rectPos = Vec2(i*spacing, (viewer.height/size)*(maxElement - this[i]))
        val rectSize = Vec2(spacing, viewer.height)
        array[i] = Rect(rectPos, rectSize, WHITE)
    }

    return array
}

fun setSize(): Size{
    while (true){
        println("----------OPTIONS-----------")
        println("SMALL (S) -> 100 array elements")
        println("MEDIUM (M) -> 250 array elements")
        println("LARGE (L) -> 500 array elements")
        println("HUGE (H) -> 1000 array elements NOT RECOMMENDED")
        println("----------------------------")
        println("Chose the array test size: ")
        when (readln()) {
            in arrayOf("s", "S") -> return Size.SMALL;
            in arrayOf("m", "M") -> return Size.MEDIUM;
            in arrayOf("l", "L") -> return Size.LARGE;
            in arrayOf("h", "H") -> return Size.HUGE;
        }
    }
    return Size.SMALL;
}