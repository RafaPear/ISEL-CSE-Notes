import pt.isel.canvas.Canvas

/**
 * Class that represents every cell of the snake's body,
 * containing a position [Vector2] and a direction [Direction].
 */

data class SnakePart(val pos: Vector2, val direction: Direction)

/**
 * The Snake class deals with every [SnakePart] contained in a
 * list and calculates how to draw it on the screen and how to move it.
 */

data class Snake(val body: List<SnakePart>, val screen: Canvas){

    /** Returns all the snake positions from a given starting point ([start]).*/
    fun totalPos(start: Int): List<Vector2>{
        var tempList = emptyList<Vector2>()
        for(i in start..<body.size)
            tempList += body[i].pos
        return tempList
    }

    /**
     * Sets the head direction so the rest of the body can calculate where to look.
     */
    fun setDirection (direction: Direction): Snake {
        var tempBody = emptyList<SnakePart>()
        for(i in 0 until body.size){
            tempBody += if(i == 0) SnakePart(body[i].pos, direction)
            else body[i]
        }
        return Snake(tempBody,screen)
    }

    /**
     * Calculates what sprite a certain part of the snake should be,
     * depending on the direction of the current sprite (i) in relation
     * to the previous sprite (i+1).
     */
    fun getSprite(i: Int): Vector2 {
        return if (i == 0) getSpriteHead(body[i].direction)
        else if (i == body.lastIndex) getSpriteTail(body[i].direction)
        else getSpriteBody(body[i].direction,body[i+1].direction)
    }

    /**
     * Draws the snake on the screen.
     */
    fun draw(){
        for(p in 0..body.indices.last){
            val sprite = getSprite(p)
            screen.drawImage("snake|${sprite.x},${sprite.y},64,64", body[p].pos,CELLS.square)
        }
    }

    /** Returns the next position of the snake head.*/
    fun getNextPosition(): Vector2{
        return (body[0].pos + body[0].direction.offset).wrap()
    }

    /**Returns a new snake 1 cell forward, if possible.*/
    fun move (game: Game): Snake {
        if (willCollide(game)) return this
        var tempBody = emptyList<SnakePart>()
        for(i in 0 until body.size) {
            tempBody += if (i == 0) {
                SnakePart(getNextPosition(), body[i].direction)
            } else SnakePart(Vector2(body[i - 1].pos.x, body[i - 1].pos.y), body[i - 1].direction)
        }
        return Snake(tempBody,screen)
    }

    /** Returns a new snake with the last body part duplicated.*/
    fun newPart(): Snake{
        var tempBody = body
        tempBody += body.last()
        return Snake(tempBody,screen)
    }
}