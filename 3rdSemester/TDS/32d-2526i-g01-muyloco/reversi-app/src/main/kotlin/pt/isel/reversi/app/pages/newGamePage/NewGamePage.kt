package pt.isel.reversi.app.pages.newGamePage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.ScaffoldView
import pt.isel.reversi.app.app.state.*
import pt.isel.reversi.app.utils.PreviousPage
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game

// Test Tags for New Game Page
fun testTagNewGamePage() = "new_game_page"
fun testTagLocalGameCheckbox() = "new_game_local_checkbox"
fun testTagGameNameTextField() = "new_game_name_textfield"
fun testTagPlayerNameTextField() = "new_game_player_name_textfield"
fun testTagBoardSizeTextField() = "new_game_board_size_textfield"
fun testTagPieceDropdown() = "new_game_piece_dropdown"
fun testTagPieceDropdownBlack() = "new_game_piece_dropdown_black"
fun testTagPieceDropdownWhite() = "new_game_piece_dropdown_white"
fun testTagStartGameButton() = "new_game_start_button"
fun testTagDropdownButton() = "new_game_dropdown_button"

/**
 * New game page for creating a local game with piece selection.
 * Allows the user to choose their piece color and start a new game.
 *
 * @param viewModel The view model for managing game creation state and logic.
 * @param playerNameChange Callback to update the player name.
 * @param onLeave Callback invoked when the user navigates back.
 */
@Composable
fun ReversiScope.NewGamePage(
    viewModel: NewGameViewModel,
    playerNameChange: (String) -> Unit,
    onLeave: () -> Unit,
) {
    val title = "Novo Jogo"

    ScaffoldView(
        setError = { error, type -> viewModel.setError(error, type) },
        error = viewModel.error,
        isLoading = viewModel.uiState.value.screenState.isLoading,
        title = title,
        previousPageContent = {
            PreviousPage { onLeave() }
        }
    ) { padding ->
        NewGamePageView(modifier = Modifier.padding(padding)) { game, boardSize, name ->
            viewModel.tryCreateGame(game, boardSize, name)
        }
    }
}

/**
 * Shared UI for new game page with piece selection and game initiation.
 * Provides input fields for game name, player name, board size, and a dropdown menu for piece selection.
 *
 * @param modifier The modifier for layout customization.
 * @param onClick Callback invoked when the user confirms game creation with selected piece, board size and player name.
 */
@Composable
private fun ReversiScope.NewGamePageView(
    modifier: Modifier,
    onClick: (Game, Int, String?) -> Unit
) {
    var game by remember { mutableStateOf(getCurrentState().game.copy(currGameName = "")) }
    var playerName by remember { mutableStateOf(appState.playerName ?: "") }
    var boardSize by remember { mutableStateOf("8") }
    var expanded by remember { mutableStateOf(false) }
    var isLocal by remember { mutableStateOf(game.currGameName.isNullOrBlank()) }
    val theme = getTheme()

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(testTagNewGamePage()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 24.dp)
                .widthIn(max = 400.dp)
                .fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ReversiText("Jogo local", fontSize = 16.sp)
                    ReversiText(
                        text = if (!isLocal) "Sem nome cria jogo local" else "Desmarcar cria jogo multijogador",
                        color = theme.textColor.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        maxLines = 2,
                        softWrap = true,
                    )
                }
                ReversiCheckbox(
                    checked = isLocal,
                    onCheckedChange = {
                        isLocal = it
                        if (it) game = game.copy(currGameName = "")
                    },
                    modifier = Modifier.testTag(testTagLocalGameCheckbox())
                )
            }

            if (!isLocal) {
                ReversiTextField(
                    value = game.currGameName ?: "",
                    onValueChange = {
                        isLocal = it.isBlank()
                        game = game.copy(currGameName = it)
                    },
                    label = { ReversiText("Nome do jogo (multijogador)") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagGameNameTextField()),
                    enabled = !isLocal,
                )
            }

            if (!isLocal) {
                ReversiTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { ReversiText("Nome de jogador") },
                    modifier = Modifier.fillMaxWidth().testTag(testTagPlayerNameTextField()),
                )
            }

            ReversiTextField(
                value = boardSize,
                onValueChange = { boardSize = it },
                label = { ReversiText("Tamanho do Tabuleiro (4-26)") },
                modifier = Modifier.fillMaxWidth().testTag(testTagBoardSizeTextField()),
            )


            val selectedPieceLabel = remember(game.myPiece) {
                when (game.myPiece) {
                    PieceType.BLACK -> "Preto"
                    PieceType.WHITE -> "Branco"
                    else -> "Selecionar cor..."
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                ReversiText(
                    "A minha peça:",
                    color = theme.textColor.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                Box(modifier = Modifier.run { fillMaxWidth() }) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth().testTag(testTagDropdownButton()),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, theme.textColor.copy(0.2f)),

                        colors = ButtonDefaults.outlinedButtonColors(contentColor = theme.textColor)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ReversiText(selectedPieceLabel)
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = theme.textColor
                            )
                        }
                    }

                    ReversiDropDownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.widthIn(min = 200.dp).testTag(testTagPieceDropdown()),
                    ) {
                        ReversiDropdownMenuItem(
                            text = "Preto",
                            onClick = {
                                game = game.changeMyPiece(PieceType.BLACK)
                                expanded = false
                            },
                            modifier = Modifier.testTag(testTagPieceDropdownBlack()),
                        )
                        ReversiDropdownMenuItem(
                            text = "Branco",
                            onClick = {
                                game = game.changeMyPiece(PieceType.WHITE)
                                expanded = false
                            },
                            modifier = Modifier.testTag(testTagPieceDropdownWhite())
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            ReversiButton(
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag(testTagStartGameButton()),
                onClick = {
                    val preparedGame = if (isLocal) game.copy(currGameName = "") else game
                    onClick(preparedGame, boardSize.parseBoardSize(), playerName.ifBlank { null })
                },
                text = "Entrar"
            )
        }

    }
}

private fun String.parseBoardSize(): Int {
    return toIntOrNull() ?: 0
}
