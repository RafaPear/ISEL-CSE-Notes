package pt.isel.reversi.app.pages.game.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.getTheme
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.GameState

/** Transparency level for ghost pieces showing potential moves. */
const val GHOST_PIECE_ALPHA = 0.5f

/**
 * Composable rendering the interactive Reversi game board.
 * Displays the grid, pieces, and available moves (if target mode is enabled).
 * Handles user clicks for piece placement with click blocking during animations.
 *
 * @param target Whether to show available move indicators.
 * @param gameState Current game state containing board and player information.
 * @param modifier Optional composable modifier for layout adjustments.
 * @param freeze Whether to prevent user interaction with the board.
 * @param getAvailablePlays Lambda returning list of available move coordinates.
 * @param onCellClick Callback invoked when a board cell is clicked with the coordinate.
 */
@Composable
fun ReversiScope.DrawBoard(
    target: Boolean,
    gameState: GameState,
    modifier: Modifier = Modifier,
    freeze: Boolean = false,
    getAvailablePlays: () -> List<Coordinate>,
    onCellClick: (coordinate: Coordinate) -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .dropShadow(shadow = Shadow(radius = 12.dp), shape = RoundedCornerShape(12.dp))
            .background(getTheme().boardBgColor, shape = RoundedCornerShape(12.dp))
            .padding(all = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .testTag(tag = testTagBoard())
    ) {
        Grid(target, gameState, modifier, freeze, getAvailablePlays) { coordinate -> onCellClick(coordinate) }
    }
}

/**
 * Renders the board grid with cells, pieces, and animated indicators for valid moves.
 * Supports animation feedback for available moves in target mode.
 *
 * @param target Whether to show available move indicators with animation.
 * @param gameState Current game state containing board and player information.
 * @param modifier Optional composable modifier for layout adjustments.
 * @param freeze Whether to prevent user interaction with cells.
 * @param getAvailablePlays Lambda returning list of available move coordinates.
 * @param onCellClick Callback invoked when a board cell is clicked.
 */
@Composable
fun ReversiScope.Grid(
    target: Boolean,
    gameState: GameState,
    modifier: Modifier = Modifier,
    freeze: Boolean = false,
    getAvailablePlays: () -> List<Coordinate>,
    onCellClick: (coordinate: Coordinate) -> Unit
) {
    val board: Board = gameState.board
    val side = board.side
    val playerTurn = gameState.lastPlayer.swap()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val availablePlays = if (target) getAvailablePlays() else emptyList()

        val infiniteTransition = rememberInfiniteTransition()
        val alphaAnim by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 0.4f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)),
                repeatMode = RepeatMode.Reverse
            )
        )
        val sizeAnim by infiniteTransition.animateFloat(
            initialValue = 1.0f,
            targetValue = 0.85f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)),
                repeatMode = RepeatMode.Reverse
            )
        )

        repeat(side) { y ->
            Row(
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                repeat(side) { x ->
                    val coordinate = Coordinate(x + 1, y + 1)
                    val ghostPiece = target && availablePlays.contains(coordinate)
                    val value = board[coordinate] ?: if (ghostPiece) playerTurn else null

                    val piece: Piece? =
                        if (value == null)
                            null
                        else
                            Piece(
                                coordinate = coordinate,
                                value = value,
                                isGhostPiece = ghostPiece
                            )

                    cellView(
                        coordinate,
                        piece = piece,
                        freeze,
                        modifier = modifier.weight(1f),
                        radiusModifier = if (ghostPiece) sizeAnim else 1f,
                        alphaModifier = if (ghostPiece) alphaAnim else 1f,
                    ) {
                        onCellClick(coordinate)
                    }
                }
            }
        }
    }
}

@Composable
fun ReversiScope.cellView(
    coordinate: Coordinate,
    piece: Piece?,
    freeze: Boolean = false,
    modifier: Modifier = Modifier,
    radiusModifier: Float = 1f,
    alphaModifier: Float = 1f,
    onClick: (coordinate: Coordinate) -> Unit
) {
    val clickable = !freeze && (piece == null || piece.isGhostPiece)
    val newModifier =
        if (clickable) modifier.clickable { onClick(coordinate) }
        else modifier

    Box(
        modifier = newModifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(getTheme().boardColor)
            .testTag(testTagCellView(coordinate)),
        contentAlignment = Alignment.Center
    ) {
        val type = piece?.value ?: return@Box

        val theme = getTheme()
        val pieceColor = when (type) {
            PieceType.BLACK -> theme.darkPieceColor
            PieceType.WHITE -> theme.lightPieceColor
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .semantics { testTag = testTagPiece(coordinate, type = type) }
        ) {
            val size = size
            val radius = size.minDimension / 2 * 0.5f
            val center = Offset(size.width / 2, size.height / 2)

            if (!piece.isGhostPiece) {
                drawPiece(radius, center, pieceColor, drawScope = this)
            } else {

                drawCircle(
                    color = pieceColor.copy(alpha = GHOST_PIECE_ALPHA),
                    radius = radius * radiusModifier,
                    center = center,
                    alpha = alphaModifier,
                )

                drawCircle(
                    color = pieceColor.copy(alpha = alphaModifier * 0.5f),
                    radius = radius * radiusModifier,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                )
            }
        }

    }
}

private fun drawPiece(
    radius: Float,
    center: Offset,
    color: Color,
    drawScope: DrawScope
) {
    val depth = radius * 0.3f
    val shadowOffset = Offset(depth * 2, depth * 2)

    val shadowColor = Color.Black.copy(alpha = 0.25f)

    drawScope.drawCircle(
        color = shadowColor,
        radius = radius,
        center = center + shadowOffset
    )

    val sideColor = Color(
        red = (color.red * 0.7f).coerceIn(0f, 1f),
        green = (color.green * 0.7f).coerceIn(0f, 1f),
        blue = (color.blue * 0.7f).coerceIn(0f, 1f),
        alpha = color.alpha
    )

    drawScope.drawCircle(
        color = sideColor,
        radius = radius,
        center = center + Offset(depth, depth)
    )

    drawScope.drawCircle(
        color = color,
        radius = radius,
        center = center
    )
}
