package pt.isel.reversi.app.gameAudio

import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.app.exceptions.CouldNotLoadAsset
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.audio.AudioModifier
import pt.isel.reversi.utils.audio.AudioPool
import pt.isel.reversi.utils.audio.AudioPool.Companion.buildAudioPool
import pt.isel.reversi.utils.audio.AudioWrapper.Companion.loadAudio
import pt.isel.reversi.utils.loadResourcesFromFolder


/**
 * Loads the game's audio pool from resources based on the specified theme.
 * Automatically configures music tracks to loop infinitely and loads sound effects normally.
 * Handles missing or failed audio loads gracefully with warnings.
 *
 * @param theme The AppTheme containing audio resource names to load.
 * @param mainFolder The folder path containing audio resources (defaults to "audios/").
 * @param setErrorFun Callback to report non-fatal errors (missing/failed audio loads).
 * @return An AudioPool containing all successfully loaded audio tracks.
 */
fun loadGameAudioPool(
    theme: AppTheme,
    mainFolder: String = "audios/",
    setErrorFun: (ReversiException) -> Unit
): AudioPool {
    val audioNames = theme.getAudioNames().toSet()
    val musicNames = theme.getAudioMusicNames().toSet()

    val loadedAudioNames: MutableSet<String> = mutableSetOf()

    val audioPaths = loadResourcesFromFolder(mainFolder) { fileName, url ->
        val name = fileName.substringBeforeLast('.')
        try {
            val value = when (name) {
                in musicNames -> loadAudio(
                    name,
                    url,
                    AudioModifier().setToLoopInfinitely()
                )

                in audioNames -> loadAudio(name, url.toURI().toURL())
                else -> null
            }
            if (value != null) loadedAudioNames.add(name)
            value
        } catch (e: Exception) {
            LOGGER.warning("Failed to load audio $name: ${e.message}. This audio will not be available.")
            setErrorFun(CouldNotLoadAsset("Could not load audio asset $name: ${e.message}", ErrorType.WARNING))
            null
        }
    }
    val missingAudio = audioNames - loadedAudioNames
    if (missingAudio.isNotEmpty()) {
        val message = "Could not load audio assets: ${missingAudio.joinToString(", ")}"
        LOGGER.warning(message)
        setErrorFun(CouldNotLoadAsset(message, ErrorType.WARNING))
    }

    return buildAudioPool { for (audio in audioPaths) add(audio) }
}