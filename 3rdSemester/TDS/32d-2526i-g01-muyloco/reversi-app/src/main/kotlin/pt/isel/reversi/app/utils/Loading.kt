package pt.isel.reversi.app.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.getTheme

/**
 * Loading overlay composable displaying a circular progress indicator with loading message.
 * Covers the entire screen with a semi-transparent overlay and prevents user interaction.
 *
 * @param modifier Optional modifier for layout adjustments.
 */
@Composable
fun ReversiScope.Loading(modifier: Modifier = Modifier) {
    Box(
        Modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(enabled = false) {}
    ) {
        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = getTheme().primaryColor, modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(16.dp))
            ReversiText("Loading...", color = Color.White, fontSize = 18.sp)
        }
    }
}