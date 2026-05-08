package pt.isel.reversi.core.exceptions

/**
 * Thrown when the storage system fails a health check or encounters a critical error.
 * Indicates that the storage backend is not functioning correctly and may require attention.
 * @property message Details about the storage failure.
 */
class BadStorage(message: String) : ReversiException(message, ErrorType.CRITICAL)