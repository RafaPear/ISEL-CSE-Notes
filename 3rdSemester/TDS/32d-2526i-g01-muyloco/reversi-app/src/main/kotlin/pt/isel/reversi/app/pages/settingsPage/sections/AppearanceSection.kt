package pt.isel.reversi.app.pages.settingsPage.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.app.app.AppThemes
import pt.isel.reversi.app.app.state.ReversiDropDownMenu
import pt.isel.reversi.app.app.state.ReversiDropdownMenuItem
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.pages.settingsPage.SettingsSection

fun testTagAppearanceSection() = "settings_appearance_section"
fun testTagThemeButton() = "settings_theme_button"
fun testTagThemeDropdown() = "settings_theme_dropdown"
fun testTagThemeMenuItem(themeName: String) = "settings_theme_menuitem_$themeName"

@Composable
fun ReversiScope.AppearanceSection(
    theme: AppTheme,
    appTheme: AppTheme,
    onClick: (AppTheme) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SettingsSection(
        title = "Aspeto Visual",
        modifier = Modifier.testTag(testTagAppearanceSection())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth().testTag(testTagThemeButton()),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReversiText(theme.name)
                    Icon(Icons.Default.Palette, null, tint = appTheme.primaryColor)
                }
            }

            ReversiDropDownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.testTag(testTagThemeDropdown())
            ) {
                AppThemes.entries.forEach { entry ->
                    ReversiDropdownMenuItem(
                        text = entry.appTheme.name,
                        onClick = {
                            onClick(entry.appTheme)
                            expanded = false
                        },
                        modifier = Modifier.testTag(testTagThemeMenuItem(entry.appTheme.name))
                    )
                }
            }
        }
    }
}
