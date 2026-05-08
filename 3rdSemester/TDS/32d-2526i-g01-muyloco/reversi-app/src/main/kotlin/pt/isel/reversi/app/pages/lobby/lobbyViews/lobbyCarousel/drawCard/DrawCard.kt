package pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.getTheme
import pt.isel.reversi.app.pages.game.utils.DrawBoard
import pt.isel.reversi.app.pages.lobby.LobbyLoadedState
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.MatchPlayers

fun testTagCard(gameId: String) = "game_card_$gameId"

fun testTagHeaderBadge(gameId: String) = "header_badge_$gameId"

fun testTagScorePanel(gameId: String) = "score_panel_$gameId"

fun testTagStatusBadge(gameId: String) = "status_badge_$gameId"

fun testTagStatusText(gameid: String) = "status_text_$gameid"

fun testTagScoreItemPiece(scorePainelTestTag: String, pieceType: PieceType) =
    "score_item_${scorePainelTestTag}_${pieceType.name}"

fun testTagScoreItemScore(scorePainelTestTag: String, pieceType: PieceType, score: Int) =
    "score_item_${scorePainelTestTag}_${pieceType.name}_score_$score"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReversiScope.GameCard(
    game: LobbyLoadedState,
    enabled: Boolean,
    cardData: CardStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val name = game.name
    val state = game.gameState

    val statusText = cardData.text
    val statusColor = cardData.color

    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.shadow(16.dp, RoundedCornerShape(24.dp))
            .testTag(testTagCard(name)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.White.copy(0.1f)),
        colors = CardDefaults.cardColors(
            containerColor = getTheme().backgroundColor,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(getTheme().secondaryColor.copy(.2f))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderBadge(statusText, statusColor, name)

            PlayerNamesInGameBadge(state.players)

            DrawBoard(
                false,
                state,
                modifier = Modifier.weight(4f),
                true,
                { emptyList() },
                {}
            )

            Spacer(Modifier.height(8.dp))

            ScorePanel(board = state.board, scorePainelTestTag = testTagScorePanel(name))
        }
    }
}

//public final data class PlayerName(
//    public final val type: PieceType,
//    public final val name: String
//)


@Composable
private fun ReversiScope.PlayerNamesInGameBadge(players: MatchPlayers) {
    val orderedTypes = listOf(PieceType.WHITE, PieceType.BLACK)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        orderedTypes.forEach { type ->
            val player = players.getPlayerByType(type)

            if (player != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(vertical = 6.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (type == PieceType.BLACK)
                            getTheme().darkPieceColor
                        else
                            getTheme().lightPieceColor,
                        border = BorderStroke(1.dp, getTheme().lightPieceColor),
                        modifier = Modifier.size(18.dp)
                    ) {}

                    Spacer(Modifier.width(8.dp))

                    ReversiText(
                        text = player.name,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.04f))
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.15f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 6.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        modifier = Modifier.size(18.dp)
                    ) {}

                    Spacer(Modifier.width(8.dp))

                    ReversiText(
                        text = "À espera de jogador…",
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.alpha(0.5f)
                    )
                }
            }
        }
    }
}


@Composable
private fun ReversiScope.HeaderBadge(statusText: String, statusColor: Color, name: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag(testTagHeaderBadge(name)),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ReversiText(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(testTagStatusText(name)),
        )
        StatusBadge(statusText, statusColor, modifier = Modifier.testTag(testTagStatusBadge(name)))
    }
}

@Composable
private fun ReversiScope.StatusBadge(text: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp), color = color.copy(0.2f)
    ) {
        ReversiText(
            text = text,
            color = color,
            fontSize = 11.sp,
            modifier = modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ReversiScope.ScorePanel(modifier: Modifier = Modifier, board: Board, scorePainelTestTag: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(getTheme().secondaryColor.copy(.2f), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(16.dp))
            .padding(8.dp)
            .testTag(scorePainelTestTag),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ScoreItem(
            type = PieceType.BLACK,
            score = board.totalBlackPieces,
            pieceTestTag = testTagScoreItemPiece(scorePainelTestTag, PieceType.BLACK),
            scoreTestTag = testTagScoreItemScore(scorePainelTestTag, PieceType.BLACK, board.totalBlackPieces)
        )
        ScoreItem(
            type = PieceType.WHITE,
            score = board.totalWhitePieces,
            pieceTestTag = testTagScoreItemPiece(scorePainelTestTag, PieceType.WHITE),
            scoreTestTag = testTagScoreItemScore(scorePainelTestTag, PieceType.WHITE, board.totalWhitePieces)
        )
    }
}

@Composable
private fun ReversiScope.ScoreItem(type: PieceType, score: Int, pieceTestTag: String, scoreTestTag: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(40.dp).background(
                if (type == PieceType.BLACK) getTheme().darkPieceColor else getTheme().lightPieceColor, CircleShape
            ).border(
                2.dp,
                if (type == PieceType.BLACK) getTheme().darkPieceColor.copy(0.2f) else getTheme().lightPieceColor.copy(
                    0.2f
                ),
                CircleShape
            ).testTag(pieceTestTag)
        )
        Spacer(Modifier.height(8.dp))
        ReversiText(
            text = score.toString(),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(scoreTestTag)
        )
    }
}