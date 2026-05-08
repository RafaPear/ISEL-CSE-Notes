package pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.ReversiTextField

fun testTagSearch() = "lobby_carousel_search"

@Composable
fun ReversiScope.Search(search: String, onValueChange: (String) -> Unit) {
    ReversiTextField(
        value = search,
        singleLine = true,
        onValueChange = { onValueChange(it) },
        label = { ReversiText("Procure um jogo...") },
        modifier = Modifier.testTag(testTagSearch())
    )
}