package pt.isel.reversi.app.pages.game.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.getTheme
import pt.isel.reversi.app.app.state.invert
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.Player

@Composable
fun ReversiScope.TextPlayersScore(
    state: GameState?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        ReversiText(
            text = "Placar",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(Modifier.height(24.dp))

        if (state == null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ReversiText(
                    "Sem jogo ativo",
                    fontStyle = FontStyle.Italic,
                )
            }
        } else {
            val orderedTypes = listOf(PieceType.WHITE, PieceType.BLACK)

            orderedTypes.forEach { type ->
                val player = state.players.getPlayerByType(type)

                if (player != null) {
                    val points = player.points
                    val isTurn = type != state.lastPlayer
                    val isWinner = state.winner?.type == type

                    PlayerScoreRow(
                        player = player,
                        points = points,
                        isTurn = isTurn,
                        isWinner = isWinner,
                        modifier = Modifier.testTag(testTagPlayerScore(Player(type, points = points)))
                    )
                } else {
                    MissingPlayerRow(type)
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ReversiScope.PlayerScoreRow(
    player: Player,
    points: Int,
    isTurn: Boolean,
    isWinner: Boolean,
    modifier: Modifier = Modifier
) {
    val type = player.type

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(getTheme().textColor.invert().copy(alpha = 0.4f))
            .border(
                width = if (isTurn) 2.dp else 0.dp,
                color = if (isTurn) Color.Green.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = if (type == PieceType.BLACK) getTheme().darkPieceColor else getTheme().lightPieceColor,
                border = BorderStroke(1.dp, getTheme().lightPieceColor),
                modifier = Modifier.size(24.dp)
            ) {}

            Spacer(Modifier.width(12.dp))

            ReversiText(
                text = player.name,
                fontWeight = if (isTurn) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.alpha(if (isTurn || isWinner) 1f else 0.6f)
            )
        }

        ReversiText(
            text = "$points",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = if (isWinner) Color.Green else getTheme().textColor
        )
    }
}

@Composable
private fun ReversiScope.MissingPlayerRow(type: PieceType) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(
                1.dp,
                Color.White.copy(alpha = 0.15f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = (if (type == PieceType.BLACK) getTheme().darkPieceColor else getTheme().lightPieceColor).copy(
                    alpha = 0.1f
                ),
                border = BorderStroke(1.dp, getTheme().lightPieceColor.copy(alpha = 0.3f)),
                modifier = Modifier.size(24.dp)
            ) {}

            Spacer(Modifier.width(12.dp))

            ReversiText(
                text = "À espera de jogador…",
                fontStyle = FontStyle.Italic,
                modifier = Modifier.alpha(0.5f)
            )
        }

        ReversiText(
            text = "--",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.alpha(0.3f)
        )
    }
}
