package pt.isel.reversi.utils.audio

import kotlinx.coroutines.delay
import pt.isel.reversi.utils.LOGGER

/**
 * Represents a pool of audio tracks that can be managed together.
 * @param pool The list of AudioWrapper instances in the pool.
 */
@Suppress("Unused")
class AudioPool(startPool: List<AudioWrapper>) {

    val pool: MutableList<AudioWrapper> = startPool.toMutableList()

    init {
        LOGGER.info("AudioPool created with ${pool.size} audio tracks")
        resetBalance()
        resetMasterVolume()
    }

    /**
     * Merges this AudioPool with another AudioPool.
     * New audio tracks from the other pool are added to this pool.
     * Duplicate audio tracks (by ID) from the new pool are not added.
     * Tracks from this pool take precedence in case of ID conflicts.
     * Old tracks that do not exist in the other pool are destroyed
     * and removed from this pool.
     *
     * @param other The other AudioPool to merge with.
     */
    fun merge(other: AudioPool) {
        val iterator = pool.iterator()
        while (iterator.hasNext()) {
            val old = iterator.next()
            if (other.pool.none { it.id == old.id }) {
                old.close()
                iterator.remove()
            }
        }

        other.pool.forEach { newTrack ->
            if (pool.none { it.id == newTrack.id }) {
                pool.add(newTrack)
            }
        }
    }

    /**
     * Plays the audio track with the specified ID.
     * @param id The ID of the audio track to play.
     */
    fun play(id: String) {
        val track = getAudioTrack(id) ?: return
        track.play()
    }

    /**
     * Stops the audio track with the specified ID.
     * @param id The ID of the audio track to stop.
     */
    fun stop(id: String) {
        val track = getAudioTrack(id) ?: return
        track.stop()
    }

    /**
     * Pauses the audio track with the specified ID.
     * @param id The ID of the audio track to pause.
     */
    fun pause(id: String) {
        val track = getAudioTrack(id) ?: return
        track.pause()
    }

    /**
     * Gets the audio track with the specified ID.
     * @param id The ID of the audio track to retrieve.
     * @return The AudioWrapper instance if found and loaded, null otherwise.
     */
    fun getAudioTrack(id: String): AudioWrapper? {
        return pool.find { it.id == id && it.isLoaded() }
    }

    /**
     * Plays all audio tracks in the pool.
     */
    fun playAll() {
        pool.forEach { it.play() }
    }

    /**
     * Stops all audio tracks in the pool.
     */
    fun stopAll() {
        pool.forEach { it.stop() }
    }

    /**
     * Pauses all audio tracks in the pool.
     */
    fun pauseAll() {
        pool.forEach { it.pause() }
    }

    /**
     * Destroys the audio pool by closing all audio clips.
     */
    fun destroy() {
        pool.forEach { it.close() }
    }

    /**
     * Checks if the audio track with the specified ID is currently playing.
     * @param id The ID of the audio track to check.
     * @return True if the track is playing, false otherwise.
     */
    fun isPlaying(id: String): Boolean {
        val track = getAudioTrack(id)
        return track?.isPlaying() ?: false
    }

    /**
     * Executes a function while any audio track in the pool is still playing.
     * @param func The function to execute.
     */
    fun whileNotFinished(func: () -> Unit = {}) {
        while (pool.any { it.isPlaying() }) {
            func()
        }
    }

    /**
     * Executes a function while the audio track with the specified ID is still playing.
     * @param id The ID of the audio track to monitor.
     * @param func The function to execute.
     */
    fun whileNotFinished(id: String, func: () -> Unit = {}) {
        val audio = getAudioTrack(id) ?: return
        while (audio.isPlaying()) {
            func()
        }
    }

    /**
     * Executes a suspend function while any audio track in the pool is still playing.
     * @param func The suspend function to execute.
     */
    suspend fun whileNotFinishedAsync(func: suspend () -> Unit) {
        while (pool.any { it.isPlaying() }) {
            func()
            delay(10)
        }
    }

    /**
     * Executes a suspend function while the audio track with the specified ID is still playing.
     * @param id The ID of the audio track to monitor.
     * @param func The suspend function to execute.
     */

