package pt.isel.reversi.utils.audio

import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.audio.controls.BooleanControlWrapper
import pt.isel.reversi.utils.audio.controls.FloatControlWrapper
import java.net.URL
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

/**
 * A wrapper for an audio Clip, providing methods to control playback and audio properties.
 *
 * @property id The identifier for the audio clip.
 * @property clip The Clip object representing the audio data.
 * @property modifier The AudioModifier containing playback settings.
 * @constructor Creates an AudioWrapper with the specified id, clip, and modifier.
 *
 * @throws IllegalArgumentException if the clip cannot be opened.
 *
 *
 * @see AudioModifier
 */
data class AudioWrapper(
    val id: String,
    private val clip: Clip,
    val modifier: AudioModifier = AudioModifier()
) {
    // Audio controls
    val masterGainControl = FloatControlWrapper.MasterVolumeControl(clip)
    val balanceControl = FloatControlWrapper.BalanceControl(clip)
    val muteControl = BooleanControlWrapper.MuteControl(clip)

    private val loopStart = modifier.loopStartPosition?.coerceIn(0, clip.frameLength - 1) ?: 0
    private val loopEnd = modifier.loopEndPosition?.coerceIn(-1, clip.frameLength - 1) ?: -1

    init {
        clip.framePosition = modifier.startPosition?.coerceIn(0, clip.frameLength - 1) ?: 0
        LOGGER.info("AUDIO '${id}' initialized")
    }

    fun isLoaded(): Boolean = clip.isOpen

    /**
     * Checks if the audio clip is currently playing.
     *
     * @return True if the clip is running, false otherwise.
     */
    fun isPlaying(): Boolean = clip.isRunning

    /**
     * Starts playback of the audio clip. If the clip is set to loop, it will loop continuously.
     */
    fun play() {
        if (clip.framePosition >= clip.frameLength)
            clip.framePosition = modifier.startPosition?.coerceIn(0, clip.frameLength - 1) ?: 0

        if (!clip.isRunning) {
            if (modifier.loop) {
                clip.setLoopPoints(loopStart, loopEnd)
                clip.loop(Clip.LOOP_CONTINUOUSLY)
            } else clip.start()
        }
        var elapsed = 0L
        while (!clip.isRunning) {
            Thread.sleep(1)
            elapsed++
            if (elapsed > 2000) {
                LOGGER.warning("AUDIO '${id}' failed to start playing after 2 seconds")
                break
            }
        }
    }

    /**
     * Pauses playback of the audio clip.
     */
    fun pause() {
        if (clip.isRunning) clip.stop()
    }

    /**
     * Stops playback of the audio clip. Depending on the modifier settings, it may reset to the start position or close the clip.
     */
    fun stop() {
        clip.stop()
        if (modifier.gotoStartOnStop) clip.framePosition = 0
        if (modifier.closeOnFinish) clip.close()
    }

    /**
     * Closes the audio clip, releasing any system resources associated with it.
     */
    fun close() {
        clip.close()
    }

    companion object {
        /**
         * Loads an audio clip from the specified URL and applies the given modifier.
         *
         * @param name The name identifier for the audio clip.
         * @param url The URL of the audio resource.
         * @param modifier The AudioModifier containing playback settings.
         * @return An AudioWrapper instance containing the loaded audio clip.
         * @throws Exception if there is an error loading the audio.
         */
        fun loadAudio(name: String, url: URL, modifier: AudioModifier = AudioModifier()): AudioWrapper {
            val original = AudioSystem.getAudioInputStream(url)

            val baseFormat = original.format
            val decodedFormat = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.sampleRate,
                16,
                baseFormat.channels,
                baseFormat.channels * 2,
                baseFormat.sampleRate,
                false
            )

            val decoded = AudioSystem.getAudioInputStream(decodedFormat, original)

            val clip = AudioSystem.getClip()

            val mixers = AudioSystem.getMixerInfo()
            if (mixers.isNotEmpty())
                clip.open(decoded)

            return AudioWrapper(name, clip, modifier)
        }
    }
}