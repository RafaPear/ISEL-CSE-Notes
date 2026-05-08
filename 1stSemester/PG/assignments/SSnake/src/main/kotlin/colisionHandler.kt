// *********************************************************
// Collision Detection Logic
// *********************************************************
// This file contains functions to handle collision detection in the game.
// It includes checks for whether the snake can move in various directions
// or is stuck between walls, ensuring smooth gameplay by managing
// the snake's interactions with obstacles.
// *********************************************************


// Returns true if the snake can continue to move forward
// without colliding with a wall (can move? yes)
fun Game.canMove(): Boolean {
    when (snake.direction){
        UP -> {
            return if (snake.position.minus(OffsetVector(0,CELL_SIZE)) in wall) false
            else if (snake.position.y <= 0 && Position(snake.position.x, snake.position.y + HEIGHT - CELL_SIZE) in wall ) false
            else true
        }
        RIGHT -> {
            return if (snake.position.plus(OffsetVector(CELL_SIZE,0)) in wall) false
            else if (snake.position.x >= WIDTH - CELL_SIZE && Position(0, snake.position.y) in wall) false
            else if (snake.position.x >= WIDTH && Position(snake.position.x - WIDTH + CELL_SIZE, snake.position.y) in wall) false
            else true
        }
        LEFT -> {
            return if (snake.position.minus(OffsetVector(CELL_SIZE,0)) in wall) false
            else if (snake.position.x <= 0 && Position(snake.position.x + WIDTH - CELL_SIZE, snake.position.y) in wall) false
            else true
        }
        DOWN -> {
            return if (snake.position.plus(OffsetVector(0,CELL_SIZE)) in wall) false
            else if (snake.position.y >= HEIGHT - CELL_SIZE && Position(snake.position.x, 0) in wall ) false
            else if (snake.position.y >= HEIGHT && Position(snake.position.x, snake.position.y - HEIGHT + CELL_SIZE) in wall ) false
            else true
        }
        else -> return true
    }
}

// Checks if the snake can change his direction
// to left without colliding with a wall
fun Game.canMoveLeft(): Boolean =
    snake.direction != RIGHT &&
            snake.position.minus(OffsetVector(CELL_SIZE,0)) !in wall &&
            snake.position.plus(OffsetVector(WIDTH - CELL_SIZE,0)) !in wall &&
            snake.position.minus(OffsetVector(CELL_SIZE,HEIGHT)) !in wall &&
            snake.position.plus(OffsetVector(-CELL_SIZE,HEIGHT)) !in wall

// Checks if the snake can change his direction
// to right without colliding with a wall
fun Game.canMoveRight(): Boolean =
    snake.direction != LEFT &&
            snake.position.plus(OffsetVector(CELL_SIZE,0)) !in wall &&
            snake.position.minus(OffsetVector(WIDTH - CELL_SIZE,0)) !in wall &&
            snake.position.plus(OffsetVector(CELL_SIZE,HEIGHT)) !in wall &&
            snake.position.minus(OffsetVector(-CELL_SIZE,HEIGHT)) !in wall

// Checks if the snake can change his direction
// to up without colliding with a wall
fun Game.canMoveUp(): Boolean =
    snake.direction != DOWN &&
            snake.position.minus(OffsetVector(0,CELL_SIZE)) !in wall &&
            snake.position.plus(OffsetVector(0,HEIGHT - CELL_SIZE)) !in wall &&
            snake.position.minus(OffsetVector(WIDTH,CELL_SIZE)) !in wall &&
            snake.position.plus(OffsetVector(WIDTH,-CELL_SIZE)) !in wall

// Checks if the snake can change his direction
// to the down without colliding with a wall
fun Game.canMoveDown(): Boolean =
    snake.direction != UP &&
            snake.position.plus(OffsetVector(0,CELL_SIZE)) !in wall &&
            snake.position.minus(OffsetVector(0,HEIGHT - CELL_SIZE)) !in wall &&
            snake.position.plus(OffsetVector(WIDTH,CELL_SIZE)) !in wall &&
            snake.position.minus(OffsetVector(WIDTH,-CELL_SIZE)) !in wall

// Checks if the snake can is stuck between 3
// walls and has no more movement options
fun Game.isStuck(): Boolean{
    if (snake.direction == LEFT || snake.direction == RIGHT) {
        if (!canMoveDown() && !canMoveUp() && !canMove()) return true
    }
    else if (snake.direction == UP || snake.direction == DOWN) {
        if (!canMoveRight() && !canMoveLeft() && !canMove()) return true
    }
    return false
}