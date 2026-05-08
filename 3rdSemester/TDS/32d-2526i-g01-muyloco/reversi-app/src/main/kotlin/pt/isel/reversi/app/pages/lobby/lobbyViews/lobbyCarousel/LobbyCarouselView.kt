package pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.lobby.LobbyLoadedState
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard.CardStatus
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard.GameCard
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard.getCardStatus
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.NavButton
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.testTagNavButton
import kotlin.math.absoluteValue

fun testTagLobbyCarouselPager() = "lobby_carousel_pager"
fun testTagLobbyCarrouselEmpty() = "lobby_carousel_empty"

const val EMPTY_LOBBY_CAROUSEL_TEXT = "Nenhum jogo encontrado"

@Composable
fun BoxWithConstraintsScope.LobbyCarouselView(
    currentGameName: String?,
    pagerState: PagerState,
    games: List<LobbyLoadedState>,
    reversiScope: ReversiScope,
    onNavButtonClick: (Int) -> Unit,
    onGameClick: (LobbyLoadedState, Int) -> Unit
) {
    val availableWidth = this.maxWidth
    val availableHeight = this.maxHeight

    val maxCardWidth = (availableWidth * 0.7f).coerceAtMost(450.dp)
    val maxCardHeight = (availableHeight * 0.8f).coerceAtMost(950.dp)

    val horizontalPadding = (availableWidth / 2 - maxCardWidth / 2)

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        pageSpacing = 16.dp,
        modifier = Modifier.testTag(testTagLobbyCarouselPager())
    ) { page ->
        val pageOffset =
            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
        val distance = pageOffset.absoluteValue.coerceIn(0f, 1f)

        val scale = 0.95f + (1f - distance) * 0.12f
        val alpha = 0.2f + (1f - distance) * 0.8f
        val translation = 8.dp * distance

        val game = games[page]
        val cardState = getCardStatus(game, currentGameName)
        with(reversiScope) {
            GameCard(
                game = game,
                cardData = cardState,
                enabled = cardState != CardStatus.CORRUPTED,
                modifier = Modifier
                    .width(maxCardWidth)
                    .height(maxCardHeight)
                    .graphicsLayer {
                        this.scaleX = scale
                        this.scaleY = scale
                        this.alpha = alpha
                        this.translationX =
                            if (pageOffset < 0) translation.toPx() else -translation.toPx()
                    },
                onClick = { onGameClick(game, page) },
            )
        }
    }

    if (games.size > 1) {
        if (pagerState.currentPage > 0) {
            NavButton(
                icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                modifier = Modifier.testTag(testTagNavButton("left")),
                alignment = Alignment.CenterStart,
                onClick = {
                    onNavButtonClick(pagerState.currentPage - 1)
                }
            )
        }
        if (pagerState.currentPage < games.size - 1) {
            NavButton(
                icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                modifier = Modifier.testTag(testTagNavButton("right")),
                alignment = Alignment.CenterEnd,
                onClick = {
                    onNavButtonClick(pagerState.currentPage + 1)
                }
            )
        }
    } else if (games.isEmpty()) {
        Text(
            text = EMPTY_LOBBY_CAROUSEL_TEXT,
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.align(Alignment.Center)
                .testTag(testTagLobbyCarrouselEmpty())
        )
    }
}

