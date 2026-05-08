package pt.isel.reversi.app.pages.game.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import pt.isel.reversi.app.app.state.ReversiButton
import pt.isel.reversi.app.app.state.ReversiScope

/**
 * Toggle button for target mode, showing available moves on the board.
 * Displays "Target ON" or "Target OFF" based on the current state.
 *
 * @param target Whether target mode is currently enabled.
 * @param modifier Optional composable modifier for layout adjustments.
 * @param freeze Whether the button is disabled due to game frozen state.
 * @param onClick Callback invoked when the button is clicked.
 */
@Composable
fun ReversiScope.TargetButton(target: Boolean, modifier: Modifier = Modifier, freeze: Boolean, onClick: () -> Unit) {
    val targetText = if (target) "ON" else "OFF"

    ReversiButton(
        text = "Target $targetText",
        modifier = modifier.testTag(tag = testTagTargetButtons(target)),
        onClick = onClick,
        enabled = !freeze,
    )
}

/**
 * Button to pass the current player's turn.
 *
 * @param modifier Optional composable modifier for layout adjustments.
 * @param freeze Whether the button is disabled due to game frozen state.
 * @param onClick Callback invoked when the button is clicked.
 */
@Composable
fun ReversiScope.PassButton(modifier: Modifier = Modifier, canPass: Boolean, freeze: Boolean, onClick: () -> Unit) {
    ReversiButton(
        text = "Pass",
        onClick = onClick,
        enabled = !freeze && canPass,
    )
}