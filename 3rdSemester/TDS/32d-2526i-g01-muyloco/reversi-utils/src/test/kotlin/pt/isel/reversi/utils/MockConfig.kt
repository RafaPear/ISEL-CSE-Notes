package pt.isel.reversi.utils

class MockConfig(override val map: Map<String, String>) : Config {
    val TEST_INT = map["TEST_INT"]?.toIntOrNull() ?: 42
    val TEST_STRING = map["TEST_STRING"] ?: "default"
    val TEST_CHAR = map["TEST_CHAR"]?.firstOrNull() ?: 'X'
    val TEST_BOOL = map["TEST_BOOL"]?.toBoolean() ?: true
    val TEST_DOUBLE = map["TEST_DOUBLE"]?.toDoubleOrNull() ?: 3.14
    val TEST_LONG = map["TEST_LONG"]?.toLongOrNull() ?: 100000L
    val TEST_FLOAT = map["TEST_FLOAT"]?.toFloatOrNull() ?: 2.71f

    override fun getDefaultConfigFileEntries(): Map<String, String> {
        return mapOf(
            "TEST_INT" to TEST_INT.toString(),
            "TEST_STRING" to TEST_STRING,
            "TEST_CHAR" to TEST_CHAR.toString(),
            "TEST_BOOL" to TEST_BOOL.toString(),
            "TEST_DOUBLE" to TEST_DOUBLE.toString(),
            "TEST_LONG" to TEST_LONG.toString(),
            "TEST_FLOAT" to TEST_FLOAT.toString()
        )
    }
}