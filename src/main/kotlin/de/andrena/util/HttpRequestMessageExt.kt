package de.andrena.util

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.HttpStatus.BAD_REQUEST
import de.andrena.util.json.DeserializationResult
import de.andrena.util.json.DeserializationResult.Error
import de.andrena.util.json.decodeJson
import de.andrena.util.json.jsonSerializer
import kotlinx.serialization.encodeToString

fun <T> HttpRequestMessage<T>.respondBadRequest(message: String): HttpResponseMessage =
    this.respondWith(BAD_REQUEST, body = message)

inline fun <T, reified R> HttpRequestMessage<T>.respondJson(status: HttpStatus, body: R): HttpResponseMessage =
    this.createResponseBuilder(status).body(jsonSerializer.encodeToString(body)).build()

fun <T> HttpRequestMessage<T>.respondWith(status: HttpStatus, body: Any): HttpResponseMessage =
    this.createResponseBuilder(status).body(body).build()

inline fun <reified T> HttpRequestMessage<String?>.decodeBodyAs(): DeserializationResult<T> =
    body?.run { decodeJson() } ?: Error("body must not be null")
