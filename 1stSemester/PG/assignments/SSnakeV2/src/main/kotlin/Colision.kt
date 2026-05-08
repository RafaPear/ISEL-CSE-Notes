
/**Returns a list of type [Vector2] with the positions occupied by the snake and the walls.*/
fun occupiedCells(game: Game) =
    if(game.grow > 0)
        game.snake.totalPos(1) + game.walls.filledPlaces
    else game.snake.totalPos(1) - game.snake.totalPos(1).last() + game.walls.filledPlaces


/**Tests if the next position of the snake head is a wall or her body. If the snake isn't growing
 * it ignores the tail.*/
fun willCollide(game: Game): Boolean {
    return (game.snake.getNextPosition() in occupiedCells(game)) &&
            !(game.snake.getNextPosition() == game.snake.totalPos(1).last() && game.grow == 0)
}

/**Tests if snake can change her direction to the new direction.
 * Returns false if the snake new direction will collide. */
fun canChangeDirection(newDirection: Direction, game: Game): Boolean {
    val tempGame = game.copy(snake = game.snake.move(game))
    return ((game.snake.body[0].pos + newDirection.offset).wrap() !in occupiedCells(game)) ||
            ((game.snake.body[0].pos + newDirection.offset).wrap() == game.snake.totalPos(1).last() && game.grow == 0)
}

/**Tests if the snake cant move at all.*/
fun isStuck(game: Game): Boolean{
    for (dir in Direction.entries){
        if (canChangeDirection(dir,game)) return false
    }
    return true
}

/**Tests if the snake next position has an apple.*/
fun checkApple(game: Game): Boolean {
    return game.snake.getNextPosition().wrap() == game.apple.pos
}
