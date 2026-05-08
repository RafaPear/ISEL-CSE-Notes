package serie2.problema

import java.util.concurrent.atomic.AtomicInteger

class ProgressBar(
    private val total: Int,
    private val startTime: Long
) {
    private val current = AtomicInteger(0)
    private var lastProgress = -1
    @Volatile
    private var running = true

    // Atualiza o progresso atual
    fun updateProgress(value: Int) {
        current.set(value)
    }

    // Para o thread
    fun stop() {
        running = false
        printProgressBar(current.get(), total, startTime, lastProgress)
        println()
    }

    // Função que roda no thread
    fun start() {
        Thread {
            while (running) {
                val curr = current.get()
                if (curr > total) break

                val progress = (curr * 100) / total
                if (progress > lastProgress) {
                    lastProgress = printProgressBar(curr, total, startTime, lastProgress)
                }
                Thread.sleep(200)  // Atualiza a cada 200ms
            }
        }.start()
    }


    // A tua função printProgressBar (deixa igual à última versão)
    private fun printProgressBar(current: Int, total: Int, startTime: Long, lastProgress: Int): Int {
        if (total == 0) return lastProgress

        val progress = (current * 100) / total

        if (progress <= lastProgress) return lastProgress

        val filledLength = (50 * progress) / 100

        val bar = buildString(50) {
            repeat(filledLength) { append('█') }
            repeat(50 - filledLength) { append('░') }
        }

        val now = System.currentTimeMillis()
        val elapsedTime = now - startTime
        val remainingTimeMs = estimateTimeRemaining(current, total, startTime)

        val color = when {
            remainingTimeMs <= 60_000     -> GREEN
            remainingTimeMs <= 120_000    -> YELLOW
            remainingTimeMs <= 180_000    -> ORANGE
            else                          -> RED
        }

        val timeElapsedStr = timeFormat(elapsedTime)
        val timeRemainingStr = if (remainingTimeMs == Long.MAX_VALUE) "Calculating..." else timeFormat(remainingTimeMs)

        print("\r$color|$bar| $progress% | Time remaining: $timeRemainingStr | Time elapsed: $timeElapsedStr $RESET")
        System.out.flush()

        return progress
    }

    // function that tells the user an estimate of how much time remains till the file is parsed.
    // without the total
    private fun estimateTimeRemaining(current: Int, total: Int, startTime: Long): Long {
        if (current == 0) return Long.MAX_VALUE
        val elapsedTime = System.currentTimeMillis() - startTime
        val estimatedTotalTime = (elapsedTime * total) / current
        return estimatedTotalTime - elapsedTime
    }


    private fun timeFormat(ms: Long): String {
        val minutes = (ms / 60000).toInt()
        val seconds = ((ms % 60000) / 1000).toInt()
        return if (minutes > 0) "${minutes}m ${seconds}s" else "${seconds}s"
    }
}
