package pt.isel.reversi.app.pages.lobby.lobbyViews

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText

const val EMPTY_LOBBY_TAG = "empty_lobby"
const val EMPTY_LOBBY_ICON_TAG = "empty_lobby_icon"
const val EMPTY_LOBBY_TEXT_TAG = "empty_lobby_text"
const val TEXT_EMPTY_LOBBY = "Nenhum jogo guardado"

/**
 * Composable displaying an empty state for the lobby when no saved games are available.
 * Shows a game icon and prompts the user to create a new game.
 *
 * @param buttonRefresh Optional composable for rendering a refresh button.
 */
@Composable
fun ReversiScope.Empty(buttonRefresh: @Composable () -> Unit = {}) {

    Icon(
        Icons.Filled.SportsEsports,
        contentDescription = null,
        modifier = Modifier.size(80.dp).testTag(EMPTY_LOBBY_ICON_TAG),
        tint = Color.White.copy(alpha = 0.3f)
    )
    Spacer(Modifier.height(16.dp))
    ReversiText(
        TEXT_EMPTY_LOBBY,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.testTag(EMPTY_LOBBY_TEXT_TAG)
    )
    Spacer(Modifier.height(8.dp))
    ReversiText(
        "Comece um novo jogo",
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag(EMPTY_LOBBY_TAG)
    )
    buttonRefresh()

}
