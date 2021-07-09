package de.andrena.util.web

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import de.andrena.util.respondBadRequest
import de.andrena.util.respondInternalServerError
import de.andrena.util.respondNotFound

class NotFoundException(message: String) : RuntimeException(message)

fun exceptionToHttpResponseMessage(
    request: HttpRequestMessage<*>,
    block: () -> HttpResponseMessage,
): HttpResponseMessage =
    try {
        block()
    } catch (ex: NotFoundException) {
        request.respondNotFound(ex.message ?: "")
    } catch (ex: IllegalArgumentException) {
        request.respondBadRequest(ex.message ?: "")
    } catch (ex: Throwable) {
        request.respondInternalServerError(ex.message ?: "")
    }
