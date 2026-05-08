package pt.isel.reversi.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.exceptions.ErrorMessage
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.utils.Loading
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException


/**
 * Main scaffold composable providing consistent layout structure for pages.
 * Includes top app bar with title and navigation, content area, and error handling.
 *
 * @param setError Callback to clear or set the current error.
 * @param backgroundTopBar Background color for the top app bar.
 * @param error Current error to render via snackbar/overlay.
 * @param isLoading When true, overlays a loading indicator.
 * @param title Title text displayed in the top app bar.
 * @param loadingModifier Modifier for the loading state overlay.
 * @param previousPageContent Composable to render the navigation/back button.
 * @param content Main page content receiving scaffold padding.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ReversiScope.ScaffoldView(
    setError: (Exception?, ErrorType?) -> Unit,
    backgroundTopBar: Color = Color.Transparent,
    error: ReversiException?,
    isLoading: Boolean = false,
    title: String = "",
    loadingModifier: Modifier = Modifier,
    previousPageContent: @Composable ReversiScope.() -> Unit,
    content: @Composable ReversiScope.(paddingValues: PaddingValues) -> Unit,
) {
    val theme = appState.theme
    val backgroundImage = theme.backgroundImage

    Box(modifier = Modifier.fillMaxSize()) {
        // Background layer

        // Scaffold layer
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backgroundTopBar,
                    ),
                    title = {
                        ReversiText(
                            text = title,
                            color = theme.textColor,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            autoSize = TextAutoSize.StepBased(
                                maxFontSize = 50.sp
                            ),
                            maxLines = 1,
                            softWrap = false,
                        )
                    },
                    navigationIcon = {
                        if (!isLoading && appState.pagesState.page != Page.MAIN_MENU) {
                            previousPageContent()
                        }
                    }
                )
            },
            snackbarHost = { ErrorMessage(error) { it, type -> setError(it, type) } }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                backgroundImage?.let { imageRes ->
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.2f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Box {
                content(paddingValues)
                if (isLoading) {
                    Loading(loadingModifier)
                }
            }
        }
    }
}