    suspend fun whileNotFinishedAsync(id: String, func: suspend () -> Unit) {
        val audio = getAudioTrack(id) ?: return
        while (audio.isPlaying()) {
            func()
            delay(10)
        }
    }

    /**
     * Changes the master volume of all audio tracks in the pool by the specified amount.
     * @param volume The amount to change the master volume by.
     */
    fun changeMasterVolume(volume: Float) {
        pool.forEach { it.masterGainControl.addValue(volume) }
    }

    /**
     * Sets the master volume of all audio tracks in the pool to the specified value.
     * @param volume The value to set the master volume to.
     */
    fun setMasterVolume(volume: Float) {
        pool.forEach { it.masterGainControl.updateValue(volume) }
    }

    /**
     * Gets the master volume of the first audio track in the pool.
     * @return The master volume if available, null otherwise.
     */
    fun getMasterVolume(): Float? {
        return pool.firstOrNull()?.masterGainControl?.getValue()
    }

    /**
     * Gets the master volume range (min and max) of the first audio track in the pool.
     * @return A Pair containing the minimum and maximum master volume, or null if the pool is empty.
     */
    fun getMasterVolumeRange(): Pair<Float, Float>? {
        val first = pool.firstOrNull() ?: return null
        return Pair(first.masterGainControl.minimumValue, 0f)
    }

    /**
     * Resets the master volume of all audio tracks in the pool to the default value.
     */
    fun resetMasterVolume() {
        pool.forEach { it.masterGainControl.resetValue() }
    }

    /**
     * Changes the balance of all audio tracks in the pool by the specified amount.
     * @param balance The amount to change the balance by.
     */
    fun changeBalance(balance: Float) {
        pool.forEach { it.balanceControl.addValue(balance) }
    }

    /**
     * Resets the balance of all audio tracks in the pool to the default value.
     */
    fun resetBalance() {
        pool.forEach { it.balanceControl.resetValue() }
    }

    /**
     * Sets the balance of all audio tracks in the pool to the specified value.
     * @param balance The value to set the balance to.
     */
    fun setBalance(balance: Float) {
        pool.forEach { it.balanceControl.updateValue(balance) }
    }

    /**
     * Gets the balance of the first audio track in the pool.
     * @return The balance if available, null otherwise.
     */
    fun getBalance(): Float? {
        return pool.firstOrNull()?.balanceControl?.getValue()
    }

    /**
     * Gets the balance range (min and max) of the first audio track in the pool.
     * @return A Pair containing the minimum and maximum balance, or null if the pool is empty.
     */
    fun getBalanceRange(): Pair<Float, Float>? {
        val first = pool.firstOrNull() ?: return null
        return Pair(first.balanceControl.minimumValue, first.balanceControl.maximumValue)
    }

    /**
     * Mutes or unmutes all audio tracks in the pool.
     * @param mute True to mute, false to unmute.
     */
    fun mute(mute: Boolean) {
        pool.forEach { it.muteControl.updateValue(mute) }
    }

    /**
     * Checks if all audio tracks in the pool are muted.
     * @return True if all tracks are muted, false otherwise.
     */
    fun isPoolMuted(): Boolean = pool.all { it.muteControl.getValue() }

    /**
     * Checks if all audio tracks in the pool are stopped.
     * @return True if all tracks are stopped, false otherwise.
     */
    fun isPoolStopped(): Boolean = pool.all {
        !it.isPlaying()
    }

    /**
     * Retrieves the IDs of all currently playing audio tracks in the pool.
     * @return A list of IDs of playing audio tracks.
     */
    fun getPlayingAudios(): List<String> {
        return pool.filter { it.isPlaying() }.map { it.id }
    }

    companion object {
        /**
         * Builds an AudioPool using the provided builder action.
         * @param builderAction The action to build the list of AudioWrapper instances.
         * @return The constructed AudioPool.
         */
        fun buildAudioPool(builderAction: MutableList<AudioWrapper>.() -> Unit): AudioPool {
            val audioList = mutableListOf<AudioWrapper>()
            audioList.builderAction()
            return AudioPool(audioList)
        }
    }
}
