// *********************************************************
// Game Logic and Rendering
// *********************************************************
// This file defines the core game logic and rendering functionalities.
// It includes methods to manage the game's state, draw elements such
// as walls and the snake, and update the game with new elements like
// additional walls. Debugging tools for visualization are also implemented
// to facilitate game development and testing.
// *********************************************************


import pt.isel.canvas.*
import kotlin.random.Random

// Debug Mode
const val DEBUG = false

// Cell properties
const val CELL_SIZE = 32
const val CELL_WIDTH = 20
const val CELL_HEIGHT = 16

// Screen properties
const val WIDTH = CELL_WIDTH*CELL_SIZE
const val HEIGHT = CELL_HEIGHT*CELL_SIZE

const val TOTAL_OF_BLOCKS = CELL_WIDTH*CELL_HEIGHT

// Snake directions (for better readability)
const val UP = 0
const val RIGHT = 1
const val LEFT = 2
const val DOWN = 3

// Vector class
data class OffsetVector(val dx: Int, val dy: Int)

// Position class
data class Position(val x: Int, val y: Int)

// Function that sums a vector to a Position
operator fun Position.plus(vector: OffsetVector)
        = Position(x + vector.dx, y + vector.dy)

// Function that subtracts a vector from a Position
operator fun Position.minus(vector: OffsetVector)
        = Position(x - vector.dx, y - vector.dy)

// Game Data Class
data class Game(val snake: Snake, val wall: List<Position>)

// Draws the walls to the screen
fun Game.drawWalls(screen: Canvas){
    var filledWall: List<Position> = wall

    for (pos in filledWall) {
        screen.drawImage("bricks.png|0,0,118,118", pos.x, pos.y, 32, 32)
    }
}

// Returns a new Game class with a new wall added to the list
fun Game.newWall(snake: Snake,screen: Canvas): Game{
    var filledWall = wall

    while (true) {
        if (isStuck()) break

        val wall = Position(
            Random.nextInt(CELL_WIDTH) * CELL_SIZE,
            Random.nextInt(CELL_HEIGHT) * CELL_SIZE
        )

        if (DEBUG) screen.drawRect(wall.x,wall.y,CELL_SIZE,CELL_SIZE,RED,5)

        if (filledWall.size >= TOTAL_OF_BLOCKS - 2) break
        else if (wall !in filledWall && wall != snake.position && wall != snake.tailPosition()) {
            filledWall = filledWall + wall
            if (DEBUG) screen.drawRect(wall.x,wall.y,CELL_SIZE,CELL_SIZE,GREEN,5)
            break
        }
    }
    return Game(snake, filledWall)
}

// Draws the snake to the screen
fun Game.drawSnake(screen: Canvas) {
    var headPos: Position = Position(0,0)
    var tailPos: Position = Position(0,0)
    var tailDeviation: Position = Position(0,0)
    var headDeviation: Position = Position(0,0)

    when(snake.direction) {
        UP -> {
            headPos = headPos.plus(OffsetVector(3,0))
            tailPos = tailPos.plus(OffsetVector(3,2))
            tailDeviation = tailDeviation.plus(OffsetVector(0,CELL_SIZE))

            // When the snake reaches
            if (snake.position.y <= 0 - CELL_SIZE) {
                headDeviation = headDeviation.plus(OffsetVector(0,HEIGHT))
            }
        }
        RIGHT -> {
            headPos = headPos.plus(OffsetVector(4,0))
            tailPos = tailPos.plus(OffsetVector(4,2))
            tailDeviation = tailDeviation.minus(OffsetVector(CELL_SIZE,0))
            if (snake.position.x > WIDTH-CELL_SIZE) {
                headDeviation = headDeviation.minus(OffsetVector(WIDTH,0))
            }
        }
        LEFT -> {
            headPos = headPos.plus(OffsetVector(3,1))
            tailPos = tailPos.plus(OffsetVector(3,3))
            tailDeviation = tailDeviation.plus(OffsetVector(CELL_SIZE,0))
            if (snake.position.x <= 0 - CELL_SIZE) {
                headDeviation = headDeviation.plus(OffsetVector(WIDTH,0))
            }
        }
        DOWN -> {
            headPos = headPos.plus(OffsetVector(4,1))
            tailPos = tailPos.plus(OffsetVector(4,3))
            tailDeviation = tailDeviation.minus(OffsetVector(0,CELL_SIZE))
            if (snake.position.y > HEIGHT-CELL_SIZE) {
                headDeviation = headDeviation.minus(OffsetVector(0,HEIGHT))
            }
        }
    }

    screen.drawImage(
        "snake|${headPos.x*64},${headPos.y*64},64,64"
        ,snake.position.x + headDeviation.x,snake.position.y + headDeviation.y,CELL_SIZE,CELL_SIZE)

    screen.drawImage(
        "snake|${tailPos.x*64},${tailPos.y*64},64,64"
        ,(snake.position.x + tailDeviation.x),(snake.position.y + tailDeviation.y),CELL_SIZE,CELL_SIZE)
}

