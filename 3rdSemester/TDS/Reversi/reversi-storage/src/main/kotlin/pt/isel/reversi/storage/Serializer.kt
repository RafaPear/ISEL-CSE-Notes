package pt.isel.reversi.storage

/**
 * Serializer contract.
 *
 * This method was based from [roby2014 - uni-projects/TDS](https://github.com/roby2014/uni-projects/tree/master/TDS)
 * @param T type of the domain entity
 * @param U type of the data we want to convert to/from (e.g String, ..)
 *
 * NOTE: deserialize(serialize(input)) = input
 */
interface Serializer<T, U> {
    fun serialize(obj: T): U
    fun deserialize(obj: U): T
}