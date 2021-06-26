package de.andrena.util

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.HttpStatus.BAD_REQUEST
import com.microsoft.azure.functions.HttpStatus.INTERNAL_SERVER_ERROR
import de.andrena.util.json.DeserializationResult.Error
import de.andrena.util.json.DeserializationResult.Ok
import de.andrena.util.json.decodeJson
import de.andrena.util.json.jsonSerializer
import kotlinx.serialization.encodeToString
import java.net.URI

fun <T> HttpRequestMessage<T>.respondBadRequest(message: String): HttpResponseMessage =
    this.respondWith(BAD_REQUEST, body = message)

fun <T> HttpRequestMessage<T>.respondInternalServerError(message: String): HttpResponseMessage =
    this.respondWith(INTERNAL_SERVER_ERROR, body = message)

inline fun <T, reified R> HttpRequestMessage<T>.respondJson(status: HttpStatus, body: R): HttpResponseMessage =
    this.createResponseBuilder(status).body(jsonSerializer.encodeToString(body)).build()

fun <T> HttpRequestMessage<T>.respondWith(status: HttpStatus, body: Any): HttpResponseMessage =
    this.createResponseBuilder(status).body(body).build()

fun <T> HttpRequestMessage<T>.respondWith(status: HttpStatus): HttpResponseMessage =
    this.createResponseBuilder(status).build()

typealias AzureFunction<T> = HttpRequestMessage<T>.() -> HttpResponseMessage

inline fun <reified T : Any> HttpRequestMessage<String?>.deserializeBodyAs(block: AzureFunction<T>): HttpResponseMessage {
    val request: HttpRequestMessage<T> = this.convertTo { body: String? ->
        if (body == null) {
            throw IllegalArgumentException("body must not be null")
        }

        when (val result = body.decodeJson<T>()) {
            is Error -> throw IllegalArgumentException(result.message)
            is Ok -> result.value
        }
    }

    return request.block()
}

inline fun <reified T, reified R> HttpRequestMessage<T>.convertTo(convert: (T) -> R): HttpRequestMessage<R> {
    val body = convert(body)

    return object : HttpRequestMessage<R> {
        override fun getUri(): URI =
            this@convertTo.uri

        override fun getHttpMethod(): HttpMethod =
            this@convertTo.httpMethod

        override fun getHeaders(): MutableMap<String, String> =
            this@convertTo.headers

        override fun getQueryParameters(): MutableMap<String, String> =
            this@convertTo.queryParameters

        override fun getBody(): R =
            body

        override fun createResponseBuilder(httpStatus: HttpStatus): HttpResponseMessage.Builder =
            this@convertTo.createResponseBuilder(httpStatus)

        override fun createResponseBuilder(httpStatusType: HttpStatusType): HttpResponseMessage.Builder =
            this@convertTo.createResponseBuilder(httpStatusType)
    }
}
