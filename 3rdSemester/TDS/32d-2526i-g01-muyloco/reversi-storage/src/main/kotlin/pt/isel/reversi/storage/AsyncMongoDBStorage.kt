package pt.isel.reversi.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [AsyncStorage] implementation via file + text strings.
 *
 * This method was based from [roby2014 - uni-projects/TDS](https://github.com/roby2014/uni-projects/tree/master/TDS)
 *
 * @param T Type of the domain entity
 * @param mongoDBConnection MongoDB connection information
 * @param databaseName Name of the MongoDB database
 * @param collectionName Name of the MongoDB collection
 */
data class AsyncMongoDBStorage<T>(
    private val mongoDBConnection: MongoDBConnection,
    private val databaseName: String,
    private val collectionName: String,
    override val serializer: Serializer<T, String>
) : AsyncStorage<String, T, String> {
    private val storage: MongoDBStorage<T> by lazy {
        MongoDBStorage(
            mongoDBConnection = mongoDBConnection,
            databaseName = databaseName,
            collectionName = collectionName,
            serializer = serializer
        )
    }

    /**
     * Creates a new entity in async MongoDB storage with the given identifier.
     * @param id The unique identifier for the entity.
     * @param factory A lambda that produces the initial entity when called.
     * @return The created entity.
     * @throws IllegalArgumentException if an entity with the given ID already exists.
     */
    override suspend fun new(id: String, factory: () -> T): T =
        withContext(Dispatchers.IO) { storage.new(id, factory) }

    /**
     * Retrieves an entity from async MongoDB storage by its identifier.
     * @param id The unique identifier of the entity to load.
     * @return The entity if found, or null if no entity with that ID exists.
     */
    override suspend fun load(id: String): T? = withContext(Dispatchers.IO) { storage.load(id) }

    /**
     * Saves (persists) an entity in async MongoDB storage under the given identifier.
     * @param id The unique identifier for the entity.
     * @param obj The entity to save.
     * @throws IllegalArgumentException if no entity with the given ID exists.
     */
    override suspend fun save(id: String, obj: T) = withContext(Dispatchers.IO) { storage.save(id, obj) }

    /**
     * Deletes an entity from async MongoDB storage by its identifier.
     * @param id The unique identifier of the entity to delete.
     * @throws IllegalArgumentException if no entity with the given ID exists.
     */
    override suspend fun delete(id: String) = withContext(Dispatchers.IO) { storage.delete(id) }

    /**
     * Gets the last modification timestamp for an entity in async MongoDB storage.
     * @param id The unique identifier of the entity.
     * @return The last modification time in milliseconds since epoch, or null if not found.
     */
    override suspend fun lastModified(id: String): Long? = withContext(Dispatchers.IO) { storage.lastModified(id) }

    /**
     * Loads all entity identifiers currently stored in this async MongoDB storage.
     * @return A list of all entity identifiers in the storage.
     */
    override suspend fun loadAllIds(): List<String> = withContext(Dispatchers.IO) { storage.loadAllIds() }

    /**
     * Closes the async MongoDB storage, releasing any held resources.
     */
    override suspend fun close() = withContext(Dispatchers.IO) { storage.close() }
}