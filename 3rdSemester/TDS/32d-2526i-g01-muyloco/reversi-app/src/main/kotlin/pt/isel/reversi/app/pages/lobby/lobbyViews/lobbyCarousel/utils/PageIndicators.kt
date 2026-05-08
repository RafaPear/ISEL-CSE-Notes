package pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText

fun testTagPageIndicators() = "page_indicators"
fun testTagPageIndicatorText() = "page_indicator_text"

fun textPageIndicator(current: Int, total: Int): String =
    if (total <= 0)
        "0 de 0"
    else
        "${current + 1} de $total"

@Composable
fun ReversiScope.PageIndicators(total: Int, current: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.testTag(testTagPageIndicators())) {
            repeat(total) { index ->
                val width by animateDpAsState(
                    targetValue = if (index == current) 28.dp else 8.dp,
                    animationSpec = spring(Spring.DampingRatioMediumBouncy),
                    label = "indicator"
                )
                Box(
                    Modifier
                        .width(width)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Color.White.copy(
                                alpha = if (index == current) 1f else 0.3f
                            )
                        )
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        val text = textPageIndicator(current, total)

        ReversiText(
            text = text,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            modifier = Modifier.testTag(testTagPageIndicatorText())
        )
    }
}