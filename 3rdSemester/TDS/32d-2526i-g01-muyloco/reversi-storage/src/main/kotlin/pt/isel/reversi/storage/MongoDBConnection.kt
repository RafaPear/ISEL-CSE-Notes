package pt.isel.reversi.storage

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Represents connection configuration for a MongoDB server.
 * Handles credential encoding and connection string generation.
 *
 * @property user The MongoDB username for authentication.
 * @property password The MongoDB password for authentication.
 * @property host The MongoDB server host address.
 * @property port The MongoDB server port number.
 */
data class MongoDBConnection(
    val user: String,
    val password: String,
    val host: String,
    val port: Int
) {

    init {
        require(user.isNotBlank())
        require(password.isNotBlank())
        require(host.isNotBlank())
        require(port in 1..65535)
    }

    /**
     * Generates the MongoDB connection URI with properly encoded credentials.
     * @return The MongoDB connection string for use with a MongoDB client.
     */
    fun getConnectionString(): String {
        val u = URLEncoder.encode(user, StandardCharsets.UTF_8)
        val p = URLEncoder.encode(password, StandardCharsets.UTF_8)

        return "mongodb://$u:$p@$host:$port"
    }
}
