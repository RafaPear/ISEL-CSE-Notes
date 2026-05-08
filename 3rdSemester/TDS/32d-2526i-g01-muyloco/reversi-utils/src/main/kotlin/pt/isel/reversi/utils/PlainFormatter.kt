package pt.isel.reversi.utils

/**
 * A plain formatter for logging that outputs the log level, source method name, and message.
 */
class PlainFormatter : java.util.logging.Formatter() {

    override fun format(record: java.util.logging.LogRecord): String {
        val origin = buildOrigin(
            record.sourceClassName,
            record.sourceMethodName
        )

        val instant = java.time.Instant.ofEpochMilli(record.millis)
        val localTime = java.time.LocalDateTime.ofInstant(
            instant,
            java.time.ZoneId.systemDefault()
        )

        val hour = localTime.hour
        val minute = localTime.minute
        val second = localTime.second

        val timestamp = String.format("%02d:%02d:%02d", hour, minute, second)

        return "[${record.level}] $timestamp ($origin) ${record.message}${System.lineSeparator()}"
    }
}
