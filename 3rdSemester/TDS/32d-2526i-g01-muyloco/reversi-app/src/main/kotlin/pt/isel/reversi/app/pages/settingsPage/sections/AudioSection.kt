package pt.isel.reversi.app.pages.settingsPage.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.ReversiText
import pt.isel.reversi.app.pages.settingsPage.SettingsSection

fun testTagAudioSection() = "settings_audio_section"
fun testTagVolumeSlider() = "settings_volume_slider"
fun testTagVolumeLabelText() = "settings_volume_label"
const val DEFAULT_MIN_VOLUME = -20f
const val DEFAULT_MAX_VOLUME = 0f

fun Float.toPercent(minVol: Float, maxVol: Float) =
    (((this - minVol) / (maxVol - minVol)) * 100).toInt().coerceIn(0, 100)

@Composable
fun ReversiScope.AudioSection(
    currentVol: Float,
    onVolumeChange: (Float) -> Unit
) {
    SettingsSection(
        title = "Áudio",
        modifier = Modifier.testTag(testTagAudioSection())
    ) {
        val (minVol, maxVol) = appState.audioPool.getMasterVolumeRange() ?: (DEFAULT_MIN_VOLUME to DEFAULT_MAX_VOLUME)
        val percent = currentVol.toPercent(minVol, maxVol)
        val volumeLabel = if (currentVol <= minVol) "Mudo" else "$percent%"

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ReversiText("Volume Geral", fontSize = 16.sp)
            ReversiText(
                volumeLabel,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(testTagVolumeLabelText())
            )
        }

        Slider(
            value = currentVol,
            valueRange = minVol..maxVol,
            onValueChange = onVolumeChange,
            modifier = Modifier.testTag(testTagVolumeSlider()),
            colors = SliderDefaults.colors(
                thumbColor = appState.theme.primaryColor,
                activeTrackColor = appState.theme.primaryColor,
                inactiveTrackColor = appState.theme.textColor.copy(alpha = 0.3f)
            )
        )
    }
}
