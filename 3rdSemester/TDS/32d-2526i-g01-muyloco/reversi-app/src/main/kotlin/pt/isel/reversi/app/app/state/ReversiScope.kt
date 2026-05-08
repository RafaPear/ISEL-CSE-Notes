package pt.isel.reversi.app.app.state

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.pages.menu.MAIN_MENU_AUTO_SIZE_BUTTON_TEXT

/**
 * Receiver scope class providing composition helper functions and access to app state.
 * Used as a receiver for composable lambdas to provide themed UI components.
 *
 * @property appState The current application state.
 */
class ReversiScope(val appState: AppStateImpl)

/**
 * Retrieves the current application state.
 *
 * @return The AppState held in this scope.
 */
fun ReversiScope.getCurrentState() = appState

/**
 * Retrieves the current application theme.
 *
 * @return The AppTheme from the current application state.
 */
fun ReversiScope.getTheme() = getCurrentState().theme

/**
 * Themed text composable following the application's color scheme.
 * Supports auto-sizing and various text styling options.
 *
 * @param text The text content to display.
 * @param color The text color (defaults to theme text color).
 * @param autoSize Optional auto-sizing configuration for dynamic font scaling.
 * @param fontSize Fixed font size (overrides auto-sizing if specified).
 * @param modifier Composable modifier for layout adjustments.
 * @param fontWeight Optional font weight for the text.
 * @param maxLines Maximum number of lines to display.
 * @param softWrap Whether to wrap text across multiple lines.
 * @param fontStyle Font style (normal, italic, etc.).
 * @param textAlign Horizontal text alignment.
 * @param overflow Text overflow behavior.
 */
@Composable
fun ReversiScope.ReversiText(
    text: String,
    color: Color = getTheme().textColor,
    autoSize: TextAutoSize? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
    maxLines: Int = 1,
    softWrap: Boolean = false,
    fontStyle: FontStyle = FontStyle.Normal,
    textAlign: TextAlign = TextAlign.Start,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontFamily = getTheme().fontFamily(),
        autoSize = autoSize,
        modifier = modifier,
        fontWeight = fontWeight,
        maxLines = maxLines,
        softWrap = softWrap,
        fontStyle = fontStyle,
        textAlign = textAlign,
        overflow = overflow,
    )
}

/**
 * Themed checkbox component following the application's design system.
 *
 * @param checked Whether the checkbox is currently checked.
 * @param onCheckedChange Callback invoked when the checkbox state changes.
 * @param modifier Composable modifier for layout adjustments.
 * @param enabled Whether the checkbox is interactive.
 */
@Composable
fun ReversiScope.ReversiCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val theme = getTheme()
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = theme.primaryColor,
            uncheckedColor = theme.textColor.copy(alpha = 0.6f),
            checkmarkColor = theme.buttonTextColor,
            disabledCheckedColor = theme.primaryColor.copy(alpha = 0.4f),
            disabledUncheckedColor = theme.textColor.copy(alpha = 0.4f),
        )
    )
}

/**
 * Themed button component using the application's primary color.
 * Supports custom shapes, borders, and disabled state styling.
 *
 * @param text The text label for the button.
 * @param modifier Composable modifier for layout adjustments.
 * @param onClick Callback invoked when the button is clicked.
 * @param enabled Whether the button is clickable.
 * @param shape The shape of the button corners.
 * @param border Optional custom border stroke.
 */
@Composable
fun ReversiScope.ReversiButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    border: BorderStroke? = null,
    onClick: () -> Unit,
) {
    val theme = getTheme()
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = theme.primaryColor,
            disabledContainerColor = theme.primaryColor.copy(alpha = 0.5f)
        ),
        enabled = enabled,
        shape = shape,
        border = border ?: BorderStroke(width = 1.dp, color = theme.buttonTextColor),

        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ReversiText(
            text = text,
            color = theme.buttonTextColor,
            // The use of autoSize is already an excellent practice for adaptability
            autoSize = MAIN_MENU_AUTO_SIZE_BUTTON_TEXT,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Themed dropdown menu with application color scheme.
 * Provides a container for dropdown menu items with consistent styling.
 *
 * @param expanded Whether the menu is currently displayed.
 * @param onDismissRequest Callback invoked when the menu should close.
 * @param modifier Composable modifier for layout adjustments.
 * @param content Lambda for menu items content.
 */
@Composable
fun ReversiScope.ReversiDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ReversiScope.() -> Unit,
) {
    val theme = getTheme()
    DropdownMenu(
        modifier = modifier.background(theme.backgroundColor),
        expanded = expanded,
        shape = RoundedCornerShape(12.dp),
        onDismissRequest = onDismissRequest,
    ) {
        CompositionLocalProvider(LocalContentColor provides theme.textColor) {
            content()
        }
    }
}

