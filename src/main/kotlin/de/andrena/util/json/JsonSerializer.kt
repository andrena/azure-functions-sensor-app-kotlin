package de.andrena.util.json

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val jsonSerializer = Json {
    serializersModule = SerializersModule {
        contextual(OffsetDateTimeSerializer)
    }
}

inline fun <reified T> String.decodeJson(): T = try {
    jsonSerializer.decodeFromString(this)
} catch (ex: SerializationException) {
    throw IllegalArgumentException(ex.message)
}

inline fun <reified T> T.encodedAsJson(): String =
    jsonSerializer.encodeToString(this)
