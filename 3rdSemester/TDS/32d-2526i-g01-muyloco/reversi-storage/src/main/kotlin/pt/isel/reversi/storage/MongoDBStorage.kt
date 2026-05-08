package pt.isel.reversi.storage

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.MongoClient
import org.bson.Document
import pt.isel.reversi.utils.TRACKER

/**
 * [Storage] implementation via MongoDB + text strings.
 *
 * This method was based from [roby2014 - uni-projects/TDS](https://github.com/roby2014/uni-projects/tree/master/TDS)
 *
 * @param T Type of the domain entity
 */
data class MongoDBStorage<T>(
    private val mongoDBConnection: MongoDBConnection,
    private val databaseName: String,
    private val collectionName: String,
    override val serializer: Serializer<T, String>
) : Storage<String, T, String> {

    private val uri = mongoDBConnection.getConnectionString()
    private val mongoClient = MongoClient.create(uri)

    private val database = mongoClient.getDatabase(databaseName)
    private val collection = database.getCollection<Document>(collectionName)

    init {
        TRACKER.trackClassCreated(this, category = "Storage.MongoDB")
        // cria coleção se não existir
        val collectionNames = database.listCollectionNames().toList()
        if (collectionName !in collectionNames) {
            database.createCollection(collectionName)
        }
    }

    private fun makeDoc(id: String, obj: String): Document =
        Document("_id", id)
            .append("obj", obj)
            .append("lastModified", System.currentTimeMillis())


    private fun ensureIdExists(id: String, shouldExist: Boolean) {
        val exists = collection.find(eq("_id", id)).firstOrNull() != null
        require(exists == shouldExist) {
            if (shouldExist)
                "There is no entity with given id '$id'"
            else
                "There is already an entity with given id '$id'"
        }
    }

    override fun new(id: String, factory: () -> T): T {
        TRACKER.trackFunctionCall(customName = "MongoDBStorage.new", details = "id=$id", category = "Storage.MongoDB")
        ensureIdExists(id, shouldExist = false)

        val obj = factory()
        val objStr = serializer.serialize(obj)

        collection.insertOne(makeDoc(id, objStr))
        return obj
    }

    override fun load(id: String): T? {
        TRACKER.trackFunctionCall(customName = "MongoDBStorage.load", details = "id=$id", category = "Storage.MongoDB")
        val doc = collection.find(eq("_id", id)).firstOrNull()
        return doc?.getString("obj")?.let(serializer::deserialize)
    }

    override fun save(id: String, obj: T) {
        TRACKER.trackFunctionCall(customName = "MongoDBStorage.save", details = "id=$id", category = "Storage.MongoDB")
        ensureIdExists(id, shouldExist = true)

        val objStr = serializer.serialize(obj)
        collection.replaceOne(eq("_id", id), makeDoc(id, objStr))
    }

    override fun delete(id: String) {
        ensureIdExists(id, shouldExist = true)
        collection.deleteOne(eq("_id", id))
    }

    override fun lastModified(id: String): Long? {
        val doc = collection.find(eq("_id", id)).firstOrNull() ?: return null
        return doc.getLong("lastModified")
    }

    override fun loadAllIds(): List<String> =
        collection.find().map { it.getString("_id") }.toList()

    override fun close() {
        mongoClient.close()
    }
}