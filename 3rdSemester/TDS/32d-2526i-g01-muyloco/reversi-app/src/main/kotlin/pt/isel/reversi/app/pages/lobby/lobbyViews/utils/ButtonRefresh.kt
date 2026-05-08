package pt.isel.reversi.app.pages.lobby.lobbyViews.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Icon button for refreshing the lobby game list.
 *
 * @param onClick Callback invoked when the refresh button is clicked.
 */
@Composable
fun RefreshButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center, // Garante o centro absoluto
        modifier = Modifier
            .size(48.dp) // Tamanho da área de toque
            .clip(CircleShape) // (Opcional) Deixa o ripple redondo
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = Icons.Rounded.Refresh,
            contentDescription = "Refresh",
            tint = Color.White.copy(alpha = 0.9f),
        )
    }
}