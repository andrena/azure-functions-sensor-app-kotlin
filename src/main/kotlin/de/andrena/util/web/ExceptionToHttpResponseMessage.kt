package de.andrena.util.web

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import de.andrena.util.respondBadRequest

fun exceptionToHttpResponseMessage(
    request: HttpRequestMessage<*>,
    block: () -> HttpResponseMessage,
): HttpResponseMessage =
    try {
        block()
    } catch (ex: IllegalArgumentException) {
        request.respondBadRequest(ex.message ?: "")
    }
