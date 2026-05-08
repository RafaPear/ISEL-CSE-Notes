package pt.isel.reversi.storage

/**
 * Contract for converting domain entities to/from a data storage format.
 *
 * Implementations must ensure that `deserialize(serialize(input)) = input` (round-trip integrity).
 *
 * This contract was based on [roby2014 - uni-projects/TDS](https://github.com/roby2014/uni-projects/tree/master/TDS)
 *
 * @param T The domain entity type to be serialized/deserialized.
 * @param U The data format type for storage (e.g., String, ByteArray, etc.).
 */
interface Serializer<T, U> {
    /**
     * Converts a domain entity to its storage representation.
     *
     * @param obj The domain entity to serialize.
     * @return The serialized representation suitable for storage.
     */
    fun serialize(obj: T): U

    /**
     * Converts a stored representation back to a domain entity.
     *
     * @param obj The stored data to deserialize.
     * @return The reconstructed domain entity.
     * @throws Exception if the data is malformed or invalid.
     */
    fun deserialize(obj: U): T
}