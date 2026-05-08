import pt.isel.canvas.*

/**Initializes a game with the specified [level], [screen] and [paused] state. This function reduces the amount of
 * junk in the main file.*/
fun initGame(screen: Canvas,level: Int, paused: Boolean): Game {
    var walls: Walls
    if (level == 0){
        val level1: MutableList<Vector2> = mutableListOf(
            Vector2(0,0), Vector2(CELLS.size,0), Vector2(CELLS.size*2,0),
            Vector2(0,CELLS.size), Vector2(0,CELLS.size*2), Vector2(0,CELLS.size*3),
            Vector2(CELLS.normalize.x-CELLS.size,0), Vector2(CELLS.normalize.x-CELLS.size*2,0),
            Vector2(CELLS.normalize.x-CELLS.size*3,0), Vector2(CELLS.normalize.x-CELLS.size,CELLS.size),
            Vector2(CELLS.normalize.x-CELLS.size,CELLS.size*2),  Vector2(CELLS.normalize.x-CELLS.size,CELLS.size*3),
            Vector2(0,CELLS.normalize.y-CELLS.size), Vector2(0,CELLS.normalize.y-CELLS.size*2),
            Vector2(0,CELLS.normalize.y-CELLS.size*3), Vector2(0,CELLS.normalize.y-CELLS.size*4),
            Vector2(CELLS.size,CELLS.normalize.y-CELLS.size), Vector2(CELLS.size*2,CELLS.normalize.y-CELLS.size),
            CELLS.normalize-CELLS.square, Vector2(CELLS.normalize.x-CELLS.size*2, CELLS.normalize.y - CELLS.size),
            Vector2(CELLS.normalize.x-CELLS.size*3, CELLS.normalize.y - CELLS.size),
            Vector2(CELLS.normalize.x-CELLS.size, CELLS.normalize.y - CELLS.size*2),
            Vector2(CELLS.normalize.x-CELLS.size, CELLS.normalize.y - CELLS.size*3),
            Vector2(CELLS.normalize.x-CELLS.size, CELLS.normalize.y - CELLS.size*4))
        walls = Walls(level1, CELLS.totalCells().toMutableList(), screen)
    }
    else walls = Walls(mutableListOf(), CELLS.totalCells().toMutableList(), screen)
    walls.updateWall()

    return Game(
        Snake(
            (listOf<SnakePart>(SnakePart(CELLS.center,Direction.UP),
            SnakePart(Vector2(CELLS.center.x,CELLS.center.y+CELLS.size),Direction.UP))), screen),
        walls, Apple(null,screen), screen, 3, 0, false, paused
    )
}

fun drawLoadingScreen(screen: Canvas){
    screen.drawRect(Vector2(0, 0), CELLS.normalize * 2)
    screen.drawText(CELLS.center - Vector2(CELLS.size * 3, -CELLS.size), "Loading...", BLACK, 50)
}

