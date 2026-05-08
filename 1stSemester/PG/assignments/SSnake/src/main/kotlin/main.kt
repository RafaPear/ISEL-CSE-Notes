// *********************************************************
// Main Entry Point for Snake Game
// *********************************************************
// This file initializes the Snake game, setting up the game state,
// handling user input for controlling the snake, and managing
// the main game loop. It ties together game logic, rendering,
// and input handling to deliver the complete experience.
// *********************************************************


import pt.isel.canvas.*

fun main() {
    onStart {
        var game = Game(Snake(Position(WIDTH/2, HEIGHT/2), UP), emptyList())
        val screen = Canvas(WIDTH, HEIGHT, BLACK)
        println("Initialized Game with: ")
        println("   - Snake position at ${game.snake.position.x}, ${game.snake.position.y}")
        println("   - Snake direction at ${game.snake.direction}")
        println("   - Cell width at $CELL_WIDTH")
        println("   - Cell height at $CELL_HEIGHT")
        println("   - Cell size at $CELL_SIZE")
        println("   - Debug mode? $DEBUG")

        screen.onKeyPressed{ keyEvent ->
            game = game.newSnake(when (keyEvent.code) {
                LEFT_CODE -> if (game.canMoveLeft()) game.snake.setDirection(LEFT) else game.snake
                RIGHT_CODE -> if (game.canMoveRight()) game.snake.setDirection(RIGHT) else game.snake
                UP_CODE -> if (game.canMoveUp()) game.snake.setDirection(UP) else game.snake
                DOWN_CODE -> if (game.canMoveDown()) game.snake.setDirection(DOWN) else game.snake
                else -> game.snake
            })
        }

        // 5-Second timer to generate a new wall position
        screen.onTimeProgress(5000) {
            game = game.newWall(game.snake,screen)
        }

        // Main Game Loop
        screen.onTimeProgress(250){
            screen.erase()
            game = game.newSnake(if (!game.canMove()) game.snake else game.snake.move())
            game.drawWalls(screen)
            game.drawSnake(screen)
            game.drawDebugView(screen)
            if (game.isStuck()) screen.drawText(WIDTH/2-CELL_SIZE*4+CELL_SIZE/2,HEIGHT/2+CELL_SIZE/2,"GAME OVER",WHITE,40)
        }
    }

    onFinish {
        println("We Stopped")
    }
}