// Returns a Game class with a new given snake
fun Game.newSnake(snake: Snake): Game = Game(snake, wall)

fun Game.drawDebugView(screen: Canvas){
    if (DEBUG){
        var grid: List<Position> = emptyList()
        for (x in 0..CELL_WIDTH)
            for (y in 0..CELL_HEIGHT)
                grid += Position(x * CELL_SIZE, y * CELL_SIZE)

        // Draw Debug Grid
        for (pos in grid) {
            screen.drawLine(pos.x, pos.y, pos.x, pos.y + CELL_SIZE, WHITE, 1)
            screen.drawLine(pos.x, pos.y, pos.x + CELL_SIZE, pos.y, WHITE, 1)
        }

        // Draw Snake Hit Boxes
        screen.drawRect(snake.position.x, snake.position.y, CELL_SIZE, CELL_SIZE, YELLOW, 2)
        screen.drawRect(snake.tailPosition().x, snake.tailPosition().y, CELL_SIZE, CELL_SIZE, YELLOW, 2)

        // Shows the Snake's Head Hit Box, or side, red when colliding
        if (!canMove()) {
            screen.drawRect(snake.position.x, snake.position.y, CELL_SIZE, CELL_SIZE, RED, 2)
        }
        if (!canMoveLeft() && snake.direction != RIGHT) {
            screen.drawLine(snake.position.x, snake.position.y, snake.position.x, snake.position.y + CELL_SIZE, RED, 5)
        }
        if (!canMoveRight() && snake.direction != LEFT) {
            screen.drawLine(
                snake.position.x + CELL_SIZE,
                snake.position.y,
                snake.position.x + CELL_SIZE,
                snake.position.y + CELL_SIZE,
                RED,
                5
            )
        }
        if (!canMoveUp() && snake.direction != DOWN) {
            screen.drawLine(snake.position.x, snake.position.y, snake.position.x + CELL_SIZE, snake.position.y, RED, 5)
        }
        if (!canMoveDown() && snake.direction != UP) {
            screen.drawLine(
                snake.position.x,
                snake.position.y + CELL_SIZE,
                snake.position.x + CELL_SIZE,
                snake.position.y + CELL_SIZE,
                RED,
                5
            )
        }

        // returns the snake direction name
        fun directionToText(): String =
            when (snake.direction) {
                0 -> "UP"
                1 -> "RIGHT"
                2 -> "LEFT"
                3 -> "DOWN"
                else -> "error"
            }

        // Draws the snakes position above her
        screen.drawText(snake.position.x,snake.position.y-1, "pos:${snake.position.x},${snake.position.y}", YELLOW, 15)

        // Draws a red circle on the snakes top left corner (real position)
        screen.drawCircle(snake.position.x,snake.position.y,3,RED)

        // Draws the snakes direction on the bottom left of the window
        screen.drawText(3, HEIGHT - CELL_SIZE/2-3, "Snake direction:${directionToText()}", YELLOW, 15)

        // Draws the snakes position on the bottom left of the window
        screen.drawText(3, HEIGHT - 3, "Snake pos:${snake.position.x},${snake.position.y}", YELLOW, 15)
    }
}