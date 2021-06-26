package de.andrena.util

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.HttpStatus.BAD_REQUEST
import com.microsoft.azure.functions.HttpStatus.INTERNAL_SERVER_ERROR
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

inline fun <reified T : Any> HttpRequestMessage<String?>.convertBodyTo(): HttpRequestMessage<T> =
    this.convertBodyTo { body: String? ->
        if (body == null) {
            throw IllegalArgumentException("body must not be null")
        }

        body.decodeJson()
    }

inline fun <reified T, reified R> HttpRequestMessage<T>.convertBodyTo(convert: (T) -> R): HttpRequestMessage<R> {
    val body = convert(body)

    return object : HttpRequestMessage<R> {
        override fun getUri(): URI =
            this@convertBodyTo.uri

        override fun getHttpMethod(): HttpMethod =
            this@convertBodyTo.httpMethod

        override fun getHeaders(): MutableMap<String, String> =
            this@convertBodyTo.headers

        override fun getQueryParameters(): MutableMap<String, String> =
            this@convertBodyTo.queryParameters

        override fun getBody(): R =
            body

        override fun createResponseBuilder(httpStatus: HttpStatus): HttpResponseMessage.Builder =
            this@convertBodyTo.createResponseBuilder(httpStatus)

        override fun createResponseBuilder(httpStatusType: HttpStatusType): HttpResponseMessage.Builder =
            this@convertBodyTo.createResponseBuilder(httpStatusType)
    }
}
