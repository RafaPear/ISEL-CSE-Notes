package pt.isel.reversi.app.pages.aboutPage

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import pt.isel.reversi.app.ScaffoldView
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.utils.PreviousPage
import pt.isel.reversi.utils.TRACKER

// Test Tags for About Page
fun testTagAboutPage() = "about_page"

/**
 * Simple about page presenting project and authorship information.
 *
 * @param viewModel View model providing screen state and error handling.
 * @param modifier Optional modifier to adjust layout in previews or reuse.
 * @param onLeave Callback invoked when navigating back.
 */
@Composable
fun ReversiScope.AboutPage(viewModel: AboutPageViewModel, modifier: Modifier = Modifier, onLeave: () -> Unit) {
    TRACKER.trackPageEnter(category = Page.ABOUT)
    ScaffoldView(
        setError = { error, type -> viewModel.setError(error, type) },
        error = viewModel.error,
        isLoading = viewModel.uiState.value.screenState.isLoading,
        title = "Sobre",
        previousPageContent = {
            PreviousPage { onLeave() }
        }
    ) { padding ->
        Column(
            modifier = modifier.fillMaxSize().padding(paddingValues = padding).testTag(testTagAboutPage()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(height = 24.dp))
            ReversiText("Projeto Reversi desenvolvido no ISEL.")
            ReversiText("Autores: ")
            ReversiText(" - Rafael Pereira - 52880")
            ReversiText(" - Ian Frunze - 52867")
            ReversiText(" - Tito Silva - A53118")
            ReversiText("Versão: Release 2.0")
            Spacer(Modifier.height(height = 24.dp))
            // how to play
            ReversiText("Como Jogar:")
            ReversiText("Crie um jogo em \"Novo Jogo\" ou entre num já existente a partir do lobby.")
            ReversiText("O objetivo do jogo é ter mais peças da sua cor no tabuleiro quando o jogo terminar.")
            ReversiText("Os jogadores jogam alternadamente, colocando uma peça de sua cor no tabuleiro.")
        }
    }
}
