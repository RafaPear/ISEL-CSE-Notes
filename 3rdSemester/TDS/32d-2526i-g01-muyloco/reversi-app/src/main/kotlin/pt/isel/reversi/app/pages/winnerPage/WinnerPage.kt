package pt.isel.reversi.app.pages.winnerPage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.ScaffoldView
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.getTheme
import pt.isel.reversi.app.pages.menu.MovingPiece
import pt.isel.reversi.app.pages.menu.drawCrown
import pt.isel.reversi.app.pages.menu.drawEyes
import pt.isel.reversi.app.pages.menu.drawPiece
import pt.isel.reversi.app.utils.PreviousPage
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.Player

fun testTagDrawCrown(): String = "draw_crown"

@Composable
fun ReversiScope.WinnerPage(
    viewModel: WinnerPageViewModel, onLeave: () -> Unit
) {
    val state = viewModel.uiState.value

    ScaffoldView(
        setError = { error, type -> viewModel.setError(error, type) },
        error = viewModel.error,
        isLoading = state.screenState.isLoading,
        title = "Fim de Jogo",
        previousPageContent = {
            PreviousPage { onLeave() }
        }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            val winner = state.winner ?: run {
                ReversiText(
                    text = "Empate!", color = appState.theme.textColor
                )
                return@Column
            }
            val players = viewModel.game.gameState?.players?.toList() ?: emptyList()

            if (players.isEmpty()) {
                ReversiText(
                    text = "Dados do vencedor indisponíveis.", color = appState.theme.textColor
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly, // Ensures equal spacing
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    players.forEach { player ->
                        PlayerResultColumn(
                            player = player, isWinner = player.type == winner.type, modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReversiScope.PlayerResultColumn(
    player: Player, isWinner: Boolean, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier, // Each player takes 50% width
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(80.dp)) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (isWinner) Modifier.semantics { testTag = testTagDrawCrown() }
                        else Modifier
                    )
            ) {
                val paddingH = size.height * 0.5f
                val cx = size.width / 2f
                val cy = size.height / 2f - paddingH / 2f
                val radius = size.minDimension / 1.2f
                val isWhite = player.type == PieceType.WHITE
                val theme = getTheme()

                drawPiece(
                    piece = MovingPiece.empty(isWhite),
                    theme = theme,
                    x = cx,
                    y = cy,
                    radiusPx = radius,
                    edgeFade = 1f,
                    baseAlpha = 1f
                )

                drawEyes(isWhite, theme, cx, cy, radius, 100f)

                // Crown
                drawCrown(cx, cy, radius, if (isWinner) 1f else 0f)
            }
        }
        DrawPlayerInfo(player)
    }
}

@Composable
fun ReversiScope.DrawPlayerInfo(player: Player) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ReversiText(
            text = player.name,
            color = appState.theme.textColor,
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        ReversiText(
            text = "${player.points} pontos",
            color = appState.theme.textColor,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

