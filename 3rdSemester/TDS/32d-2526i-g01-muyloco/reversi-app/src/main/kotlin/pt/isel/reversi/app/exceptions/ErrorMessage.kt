package pt.isel.reversi.app.exceptions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.app.state.safeKillApp
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.utils.LOGGER

/**
 * Composable that displays an error message based on the error type.
 * @param error Current error to show (null hides the message).
 * @param modifier Optional modifier for styling the composable.
 * @param setError Callback to clear or update the error.
 */
@Composable
fun ReversiScope.ErrorMessage(
    error: ReversiException?,
    modifier: Modifier = Modifier,
    setError: (Exception?, ErrorType?) -> Unit
) {
    // Avoid logging on every recomposition, only when error changes
    LaunchedEffect(error) {
        val error = error ?: return@LaunchedEffect
        when (error.type) {
            ErrorType.INFO -> LOGGER.info("${error.message}")
            ErrorType.WARNING -> LOGGER.warning("${error.message}")
            ErrorType.ERROR -> LOGGER.severe("${error.message}")
            ErrorType.CRITICAL -> LOGGER.severe("Critical ${error.message}")
        }
    }

    when (error?.type) {
        ErrorType.INFO -> ToastMessage(error, modifier, setError)
        ErrorType.WARNING -> WarningMessage(error, modifier, setError)
        ErrorType.ERROR -> ErrorMessageDialog(error, modifier, setError)
        ErrorType.CRITICAL -> CriticalMessageDialog(error, modifier, setError)
        null -> return
    }
}

@Composable
fun ReversiScope.WarningMessage(
    error: ReversiException?,
    modifier: Modifier = Modifier,
    setError: (Exception?, ErrorType?) -> Unit
) {
    val errorMessage = error?.message ?: return

    val overlayColor = Color.Black.copy(alpha = 0.6f)
    val warningBackgroundColor = Color(0xFFFFCC80)
    val buttonBackgroundColor = Color(0xFFFFA000)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(overlayColor)
            .clickable(enabled = false, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(warningBackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color.Black,
                modifier = Modifier.size(48.dp)
            )

            ReversiText(
                text = errorMessage,
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                softWrap = true,
                maxLines = Int.MAX_VALUE
            )

            Button(
                onClick = { setError(null, null) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                ReversiText(text = "OK", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ReversiScope.ErrorMessageDialog(
    error: ReversiException?,
    modifier: Modifier = Modifier,
    setError: (Exception?, ErrorType?) -> Unit
) {
    val errorMessage = error?.message ?: return

    val overlayColor = Color.Black.copy(alpha = 0.6f)
    val errorBackgroundColor = Color(0xFFFFCDD2)
    val buttonBackgroundColor = Color(0xFFD32F2F)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(overlayColor)
            .clickable(enabled = false, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(errorBackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(48.dp)
            )

            ReversiText(
                text = errorMessage,
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                softWrap = true,
                maxLines = Int.MAX_VALUE
            )

            Button(
                onClick = { setError(null, null) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                ReversiText(text = "OK", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ReversiScope.CriticalMessageDialog(
    error: ReversiException?,
    modifier: Modifier = Modifier,
    setError: (Exception?, ErrorType?) -> Unit
) {
    val errorMessage = error?.message ?: return

    val overlayColor = Color.Black.copy(alpha = 0.7f)
    val criticalBackgroundColor = Color(0xFFFF3434)
    val buttonBackgroundColor = Color(0xFFFFABAB)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(overlayColor)
            .clickable(enabled = false, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(12.dp))
                .background(criticalBackgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Critical",
                tint = Color(0xFFB71C1C),
                modifier = Modifier.size(56.dp)
            )

            ReversiText(
                text = errorMessage,
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                softWrap = true,
                maxLines = Int.MAX_VALUE
            )

            Button(
                onClick = {
                    setError(null, null)
                    safeKillApp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonBackgroundColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                ReversiText(text = "OK", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Composable that shows a toast message for errors.
 * The message is displayed for 2 seconds before being cleared.
 * @param error Current error to show.
 * @param modifier Optional modifier for styling the composable.
 * @param setError Callback to clear the error after display.
 */
@Composable
fun ReversiScope.ToastMessage(
    error: ReversiException?,
    modifier: Modifier = Modifier,
    setError: (Exception?, ErrorType?) -> Unit
) {
    val offsetY = remember { Animatable(-100f) }
    val message = error?.message

    val slideDuration = 300
    val displayDuration = 2000L

    val infoBackgroundColor = Color(0xFFFF5722)

    val infoTextColor = Color.White.copy(alpha = 0.8f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(x = 0, y = offsetY.value.toInt()) }
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .background(infoBackgroundColor)
                .height(IntrinsicSize.Min)
        ) {
            ReversiText(
                text = message ?: return@Box,
                color = infoTextColor,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                fontWeight = FontWeight.Bold,
            )
        }
    }

    LaunchedEffect(key1 = error, key2 = message) {
        if (error == null || message == null) return@LaunchedEffect

        offsetY.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = slideDuration)
        )
        delay(displayDuration)

        offsetY.animateTo(
            targetValue = -100f,
            animationSpec = tween(durationMillis = slideDuration)
        )
        setError(null, null)
    }
}
