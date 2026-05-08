package pt.isel.reversi.core.storage

import pt.isel.reversi.storage.MongoDBConnection

/**
 * Sealed class representing different storage backend configurations.
 * Different storage implementations can be selected by creating appropriate subclass instances.
 */
sealed class StorageParams {
    /**
     * Configuration for file-based storage.
     * @property folder The directory path where game files will be stored.
     */
    data class FileStorageParams(val folder: String) : StorageParams()

    /**
     * Configuration for MongoDB-based storage.
     * @property mongoDBConnection The MongoDB connection details.
     * @property databaseName The name of the database to use.
     * @property collectionName The name of the collection to use.
     */
    data class DatabaseStorageParams(
        val mongoDBConnection: MongoDBConnection, val databaseName: String, val collectionName: String
    ) : StorageParams()
}