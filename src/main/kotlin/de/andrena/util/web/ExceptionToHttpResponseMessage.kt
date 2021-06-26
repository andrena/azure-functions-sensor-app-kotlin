package de.andrena.util.web

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import de.andrena.util.respondBadRequest
import de.andrena.util.respondInternalServerError

fun exceptionToHttpResponseMessage(
    request: HttpRequestMessage<*>,
    block: () -> HttpResponseMessage,
): HttpResponseMessage =
    try {
        block()
    } catch (ex: IllegalArgumentException) {
        request.respondBadRequest(ex.message ?: "")
    } catch (ex: Throwable) {
        request.respondInternalServerError(ex.message ?: "")
    }
