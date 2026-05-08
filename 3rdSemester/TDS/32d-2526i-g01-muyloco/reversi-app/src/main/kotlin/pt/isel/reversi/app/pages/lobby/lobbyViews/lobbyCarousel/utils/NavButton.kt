package pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

fun testTagNavButton(direction: String) = "nav_button_$direction"

/**
 * A composable function that creates a navigation button with an icon.
 *
 * @param modifier The modifier to be applied to the IconButton, test tag need is added here.
 * @param icon The icon to be displayed inside the button.
 * @param alignment The alignment of the button within its parent BoxScope.
 * @param onClick The callback to be invoked when the button is clicked.
 */
@Composable
fun BoxScope.NavButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    alignment: Alignment,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .align(alignment)
            .padding(16.dp)
            .size(50.dp)
            .background(Color(0xFF2D2D2D).copy(alpha = 0.9f), CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
    ) {
        val icons = listOf(
            Icons.AutoMirrored.Rounded.ArrowBackIos,
            Icons.AutoMirrored.Rounded.ArrowForwardIos
        )
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = if (icon in icons) Modifier.padding(start = 4.dp) else Modifier
        )
    }
}