package de.andrena.util

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.HttpStatus.BAD_REQUEST
import de.andrena.util.DeserializationResult.Error
import de.andrena.util.DeserializationResult.Ok
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun <T> HttpRequestMessage<T>.respondBadRequest(message: String): HttpResponseMessage =
    this.respondWith(BAD_REQUEST, body = message)

fun <T> HttpRequestMessage<T>.respondWith(status: HttpStatus, body: Any): HttpResponseMessage =
    this.createResponseBuilder(status).body(body).build()

sealed class DeserializationResult<out T> {
    class Ok<T>(val value: T) : DeserializationResult<T>()
    class Error(val message: String) : DeserializationResult<Nothing>()
}

inline fun <reified T> HttpRequestMessage<String?>.decodeBodyAs(): DeserializationResult<T> =
    body?.run { decode() } ?: Error("body must not be null")

inline fun <reified T> String.decode(): DeserializationResult<T> = try {
    Ok(Json.decodeFromString(this))
} catch (ex: SerializationException) {
    Error(ex.message ?: "")
}
