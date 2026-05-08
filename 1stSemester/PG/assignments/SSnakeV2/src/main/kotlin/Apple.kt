import pt.isel.canvas.*

/**
 * The Apple data class calculates the position [Vector2] for every new apple
 * that needs to be generated. To generate an apple use the function [newApple]
 * and do draw it use the function [draw].
 */

data class Apple(
    val pos: Vector2? = null,
    val screen: Canvas
){
    /**Generates a new apple on an available cell.*/
    fun newApple(game: Game): Apple {
        var tempPos = pos
        while(game.walls.availablePlaces.size-game.snake.totalPos(0).size != 0){
            val i = game.walls.availablePlaces.indices.random()

            if (game.walls.availablePlaces[i] !in occupiedCells(game) &&
                game.walls.availablePlaces[i] !== game.snake.getNextPosition()){
                tempPos = game.walls.availablePlaces[i]
                break
            }
        }
        return Apple(tempPos, screen)
    }

    /**Draws the apple on the screen.*/
    fun draw(){
        if (pos != null) {
            screen.drawImage("snake|0,192,64,64", pos, CELLS.square)
        }
    }
}