/**The Game data class is responsible for the game drawing and control. */
data class Game(
    val snake: Snake,
    val walls: Walls,
    val apple: Apple,
    val screen: Canvas,
    val grow: Int,
    val score: Int,
    val debug: Boolean,
    val paused: Boolean
) {


    val debugButton = Button(
        Vector2(CELLS.normalize.x-CELLS.size*4+CELLS.size/2,CELLS.normalize.y+CELLS.size/2),
        Vector2(CELLS.size*3, CELLS.size), screen, "Debug", BLACK)

    val pauseButton = Button(Vector2(debugButton.pos.x-debugButton.size.x-CELLS.size/2, debugButton.pos.y),
        Vector2(CELLS.size*3, CELLS.size), screen, "Pause", BLACK)

    val restartButton = Button(CELLS.center - Vector2(CELLS.size*2,CELLS.size/2),
        Vector2(CELLS.size*4, CELLS.size), screen, "  Restart", BLACK)

    val level1Button: Button = Button(
        CELLS.center - Vector2(CELLS.size * 6, 0),
        Vector2(CELLS.size * 5, CELLS.size),
        screen,
        "Level 1",
        BLACK
    )
    val level2Button: Button = Button(
        CELLS.center + Vector2(CELLS.size * 2, 0),
        Vector2(CELLS.size * 5, CELLS.size),
        screen,
        "Level 2",
        BLACK
    )

    /** Creates a new apple*/
    fun addApple(): Game = this.copy(apple = apple.newApple(this))

    /** Runs the game on two different states, [paused] or not paused. When not paused, the game moves
     * the [snake], verifies if the snake will eat the [apple], if so, it generates another one. Then tests if the
     * apple is in the snake body (to prevent sync issues), draws the game, grows the snake and then draws
     * the ui elements including debug. When paused the game mainly draws the ui elements and game elements with the
     * exception that if the snake size is smaller or equal to 3, it will draw the level selector above all the
     * other ui elements*/
    fun run(): Game{
        if (!paused){
            var newGame = this.copy(snake = snake.move(this))
            if (checkApple(this)) {
                playSound("LevelUp")
                newGame = newGame.copy(grow = newGame.grow + growAmount, score = newGame.score + scoreAmount).addApple()
            }
            if (apple.pos in snake.totalPos(0)) newGame = newGame.addApple()
            draw(newGame)
            if (debug) newGame.drawDebug()
            newGame.drawUI()
            return newGame
        }
        else {
            draw(this)
            if (debug) drawDebug()
            drawUI()
            if (snake.totalPos(0).size <= 3) drawLevelSelect()
            return this
        }

    }

    /**Draws the game components: [snake], [walls] and [apple]*/
    fun draw(newGame: Game){
        newGame.screen.erase()
        newGame.apple.draw()
        newGame.snake.draw()
        newGame.walls.draw()
    }

    /**Draws the level selector screen*/
    fun drawLevelSelect(){
        screen.drawRect(Vector2(0, 0), CELLS.normalize * 2, 0, BLACK)
        screen.drawText(
            CELLS.center - Vector2(CELLS.size * 8, CELLS.size * 4),
            "SSnake: Select Level",
            WHITE,
            50
        )
        level1Button.draw()
        level2Button.draw()
    }

    /**Draws the game ui with the [score], snake size and buttons*/
    fun drawUI(){
        screen.drawRect(Vector2(0,CELLS.normalize.y),Vector2(CELLS.normalize.x, CELLS.size*2),0,GREY)
        debugButton.draw()
        pauseButton.draw()
        if (isStuck(this) && snake.totalPos(0).size < 60) {
            screen.drawText(CELLS.center - Vector2(CELLS.size * 4, CELLS.size), "YOU LOSE", WHITE, 50)
            restartButton.draw()
        }
        else if (isStuck(this)) {
            screen.drawText(CELLS.center - Vector2((CELLS.size * 3.5).toInt(), CELLS.size), "YOU WIN", WHITE, 50)
            restartButton.draw()
        }
        screen.drawText(Vector2(CELLS.size*6,CELLS.normalize.y) + Vector2(0,CELLS.size + CELLS.size/4), "Score: $score",WHITE,28)

        if (snake.totalPos(0).size < 60)
            screen.drawText(Vector2(CELLS.size/2,CELLS.normalize.y) + Vector2(0,CELLS.size + CELLS.size/4), "Size: ${snake.totalPos(0).size}",WHITE,28)
        else
            screen.drawText(Vector2(CELLS.size/2,CELLS.normalize.y) + Vector2(0,CELLS.size + CELLS.size/4), "Size: ${snake.totalPos(0).size}",GREEN,28)

        if (paused){
            screen.drawText(Vector2(CELLS.size, CELLS.size+CELLS.size/2),"PAUSED",RED,("PAUSED").length*5)
            screen.drawRect(Vector2(0,0)+CELLS.size/2,CELLS.normalize-CELLS.size,2,RED)
        }

    }

    /**Grows the snake by 1 when she is moving freely and returns the new game.*/
    fun grow(): Game{
        return if(!willCollide(this) && grow > 0){
            this.copy(snake = snake.newPart(), grow = grow -1)
        }
        else this
    }

    /**Draws the game [debug] elements: cell grid, collision markers, and snake position highlighter*/
    fun drawDebug(){
        for (x in 0..<CELLS.grid.x)
            for (y in 0..<CELLS.grid.y)
                screen.drawRect(Vector2(x,y)*CELLS.size,CELLS.square,1)

        for (i in 0..<snake.totalPos(0).size)
                screen.drawRect(snake.body[i].pos,CELLS.square,2,YELLOW)
        if (snake.body[0].direction == Direction.LEFT || snake.body[0].direction == Direction.RIGHT) {
            if (!canChangeDirection(Direction.UP, this))
                screen.drawLine(snake.body[0].pos, snake.body[0].pos + Vector2(CELLS.size, 0), 4, RED)
            if (!canChangeDirection(Direction.DOWN, this))
                screen.drawLine(snake.body[0].pos + Vector2(0, CELLS.size), snake.body[0].pos + Vector2(CELLS.size, CELLS.size), 4, RED)
        }
        else if (snake.body[0].direction == Direction.UP || snake.body[0].direction == Direction.DOWN) {
            if (!canChangeDirection(Direction.LEFT, this))
                screen.drawLine(snake.body[0].pos, snake.body[0].pos + Vector2(0, CELLS.size), 4, RED)
            if (!canChangeDirection(Direction.RIGHT, this))
                screen.drawLine(snake.body[0].pos + Vector2(CELLS.size, 0), snake.body[0].pos + Vector2(CELLS.size, CELLS.size), 4, RED)
        }
        if (willCollide(this))
            when (snake.body[0].direction) {
                Direction.UP ->
                    screen.drawLine(snake.body[0].pos, snake.body[0].pos + Vector2(CELLS.size, 0), 4, RED)
                Direction.DOWN ->
                    screen.drawLine(snake.body[0].pos + Vector2(0, CELLS.size), snake.body[0].pos + Vector2(CELLS.size, CELLS.size), 4, RED)
                Direction.LEFT ->
                    screen.drawLine(snake.body[0].pos, snake.body[0].pos + Vector2(0, CELLS.size), 4, RED)
                Direction.RIGHT ->
                    screen.drawLine(snake.body[0].pos + Vector2(CELLS.size, 0), snake.body[0].pos + Vector2(CELLS.size, CELLS.size), 4, RED)
            }
    }
}