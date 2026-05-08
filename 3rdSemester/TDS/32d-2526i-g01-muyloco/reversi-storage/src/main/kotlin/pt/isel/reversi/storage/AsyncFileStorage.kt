package pt.isel.reversi.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [AsyncStorage] implementation via file + text strings.
 *
 * This method was based from [roby2014 - uni-projects/TDS](https://github.com/roby2014/uni-projects/tree/master/TDS)
 *
 * @param T Type of the domain entity
 * @param folder Folder where the file will be stored
 */
data class AsyncFileStorage<T>(
    private val folder: String,
    override val serializer: Serializer<T, String>
) : AsyncStorage<String, T, String> {
    private val fileStorage = FileStorage(folder, serializer)

    /**
     * Creates a new entity in async file storage with the given identifier.
     * @param id The unique identifier for the entity.
     * @param factory A lambda that produces the initial entity when called.
     * @return The created entity.
     * @throws IllegalArgumentException if an entity with the given ID already exists.
     */
    override suspend fun new(id: String, factory: () -> T): T =
        withContext(Dispatchers.IO) { fileStorage.new(id, factory) }

    /**
     * Retrieves an entity from async file storage by its identifier.
     * @param id The unique identifier of the entity to load.
     * @return The entity if found, or null if no entity with that ID exists.
     */
    override suspend fun load(id: String): T? = withContext(Dispatchers.IO) { fileStorage.load(id) }

    /**
     * Saves (persists) an entity in async file storage under the given identifier.
     * @param id The unique identifier for the entity.
     * @param obj The entity to save.
     * @throws IllegalArgumentException if no entity with the given ID exists.
     */
    override suspend fun save(id: String, obj: T) = withContext(Dispatchers.IO) { fileStorage.save(id, obj) }

    /**
     * Deletes an entity from async file storage by its identifier.
     * @param id The unique identifier of the entity to delete.
     * @throws IllegalArgumentException if no entity with the given ID exists.
     */
    override suspend fun delete(id: String) = withContext(Dispatchers.IO) { fileStorage.delete(id) }

    /**
     * Gets the last modification timestamp for an entity in async file storage.
     * @param id The unique identifier of the entity.
     * @return The last modification time in milliseconds since epoch, or null if not found.
     */
    override suspend fun lastModified(id: String): Long? = withContext(Dispatchers.IO) { fileStorage.lastModified(id) }

    /**
     * Loads all entity identifiers currently stored in this async file storage.
     * @return A list of all entity identifiers in the storage directory.
     */
    override suspend fun loadAllIds(): List<String> = withContext(Dispatchers.IO) { fileStorage.loadAllIds() }

    /**
     * Closes the async file storage, releasing any held resources.
     */
    override suspend fun close() = withContext(Dispatchers.IO) { fileStorage.close() }
}