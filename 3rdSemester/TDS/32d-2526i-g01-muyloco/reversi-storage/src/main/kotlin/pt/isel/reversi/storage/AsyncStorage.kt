package pt.isel.reversi.storage

/**
 * Asynchronous storage contract for persisting and retrieving domain entities.
 *
 * This interface extends the storage concept with coroutine-based suspend functions
 * for non-blocking I/O operations. All methods are suspendable and should be called
 * within a coroutine context.
 *
 * This contract was based on [roby2014 - uni-projects/TDS](https://github.com/roby2014/uni-projects/tree/master/TDS)
 *
 * @param K The type used to identify entities (e.g., String for names, Int for IDs).
 * @param T The domain entity type being stored.
 * @param U The storage format type used internally by the serializer (e.g., String, ByteArray).
 */
interface AsyncStorage<K, T, U> {

    /**
     * The serializer responsible for converting entities to/from the storage format.
     */
    val serializer: Serializer<T, U>

    /**
     * Creates a new entity in storage with the given identifier.
     *
     * @param id The unique identifier for the entity.
     * @param factory A lambda that produces the initial entity when called.
     * @return The created entity.
     * @throws Exception if an entity with the given [id] already exists.
     */
    suspend fun new(id: K, factory: () -> T): T

    /**
     * Retrieves an entity from storage by its identifier.
     *
     * @param id The unique identifier of the entity to load.
     * @return The entity if found, or null if no entity with that [id] exists.
     */
    suspend fun load(id: K): T?

    /**
     * Saves (persists) an entity in storage under the given identifier.
     *
     * @param id The unique identifier for the entity.
     * @param obj The entity to save.
     */
    suspend fun save(id: K, obj: T)

    /**
     * Deletes an entity from storage by its identifier.
     *
     * @param id The unique identifier of the entity to delete.
     */
    suspend fun delete(id: K)

    /**
     * Gets the last modification timestamp for an entity.
     *
     * @param id The unique identifier of the entity.
     * @return The last modification time in milliseconds since epoch, or null if not found.
     */
    suspend fun lastModified(id: K): Long?

    /**
     * Loads all identifiers currently stored in this storage.
     *
     * @return A list of all entity identifiers in the storage.
     */
    suspend fun loadAllIds(): List<K>

    /**
     * Closes the storage, releasing any held resources.
     *
     * After calling this method, the storage should not be used further.
     */
    suspend fun close()
}