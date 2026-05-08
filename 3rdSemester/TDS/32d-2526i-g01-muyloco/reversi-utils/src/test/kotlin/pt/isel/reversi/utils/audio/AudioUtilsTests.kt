package pt.isel.reversi.utils.audio

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import java.net.URL
import kotlin.test.*

class AudioUtilsTests {

    @AfterTest
    @BeforeTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    // Helper function to create a mock audio file URL for testing
    private fun createTestAudioUrl(): URL {
        // You'll need to provide a valid test audio file path
        // For example: return File("src/test/resources/test-audio.wav").toURI().toURL()
        return javaClass.getResource("/test-audio.wav") ?: throw IllegalStateException("Test audio file not found")
    }

    // AudioModifier Tests
    @Test
    fun `AudioModifier default values are correct`() {
        val modifier = AudioModifier()

        assertFalse(modifier.closeOnFinish)
        assertTrue(modifier.gotoStartOnStop)
        assertNull(modifier.startPosition)
        assertFalse(modifier.loop)
        assertNull(modifier.loopStartPosition)
        assertNull(modifier.loopEndPosition)
    }

    @Test
    fun `setToCloseOnFinish creates copy with closeOnFinish true`() {
        val modifier = AudioModifier()
        val modified = modifier.setToCloseOnFinish()

        assertTrue(modified.closeOnFinish)
        assertFalse(modifier.closeOnFinish) // Original unchanged
    }

    @Test
    fun `setToNotGoToStartOnStop creates copy with gotoStartOnStop false`() {
        val modifier = AudioModifier()
        val modified = modifier.setToNotGoToStartOnStop()

        assertFalse(modified.gotoStartOnStop)
        assertTrue(modifier.gotoStartOnStop) // Original unchanged
    }

    @Test
    fun `setStartPosition sets the correct position`() {
        val modifier = AudioModifier()
        val position = 1000
        val modified = modifier.setStartPosition(position)

        assertEquals(position, modified.startPosition)
        assertNull(modifier.startPosition) // Original unchanged
    }

    @Test
    fun `setToLoop enables looping with specified positions`() {
        val modifier = AudioModifier()
        val start = 100
        val end = 500
        val modified = modifier.setToLoop(start, end)

        assertTrue(modified.loop)
        assertEquals(start, modified.loopStartPosition)
        assertEquals(end, modified.loopEndPosition)
    }

    @Test
    fun `setToLoop with null positions enables looping without specific bounds`() {
        val modifier = AudioModifier()
        val modified = modifier.setToLoop()

        assertTrue(modified.loop)
        assertNull(modified.loopStartPosition)
        assertNull(modified.loopEndPosition)
    }

    @Test
    fun `setToLoopInfinitely configures infinite loop`() {
        val modifier = AudioModifier()
        val modified = modifier.setToLoopInfinitely()

        assertTrue(modified.loop)
        assertEquals(0, modified.loopStartPosition)
        assertEquals(-1, modified.loopEndPosition)
    }

    // AudioWrapper Tests
    @Test
    fun `loadAudio creates AudioWrapper with correct id`() {
        val name = "test-audio"
        val url = createTestAudioUrl()

        val audio = AudioWrapper.loadAudio(name, url)

        assertEquals(name, audio.id)
        audio.close()
    }

    @Test
    fun `loadAudio with modifier applies modifier settings`() {
        val url = createTestAudioUrl()
        val modifier = AudioModifier(loop = true, startPosition = 100)

        val audio = AudioWrapper.loadAudio("test", url, modifier)

        assertEquals(modifier, audio.modifier)
        audio.close()
    }

    @Test
    fun `play starts audio playback`() {
        val url = createTestAudioUrl()
        val audio = AudioWrapper.loadAudio("test", url)

        audio.play()
        assertTrue(audio.isPlaying())

        audio.stop()
        audio.close()
    }

    @Test
    fun `stop halts audio playback`() {
        val url = createTestAudioUrl()
        val audio = AudioWrapper.loadAudio("test", url)

        audio.play()
        assertTrue(audio.isPlaying())

        audio.stop()
        assertFalse(audio.isPlaying())

        audio.close()
    }

    @Test
    fun `pause halts audio playback`() {
        val url = createTestAudioUrl()
        val audio = AudioWrapper.loadAudio("test", url)

        audio.play()
        assertTrue(audio.isPlaying())

        audio.pause()
        assertFalse(audio.isPlaying())

        audio.stop()
        audio.close()
    }

    @Test
    fun `audio with loop modifier loops continuously`() {
        val url = createTestAudioUrl()
        val modifier = AudioModifier().setToLoopInfinitely()
        val audio = AudioWrapper.loadAudio("test", url, modifier)

        audio.play()
        assertTrue(audio.isPlaying())

        audio.stop()
        audio.close()
    }

