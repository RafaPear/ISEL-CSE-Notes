import pt.isel.canvas.*

/**The Walls data class is responsible for the management of available cells and wall creation and storing.*/
data class Walls(
    val filledPlaces: MutableList<Vector2>,
    val availablePlaces: MutableList<Vector2>,
    val screen: Canvas
){
    /**Updates the [availablePlaces] subtracting the [filledPlaces] values. */
    fun updateWall() {
        for (i in filledPlaces)
            availablePlaces -= i
    }

    /**Adds a new random wall from the [availablePlaces] to the [filledPlaces], verifying if this new
     * random wall is not in the snake or apple.*/
    fun newWall(snake: Snake, apple: Apple, debug: Boolean){
        updateWall()
        while(availablePlaces.size-snake.totalPos(0).size-1 != 0){
            val i = availablePlaces.indices.random()

            if (availablePlaces[i] !in snake.totalPos(0) && availablePlaces[i] != apple.pos){
                if (debug) screen.drawRect(availablePlaces[i],CELLS.square,2,GREEN)
                filledPlaces += availablePlaces[i]
                availablePlaces -= availablePlaces[i]
                break
            }
            if (debug) screen.drawRect(availablePlaces[i],CELLS.square,2,RED)
        }
    }

    /** Draws all the walls in [filledPlaces] to the screen*/
    fun draw(){
        for (pos in filledPlaces) {
            screen.drawImage("bricks|0,0,118,118", pos, CELLS.square)
        }
    }
}