package de.andrena.util.json

import de.andrena.util.json.DeserializationResult.Error
import de.andrena.util.json.DeserializationResult.Ok
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val jsonSerializer = Json {
    serializersModule = SerializersModule {
        contextual(InstantSerializer)
    }
}

inline fun <reified T> String.decodeJson(): DeserializationResult<T> = try {
    Ok(jsonSerializer.decodeFromString(this))
} catch (ex: SerializationException) {
    Error(ex.message ?: "")
}

sealed class DeserializationResult<out T> {
    class Ok<T>(val value: T) : DeserializationResult<T>()
    class Error(val message: String) : DeserializationResult<Nothing>()
}