/**
 * Themed dropdown menu item for use within ReversiDropDownMenu.
 * Provides consistent styling for menu options.
 *
 * @param text The text label for the menu item.
 * @param onClick Callback invoked when the menu item is selected.
 * @param modifier Composable modifier for layout adjustments.
 * @param enabled Whether the menu item is selectable.
 */
@Composable
fun ReversiScope.ReversiDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val theme = getTheme()
    DropdownMenuItem(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth(),
        text = {
            ReversiText(
                text = text,
                color = theme.textColor,
                fontSize = 16.sp,
            )
        },
        onClick = onClick,
        colors = MenuDefaults.itemColors(
            textColor = theme.textColor,
            trailingIconColor = theme.textColor,
            leadingIconColor = theme.textColor,
            disabledTextColor = theme.textColor.copy(alpha = 0.3f),
        ),
        enabled = enabled
    )
}

/**
 * Themed text input field following the application's design system.
 * Supports placeholder, label, and single/multi-line modes.
 *
 * @param value Current text value in the field.
 * @param onValueChange Callback invoked when the text content changes.
 * @param placeholder Optional placeholder content.
 * @param label Optional label content.
 * @param singleLine Whether the field accepts only a single line of input.
 * @param modifier Composable modifier for layout adjustments.
 * @param onDone Callback invoked when the user completes input (IME action).
 * @param enabled Whether the field is editable.
 */
@Composable
fun ReversiScope.ReversiTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: @Composable () -> Unit = {},
    label: @Composable () -> Unit = {},
    singleLine: Boolean = true,
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},
    enabled: Boolean = true,
) {
    val theme = getTheme()
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(fontFamily = theme.fontFamily()),
        label = label,
        placeholder = placeholder,
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = theme.primaryColor,
            unfocusedIndicatorColor = theme.textColor.copy(0.3f),
            cursorColor = theme.primaryColor,
            focusedTextColor = theme.textColor,
            unfocusedTextColor = theme.textColor,
            focusedLabelColor = theme.primaryColor,
            unfocusedLabelColor = theme.textColor
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        ),
        enabled = enabled
    )
}

fun testTagButtonConfirm() = "PopUpConfirmButton"
/**
 * Modal popup for confirming user actions.
 *
 * @param message The confirmation message to display.\
 * @param onConfirm Callback invoked when the user confirms the action.
 * @param onDismiss Callback invoked when the popup is closed without selection.
 */
@Composable
fun ReversiScope.ConfirmationPopUp(
    title: String,
    message: String,
    onConfirmText: String = "Confirm",
    onDismissText: String = "Cancel",
    onConfirm: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    if (onDismiss != null) onDismiss()
                }
            }
    ) {
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { })
                }
                .background(getTheme().secondaryColor, RoundedCornerShape(16.dp))
                .border(
                    1.dp,
                    getTheme().secondaryColor.invert().copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ReversiText(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                ReversiText(
                    text = message,
                    textAlign = TextAlign.Center,
                    maxLines = 10,
                    softWrap = true,
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    if (onConfirm != null)
                        ReversiButton(
                            text = onConfirmText,
                            onClick = onConfirm,
                            modifier = Modifier.testTag(testTagButtonConfirm())
                        )

                    if (onDismiss != null)
                        ReversiButton(
                            text = onDismissText,
                            onClick = onDismiss,
                        )
                }
            }
        }
    }
}

fun Color.invert(): Color {
    return Color(
        red = 1f - this.red,
        green = 1f - this.green,
        blue = 1f - this.blue,
        alpha = this.alpha
    )
}