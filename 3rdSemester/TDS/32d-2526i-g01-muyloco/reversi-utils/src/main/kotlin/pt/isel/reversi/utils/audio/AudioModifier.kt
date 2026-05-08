package pt.isel.reversi.utils.audio

/**
 * Modifier for audio playback behavior.
 *
 * @property closeOnFinish If true, the audio resource will be released when playback finishes.
 * @property gotoStartOnStop If true, the audio playback position will reset to the start when stopped.
 * @property startPosition The position (in frames) to start playback from. If null, starts from the beginning.
 * @property loop If true, the audio will loop continuously.
 * @property loopStartPosition The position (in frames) to start looping from. If null, loops from the beginning.
 * @property loopEndPosition The position (in frames) to end looping at. If null, loops until the end of the audio.
 */
data class AudioModifier(
    val closeOnFinish: Boolean = false,
    val gotoStartOnStop: Boolean = true,
    val startPosition: Int? = null,
    val loop: Boolean = false,
    val loopStartPosition: Int? = null,
    val loopEndPosition: Int? = null,
) {
    /**
     * Creates a copy of this AudioModifier with closeOnFinish set to true.
     *
     * @return A new AudioModifier instance with closeOnFinish set to true.
     */
    fun setToCloseOnFinish(): AudioModifier =
        this.copy(closeOnFinish = true)

    /**
     * Creates a copy of this AudioModifier with gotoStartOnStop set to false.
     *
     * @return A new AudioModifier instance with gotoStartOnStop set to false.
     */
    fun setToNotGoToStartOnStop(): AudioModifier =
        this.copy(gotoStartOnStop = false)

    /**
     * Creates a copy of this AudioModifier with the specified start position.
     *
     * @param position The position (in frames) to start playback from.
     * @return A new AudioModifier instance with the specified start position.
     */
    fun setStartPosition(position: Int): AudioModifier =
        this.copy(startPosition = position)

    /**
     * Creates a copy of this AudioModifier configured for looping playback.
     * @param startPosition The position (in frames) to start looping from. If null, loops from the beginning.
     * @param endPosition The position (in frames) to end looping at. If null, loops until the end of the audio.
     * @return A new AudioModifier instance configured for looping playback.
     */
    fun setToLoop(startPosition: Int? = null, endPosition: Int? = null): AudioModifier =
        this.copy(loop = true, loopStartPosition = startPosition, loopEndPosition = endPosition)

    /**
     * Creates a copy of this AudioModifier configured for infinite looping playback.
     * @return A new AudioModifier instance configured for infinite looping playback.
     */
    fun setToLoopInfinitely(): AudioModifier = this.copy(loop = true, loopStartPosition = 0, loopEndPosition = -1)
}