// *********************************************************
// Snake Class and Movement Logic
// *********************************************************
// This file defines the Snake class and its related functionality,
// including methods for moving the snake, updating its direction,
// and determining the tail's position. It provides the core logic
// for controlling the snake's behavior within the game.
// *********************************************************


// Snake class containing the position of the
// snake's head (Top left corner) and the direction
// (defined from 0 to 3, Up (0), Right (1), Left(2), Down(3))
data class Snake(val position: Position, val direction: Int)

// Changes the direction of the snake
fun Snake.setDirection(direction: Int): Snake = Snake(position, direction)

// Moves the snake forward in relation to the direction
fun Snake.move(): Snake{
    when(direction) {
        UP -> {
            return if (position.y < 0)
                return Snake(Position(position.x, HEIGHT - CELL_SIZE*2), direction)
            else if (position.x < 0)
                return Snake(Position(WIDTH- CELL_SIZE, position.y - CELL_SIZE), direction)
            else if (position.x >= WIDTH)
                return Snake(Position(0, position.y - CELL_SIZE), direction)
            else Snake(Position(position.x, position.y - CELL_SIZE), direction)
        }
        RIGHT -> {
            return if (position.x >= WIDTH)
                Snake(Position(CELL_SIZE, position.y), direction)
            else if (position.y < 0)
                return Snake(Position(position.x + CELL_SIZE, HEIGHT - CELL_SIZE), direction)
            else if (position.y >= HEIGHT)
                return Snake(Position(position.x + CELL_SIZE, 0), direction)
            else Snake(Position(position.x+CELL_SIZE,position.y), direction)
        }
        LEFT -> {
            return if (position.x < 0)
                Snake(Position(WIDTH - CELL_SIZE*2,position.y), direction)
            else if (position.y < 0)
                return Snake(Position(position.x - CELL_SIZE, HEIGHT - CELL_SIZE), direction)
            else if (position.y >= HEIGHT)
                return Snake(Position(position.x - CELL_SIZE, 0), direction)
            else Snake(Position(position.x-CELL_SIZE,position.y), direction)
        }
        DOWN -> {
            return if (position.y >= HEIGHT)
                Snake(Position(position.x, CELL_SIZE), direction)
            else if (position.x < 0)
                return Snake(Position(WIDTH- CELL_SIZE, position.y + CELL_SIZE), direction)
            else if (position.x >= WIDTH)
                return Snake(Position(0, position.y + CELL_SIZE), direction)
            else Snake(Position(position.x,position.y+CELL_SIZE), direction)
        }
        else -> return this}
}

// Returns the position of the snake's tail
fun Snake.tailPosition(): Position {
    var tailDeviation = Position(0,0)
    when(direction) {
        UP -> tailDeviation = position.plus(OffsetVector(0,CELL_SIZE))
        RIGHT -> tailDeviation = position.minus(OffsetVector(CELL_SIZE,0))
        LEFT -> tailDeviation = position.plus(OffsetVector(CELL_SIZE,0))
        DOWN -> tailDeviation = position.minus(OffsetVector(0,CELL_SIZE))
        }
    return Position((tailDeviation.x),(tailDeviation.y))
}