package pt.isel.reversi.app.pages.settingsPage.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.ReversiTextField
import pt.isel.reversi.app.pages.settingsPage.SettingsSection

fun testTagGameSection() = "settings_game_section"
fun testTagPlayerNameSettings() = "settings_player_name_textfield"

@Composable
fun ReversiScope.GameSection(playerName: String?, onValueChange: (String) -> Unit) {
    SettingsSection(
        title = "Jogo",
        modifier = Modifier.testTag(testTagGameSection())
    ) {
        ReversiTextField(
            value = playerName ?: "",
            onValueChange = { onValueChange(it) },
            label = { ReversiText("Nome do Jogador") },
            modifier = Modifier.fillMaxWidth().testTag(testTagPlayerNameSettings())
        )
    }
}