package de.andrena.util

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.HttpStatus.*
import de.andrena.util.json.decodeJson
import java.net.URI

fun HttpRequestMessage<*>.mandatoryQueryParam(name: String): String =
    requireNotNull(queryParameters[name]) { "QueryParam $name is mandatory" }

fun <T> HttpRequestMessage<T>.respondBadRequest(message: String): HttpResponseMessage =
    this.respondWith(BAD_REQUEST, body = message)

fun <T> HttpRequestMessage<T>.respondNotFound(message: String): HttpResponseMessage =
    this.respondWith(NOT_FOUND, body = message)

fun <T> HttpRequestMessage<T>.respondInternalServerError(message: String): HttpResponseMessage =
    this.respondWith(INTERNAL_SERVER_ERROR, body = message)

fun <T> HttpRequestMessage<T>.respondWith(status: HttpStatus, body: Any): HttpResponseMessage =
    this.createResponseBuilder(status).body(body).build()

fun <T> HttpRequestMessage<T>.respondWith(status: HttpStatus): HttpResponseMessage =
    this.createResponseBuilder(status).build()

inline fun <reified T : Any> HttpRequestMessage<String?>.convertJsonBodyTo(): HttpRequestMessage<T> =
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
