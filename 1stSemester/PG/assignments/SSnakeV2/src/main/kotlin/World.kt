import pt.isel.canvas.*

/**
 * A Vector2 is a two-dimensional class that contains an [x] value
 * and an [y] value. Is supports operations with constants or Vectors like
 * addition, subtraction, multiplication or division.
 */

data class Vector2(var x: Int, var y: Int) {
    operator fun plus(a: Int) = Vector2(x + a, y + a)
    operator fun plus(a: Vector2) = Vector2(x + a.x, y + a.y)

    operator fun minus(a: Int) = Vector2(x - a, y - a)
    operator fun minus(a: Vector2) = Vector2(x - a.x, y - a.y)

    operator fun times(a: Int) = Vector2(x * a, y * a)
    operator fun times(a: Vector2) = Vector2(x * a.x, y * a.y)

    operator fun div(a: Int) = Vector2(x / a, y / a)
    operator fun div(a: Vector2) = Vector2(x / a.x, y / a.y)

    operator fun rem(a: Int) = Vector2(x % a, y % a)
    operator fun rem(a: Vector2) = Vector2(x % a.x, y % a.y)

    /** Wraps the vector values to the screen bounds. Thanks to Artur Nogueira, Group 12 for the idea.*/
    fun wrap(): Vector2 = (this + CELLS.normalize) % CELLS.normalize
}

/**Returns the sprite for the snake head given a [direction].*/
fun getSpriteHead(direction: Direction): Vector2{
   return when (direction) {
        Direction.UP -> Vector2(192,0)
        Direction.DOWN -> Vector2(256,64)
        Direction.LEFT -> Vector2(192,64)
        Direction.RIGHT -> Vector2(256,0)
    }
}

/**Returns the sprite for the snake tail given a [direction].*/
fun getSpriteTail(direction: Direction): Vector2{
    return when (direction) {
        Direction.UP -> Vector2(3,2).times(64)
        Direction.DOWN -> Vector2(4,3).times(64)
        Direction.LEFT -> Vector2(3,3).times(64)
        Direction.RIGHT -> Vector2(4,2).times(64)
    }
}

/**Returns the sprite for the snake body part given a current direction and previous direction.*/
fun getSpriteBody(myDir: Direction, previousDir: Direction): Vector2{
    return when (myDir) {
        Direction.UP -> {
            if (previousDir == Direction.LEFT) Vector2(0,64)
            else if (previousDir == Direction.RIGHT) Vector2(128,128)
            else Vector2(128,64)
        }
        Direction.DOWN -> {
            if (previousDir == Direction.LEFT) Vector2(0,0)
            else if (previousDir == Direction.RIGHT) Vector2(128,0)
            else Vector2(128,64)
        }
        Direction.LEFT -> {
            if (previousDir == Direction.UP) Vector2(128,0)
            else if (previousDir == Direction.DOWN) Vector2(128,128)
            else Vector2(64,0)
        }
        Direction.RIGHT -> {
            if (previousDir == Direction.UP) Vector2(0,0)
            else if (previousDir == Direction.DOWN) Vector2(0,64)
            else Vector2(64,0)
        }
    }
}

/**
 * Enumerate containing the 4 basic two-dimensional directions and their offset values:
 * [UP], [DOWN], [LEFT] and [RIGHT].
 */

enum class Direction(val offset: Vector2){
    UP(Vector2(0,-CELLS.size)),
    DOWN(Vector2(0,CELLS.size)),
    LEFT(Vector2(-CELLS.size,0)),
    RIGHT(Vector2(CELLS.size,0))
}

/**
 * Creates the Screen class for a cleaner code. The [grid] parameter is a
 * position class that contains an amount of cells on the x-axis and
 * on the y-axis. The [size]
 * parameter is the pixel size.
 */

data class Cells(val grid: Vector2, val size: Int) {
    val normalize = grid.div(2).times(size*2) // Pixel coordinates
    val center = normalize.div(2)
    val square = Vector2(size,size) // A Cell

    /**Creates the Canvas window with the specified grid and size values. */
    fun createWindow() = Canvas(normalize.x,normalize.y + size*2,BLACK)

    /** Returns the total usable cells list of positions*/
    fun totalCells(): List<Vector2>{
        var tempList = emptyList<Vector2>()
        for(x in 0 until grid.x-1)
            for(y in 0 until grid.y-1)
                tempList += Vector2(x, y).times(size)
        return tempList
    }
}

/**Makeshift button class responsible for drawing and checking if a mouse event happened inside the button bounds.
 * The [thickness] and [color] can be ignored, if so, they will be set to 0 and WHITE respectively.*/
data class Button(
    val pos: Vector2,
    val size: Vector2,
    val screen: Canvas,
    val name: String,
    val textColor: Int,
    val thickness: Int? = null,
    val color: Int? = null
){
    fun draw(){
        val newThickness: Int = thickness ?: 0
        val textSize: Vector2 = Vector2(((name.length*2+size.x)/name.length).toInt(),(name.length+size.y/name.length+CELLS.size/3).toInt())
        val newColor: Int = color ?: WHITE
        screen.drawRect(pos,size, newThickness,newColor)
        screen.drawText(pos+textSize,name,textColor, 20)
    }

    fun isClicked(mouseEvent: MouseEvent): Boolean{
        return (mouseEvent.x >= pos.x &&
            mouseEvent.y >= pos.y &&
            mouseEvent.x <= pos.x + size.x &&
            mouseEvent.y <= pos.y + size.y)
    }
}



