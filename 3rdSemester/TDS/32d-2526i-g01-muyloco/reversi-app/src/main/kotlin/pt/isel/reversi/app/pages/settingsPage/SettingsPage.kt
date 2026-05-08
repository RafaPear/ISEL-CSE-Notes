package pt.isel.reversi.app.pages.settingsPage

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.ScaffoldView
import pt.isel.reversi.app.app.state.ReversiButton
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.getTheme
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.settingsPage.sections.AppearanceSection
import pt.isel.reversi.app.pages.settingsPage.sections.AudioSection
import pt.isel.reversi.app.pages.settingsPage.sections.CoreConfigSection
import pt.isel.reversi.app.pages.settingsPage.sections.GameSection
import pt.isel.reversi.app.utils.PreviousPage
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.utils.TRACKER

// Test Tags for Settings Page
fun testTagSettingsPage() = "settings_page"
fun testTagApplyButton() = "settings_apply_button"

/**
 * Section header composable for organizing settings into logical groups.
 * Displays a title and divider line with the section content below.
 *
 * @param title The section title/header.
 * @param modifier Optional modifier for the section.
 * @param content Lambda for the section's content composables.
 */
@Composable
fun ReversiScope.SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReversiText(
            text = title,
            color = getTheme().primaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        HorizontalDivider(
            color = getTheme().textColor.copy(alpha = 0.1f),
            thickness = 1.dp
        )
        content()
    }
}

/**
 * Settings page displaying configuration options including player name, storage, audio, and theme.
 * Provides apply action to persist changes and navigate back.
 *
 * @param viewModel The ViewModel managing the settings state and logic.
 * @param onLeave Callback invoked when leaving the settings page.
 */
@Composable
fun ReversiScope.SettingsPage(
    viewModel: SettingsViewModel,
    onLeave: () -> Unit
) {
    TRACKER.trackRecomposition(category = Page.SETTINGS)
    val draftPlayerName = viewModel.uiState.value.draftPlayerName
    val draftTheme = viewModel.uiState.value.draftTheme
    val draftCoreConfig = viewModel.uiState.value.draftCoreConfig
    val currentVol = viewModel.uiState.value.currentVol

    ScaffoldView(
        setError = { error, type ->
            viewModel.setError(error, type)
            if (error == null && viewModel.error?.type != ErrorType.INFO) onLeave()
        },
        error = viewModel.error,
        isLoading = viewModel.uiState.value.screenState.isLoading,
        title = "Definições",
        previousPageContent = {
            PreviousPage { onLeave() }
        }
    ) { padding ->
        val scrollState = rememberScrollState(0)

        Box(
            modifier = Modifier.fillMaxSize().padding(padding).testTag(testTagSettingsPage()),
            contentAlignment = Alignment.TopCenter
        ) {

            // Scroll Bar for Desktop
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = scrollState
                ),
                style = ScrollbarStyle(
                    minimalHeight = 16.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4.dp),
                    hoverDurationMillis = 300,
                    unhoverColor = getTheme().primaryColor.copy(alpha = 0.12f),
                    hoverColor = getTheme().primaryColor.copy(alpha = 0.24f)
                )
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.9f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {

                GameSection(
                    playerName = draftPlayerName,
                    onValueChange = { viewModel.setDraftPlayerName(it) }
                )

                CoreConfigSection(
                    coreConfig = draftCoreConfig,
                    onConfigChange = { viewModel.setDraftCoreConfig(it) }
                )

                AudioSection(
                    currentVol = currentVol,
                    onVolumeChange = { viewModel.setCurrentVol(it) },
                )

                AppearanceSection(
                    theme = draftTheme,
                    appTheme = appState.theme
                ) { viewModel.setDraftTheme(it) }

                // Apply button
                ApplyButton {
                    TRACKER.trackFunctionCall(details = "Apply settings clicked")
                    viewModel.applySettings(
                        oldTheme = appState.theme,
                        newName = draftPlayerName?.ifEmpty { appState.playerName },
                        newTheme = draftTheme,
                        draftCoreConfig = draftCoreConfig,
                        volume = currentVol,
                        endAction = onLeave
                    )
                }
            }
        }
    }
}


@Composable
private fun ReversiScope.ApplyButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        ReversiButton(text = "Aplicar", onClick = onClick, modifier = Modifier.testTag(testTagApplyButton()))
    }
}