package pt.isel.reversi.storage

import okio.FileSystem
import okio.Path.Companion.toPath
import pt.isel.reversi.utils.TRACKER

/**
 * [Storage] implementation via file + text strings.
 *
 * This method was based from [roby2014 - uni-projects/TDS](https://github.com/roby2014/uni-projects/tree/master/TDS)
 *
 * @param T Type of the domain entity
 * @param folder Folder where the file will be stored
 */
data class FileStorage<T>(
    private val folder: String,
    override val serializer: Serializer<T, String>
) : Storage<String, T, String> {

    init {
        TRACKER.trackClassCreated(this, category = "Storage.File")
    }

    /**
     * Computes the file path for an entity with the given ID.
     * @param id The entity identifier.
     * @return The file path where the entity will be stored.
     */
    private fun path(id: String) = "$folder/$id.txt".toPath()

    /**
     * Creates a new entity in file storage with the given identifier.
     * @param id The unique identifier for the entity.
     * @param factory A lambda that produces the initial entity when called.
     * @return The created entity.
     * @throws IllegalArgumentException if an entity with the given ID already exists.
     */
    override fun new(id: String, factory: () -> T): T {
        TRACKER.trackFunctionCall(customName = "FileStorage.new", details = "id=$id", category = "Storage.File")
        val fs = FileSystem.SYSTEM

        require(!fs.exists(path(id))) { "There is already an entity with given id '$id'" }

        val obj = factory()
        val objStr = serializer.serialize(obj)

        path(id).parent?.let { fs.createDirectories(it) }

        fs.write(path(id), true) {
            writeUtf8(objStr)
        }

        return obj
    }

    /**
     * Retrieves an entity from file storage by its identifier.
     * @param id The unique identifier of the entity to load.
     * @return The entity if found, or null if no entity with that ID exists.
     */
    override fun load(id: String): T? {
        TRACKER.trackFunctionCall(customName = "FileStorage.load", details = "id=$id", category = "Storage.File")
        val fs = FileSystem.SYSTEM

        if (!fs.exists(path(id))) return null

        // check if file is not in use by another process
        fs.metadata(path(id))
        val content = fs.read(path(id)) { readUtf8() }

        return serializer.deserialize(content)
    }

    /**
     * Saves (persists) an entity in file storage under the given identifier.
     * @param id The unique identifier for the entity.
     * @param obj The entity to save.
     * @throws IllegalArgumentException if no entity with the given ID exists.
     */
    override fun save(id: String, obj: T) {
        TRACKER.trackFunctionCall(customName = "FileStorage.save", details = "id=$id", category = "Storage.File")
        val fs = FileSystem.SYSTEM

        require(fs.exists(path(id))) { "There is no entity with given id '$id'" }

        val objStr = serializer.serialize(obj)

        fs.write(path(id), false) {
            writeUtf8(objStr)
        }
    }

    /**
     * Deletes an entity from file storage by its identifier.
     * @param id The unique identifier of the entity to delete.
     * @throws IllegalArgumentException if no entity with the given ID exists.
     */
    override fun delete(id: String) {
        val fs = FileSystem.SYSTEM

        require(fs.exists(path(id))) { "There is no entity with given id '$id'" }

        fs.delete(path(id))
    }

    /**
     * Gets the last modification timestamp for an entity in file storage.
     * @param id The unique identifier of the entity.
     * @return The last modification time in milliseconds since epoch, or null if not found.
     */
    override fun lastModified(id: String): Long? {
        val fs = FileSystem.SYSTEM
        if (!fs.exists(path(id))) return null

        return fs.metadata(path(id)).lastModifiedAtMillis
    }

    /**
     * Loads all entity identifiers currently stored in this file storage.
     * @return A list of all entity identifiers in the storage directory.
     */
    override fun loadAllIds(): List<String> {
        val fs = FileSystem.SYSTEM
        val dirPath = folder.toPath()

        if (!fs.exists(dirPath)) return emptyList()

        return fs.list(dirPath)
            .filter { fs.metadata(it).isRegularFile }
            .map { it.name.removeSuffix(".txt") }
    }

    /**
     * Closes the file storage, releasing any held resources.
     * For file storage, this is a no-op as there are no persistent resources to close.
     */
    override fun close() {
        // No resources to release
    }
}