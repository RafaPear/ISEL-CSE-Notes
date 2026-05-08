import pt.isel.canvas.*

val CELLS = Cells(Vector2(20,16),32)
const val scoreAmount: Int = 1
const val growAmount: Int = 5


fun main() {
    onStart {
        println("Here we goo!!")

        //Creates the window with the specified values in CELLS.
        val screen = CELLS.createWindow() ; println("Created the window with $CELLS")

        //Draws the loading screen while the sounds load.
        drawLoadingScreen(screen)
        loadSounds("LevelUp", "Music1", "button1", "button2") ; println("Loading Sounds")

        //Initializes the game with the Paused condition to allow the player to choose the desired level.
        var game: Game = initGame(screen, 0, true).addApple()

        //Plays the song on start and then loops it every 1.68 minutes.
        playSound("Music1")
        screen.onTimeProgress(101000) { playSound("Music1") }

        //Keyboard Key handler for the snake movement.
        // For the key pressed it checks if the snake can change to the desired direction
        // and if the game is NOT paused.
        screen.onKeyPressed { keyEvent ->
            game = when (keyEvent.code) {
                UP_CODE -> if (canChangeDirection(Direction.UP, game) && !game.paused)
                    game.copy(snake = game.snake.setDirection(Direction.UP))
                else game

                DOWN_CODE -> if (canChangeDirection(Direction.DOWN, game) && !game.paused)
                    game.copy(snake = game.snake.setDirection(Direction.DOWN))
                else game

                LEFT_CODE -> if (canChangeDirection(Direction.LEFT, game) && !game.paused)
                    game.copy(snake = game.snake.setDirection(Direction.LEFT))
                else game

                RIGHT_CODE -> if (canChangeDirection(Direction.RIGHT, game) && !game.paused)
                    game.copy(snake = game.snake.setDirection(Direction.RIGHT))
                else game

                else -> game
            }
        }

        //Mouse left click handler for the buttons.
        screen.onMouseDown { mouseEvent ->
            if (game.snake.totalPos(0).size <= 3 && game.paused) {
                if (game.level1Button.isClicked(mouseEvent)) {
                    game = initGame(screen, 1, false).addApple()
                } else if (game.level2Button.isClicked(mouseEvent)) {
                    game = initGame(screen, 0, false).addApple()
                }
            }
            //Checks if the Debug Button was pressed (if the mouse click was inside the button borders).
            game = game.copy(
                debug = if (game.debugButton.isClicked(mouseEvent) && game.snake.totalPos(0).size > 3) {
                    if (game.debug) playSound("button1") else playSound("button2")
                    !game.debug
                } else game.debug
            )

            //Checks if the Pause Button was pressed (if the mouse click was inside the button borders).
            game = game.copy(
                paused = if (game.pauseButton.isClicked(mouseEvent) && game.snake.totalPos(0).size > 3) {
                    if (game.paused) playSound("button1") else playSound("button2")
                    !game.paused
                } else game.paused
            )

            //Checks if the Restart Button was pressed (Only works if the player is stuck).
            if (game.restartButton.isClicked(mouseEvent) && isStuck(game)) {
                playSound("button1")
                game = initGame(screen, 0, true).addApple()
            }
        }

        //Creates a new wall every 5 seconds (if the game is NOT paused) and plays a sound.
        screen.onTimeProgress(5000) {
            if (!game.paused) {
                game.walls.newWall(game.snake, game.apple, game.debug)
                playSound("button1")
            }
        }

        //Main game run loop every 250 milliseconds or
        screen.onTimeProgress(250) {
            if (!game.paused) {
                game = game.grow()
            }
            game = game.run()
        }
    }

    onFinish {
        println("Oh already??... :(")
    }
}