    // AudioPool Tests
    @Test
    fun `buildAudioPool creates pool with correct size`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
            add(AudioWrapper.loadAudio("audio2", url))
            add(AudioWrapper.loadAudio("audio3", url))
        }

        assertEquals(3, pool.pool.size)
        pool.destroy()
    }

    @Test
    fun `getAudioTrack returns correct track by id`() {
        val url = createTestAudioUrl()
        val id = "audio1"

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio(id, url))
            add(AudioWrapper.loadAudio("audio2", url))
        }

        val track = pool.getAudioTrack(id)
        assertNotNull(track)
        assertEquals(id, track.id)

        pool.destroy()
    }

    @Test
    fun `getAudioTrack returns null for non-existent id`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
        }

        val track = pool.getAudioTrack("non-existent")
        assertNull(track)

        pool.destroy()
    }

    @Test
    fun `play starts specific track by id`() {
        val url = createTestAudioUrl()
        val id = "audio1"

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio(id, url))
        }

        pool.play(id)
        assertTrue(pool.isPlaying(id))

        pool.stopAll()
        pool.destroy()
    }

    @Test
    fun `stop halts specific track by id`() {
        val url = createTestAudioUrl()
        val id = "audio1"

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio(id, url))
        }

        pool.play(id)
        pool.stop(id)
        assertFalse(pool.isPlaying(id))

        pool.destroy()
    }

    @Test
    fun `playAll starts all tracks`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
            add(AudioWrapper.loadAudio("audio2", url))
        }

        pool.playAll()
        assertTrue(pool.isPlaying("audio1"))
        assertTrue(pool.isPlaying("audio2"))

        pool.stopAll()
        pool.destroy()
    }

    @Test
    fun `stopAll halts all tracks`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
            add(AudioWrapper.loadAudio("audio2", url))
        }

        pool.playAll()
        pool.stopAll()

        assertFalse(pool.isPlaying("audio1"))
        assertFalse(pool.isPlaying("audio2"))

        pool.destroy()
    }

    @Test
    fun `pauseAll pauses all tracks`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
            add(AudioWrapper.loadAudio("audio2", url))
        }

        pool.playAll()
        pool.pauseAll()

        assertFalse(pool.isPlaying("audio1"))
        assertFalse(pool.isPlaying("audio2"))

        pool.destroy()
    }

    @Test
    fun `isPoolStopped returns true when all tracks stopped`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
        }

        assertTrue(pool.isPoolStopped())

        pool.play("audio1")
        assertFalse(pool.isPoolStopped())

        pool.stop("audio1")
        assertTrue(pool.isPoolStopped())

        pool.destroy()
    }

    @Test
    fun `setMasterVolume changes volume for all tracks`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
            add(AudioWrapper.loadAudio("audio2", url))
        }

        val newVolume = -10.0f
        pool.setMasterVolume(newVolume)

        val volume = pool.getMasterVolume()
        assertNotNull(volume)
        assertEquals(newVolume, volume, 0.1f)

        pool.destroy()
    }

    @Test
    fun `resetMasterVolume restores default volume`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
        }

        val originalVolume = pool.getMasterVolume()
        pool.setMasterVolume(-20.0f)
        pool.resetMasterVolume()

        assertEquals(originalVolume, pool.getMasterVolume())

        pool.destroy()
    }

    @Test
    fun `setBalance changes balance for all tracks`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
        }

        val newBalance = 0.5f
        pool.setBalance(newBalance)

        val balance = pool.getBalance()
        assertNotNull(balance)
        assertEquals(newBalance, balance, 0.1f)

        pool.destroy()
    }

    @Test
    fun `mute silences all tracks`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
        }

        pool.mute(true)
        val track = pool.getAudioTrack("audio1")
        assertTrue(track?.muteControl?.getValue() ?: false)

        pool.destroy()
    }

    @Test
    fun `whileNotFinishedAsync waits until track finishes`() = runBlocking {
        val url = createTestAudioUrl()
        val id = "audio1"

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio(id, url))
        }

        pool.play(id)
        var executed = false

        pool.whileNotFinishedAsync(id) {
            executed = true
            pool.stop(id) // Force stop to exit loop
        }

        assertTrue(executed)
        pool.destroy()
    }

    @Test
    fun `destroy closes all audio resources`() {
        val url = createTestAudioUrl()

        val pool = AudioPool.buildAudioPool {
            add(AudioWrapper.loadAudio("audio1", url))
            add(AudioWrapper.loadAudio("audio2", url))
        }

        pool.destroy()

        // After destroy, attempting to play should not work
        pool.play("audio1")
        assertFalse(pool.isPlaying("audio1"))
    }
}