package pt.isel.reversi.app.pages.game

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.game.utils.*
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.game.Game

/**
 * Main game view composable displaying the board and player information side-by-side.
 * Combines the game board on the left with player scores and controls on the right.
 *
 * @param modifier Optional composable modifier for layout adjustments.
 * @param game The current game instance containing state and configuration.
 * @param freeze Whether to disable board interactions.
 * @param getAvailablePlays Lambda returning list of valid move coordinates.
 * @param onCellClick Callback invoked when a board cell is clicked.
 * @param setTargetMode Callback invoked to toggle target mode visibility.
 */
@Composable
fun ReversiScope.GamePageView(
    modifier: Modifier = Modifier,
    game: Game,
    freeze: Boolean,
    getAvailablePlays: () -> List<Coordinate>,
    onCellClick: (coordinate: Coordinate) -> Unit,
    setTargetMode: (target: Boolean) -> Unit,
    pass: () -> Unit,
) {
    Column(
        modifier = modifier.testTag(tag = testTagGamePage()),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
        ) {
            Box(
                modifier = Modifier.weight(0.7f),
            ) {
                val gameState = game.gameState ?: return@Box
                DrawBoard(
                    game.target,
                    gameState,
                    freeze = freeze,
                    getAvailablePlays = { getAvailablePlays() }
                ) { onCellClick(it) }
            }

            Spacer(modifier = Modifier.width(width = 16.dp))

            // Coluna dos jogadores e botões
            Column(
                modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val myPiece = game.myPiece
                if (myPiece != null) {
                    TextPlayersScore(
                        state = game.gameState
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                val target = game.target
                val lastPlayer = game.gameState?.lastPlayer
                val canPass =
                    if (game.currGameName == null ) {
                        getAvailablePlays().isEmpty()
                    } else {
                        getAvailablePlays().isEmpty() && lastPlayer != null && lastPlayer != game.myPiece
                    }

                TargetButton(target, freeze = freeze) {
                    setTargetMode(!target)
                }

                Spacer(modifier = Modifier.height(32.dp))

                PassButton(
                    canPass = canPass,
                    freeze = freeze
                ) { pass() }
            }
        }
    }
}