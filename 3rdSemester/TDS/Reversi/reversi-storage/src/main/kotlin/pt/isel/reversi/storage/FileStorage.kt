package pt.isel.reversi.storage

import okio.FileSystem
import okio.Path.Companion.toPath

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

    /** Storage file path for entity identified by [id]. */
    private fun path(id: String) = "$folder/$id.txt".toPath()

    override fun new(id: String, factory: () -> T): T {
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

    override fun load(id: String): T? {
        val fs = FileSystem.SYSTEM

        if (!fs.exists(path(id))) return null

        val content = fs.read(path(id)) { readUtf8() }

        return serializer.deserialize(content)
    }

    override fun save(id: String, obj: T) {
        val fs = FileSystem.SYSTEM

        require(fs.exists(path(id))) { "There is no entity with given id '$id'" }

        val objStr = serializer.serialize(obj)

        fs.write(path(id), false) {
            writeUtf8(objStr)
        }
    }

    override fun delete(id: String) {
        val fs = FileSystem.SYSTEM

        require(fs.exists(path(id))) { "There is no entity with given id '$id'" }

        fs.delete(path(id))
    }
}