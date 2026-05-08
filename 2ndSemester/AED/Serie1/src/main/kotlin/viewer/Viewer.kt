package viewer

import pt.isel.canvas.*
import kotlin.system.exitProcess

class Viewer(size: Size, bgr: Int) {
    var time: Long = size.time
    val width = size.width
    val height = size.height
    val screen = Canvas(width, height, bgr)

    init {
        onFinish {
            exitProcess(0)
        }
    }

    fun update(clear: Boolean = false, f: (Canvas) -> Unit) {
        if (clear)
            screen.erase()
        f(screen)
    }

    fun load(intArr: IntArray, time: Long = this.time){
        this.update(true) { it: Canvas ->
            val arr = intArr.toRectArray(this)
            for (i in arr.indices) {
                arr[i].draw(this)
            }
        }
        Thread.sleep(time)
    }

    fun insert(intArr: IntArray, index: Int, time: Long = this.time) {
        this.update(true) { it: Canvas ->
            val arr = intArr.toRectArray(this)
            for (i in arr.indices) {
                arr[i].draw(this)
            }
            arr[index].draw(this, GREEN, Rect(arr[index].pos, arr[index].size, GREEN))
            Thread.sleep(time*2)
        }
    }

    fun check(intArr: IntArray, from: Int = 0, until: Int = intArr.indices.last(), time: Long = this.time){
        this.update(true) { it: Canvas ->
            val arr = intArr.toRectArray(this)
            for (i in arr){
                i.draw(this)
            }
            for (i in from+1..until){
                Thread.sleep(time)
                if (intArr[i-1] < intArr[i])
                    arr[i-1].draw(this,GREEN)
                else {
                    for (j in i-1 until arr.size) {
                        arr[j].draw(this, RED)
                    }
                    break
                }
                arr[i].draw(this,GREEN)
            }
        }
    }
}