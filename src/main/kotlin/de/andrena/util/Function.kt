package de.andrena.util

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import de.andrena.util.web.exceptionToHttpResponseMessage

typealias AzureFunction<T> = HttpRequestMessage<T>.() -> HttpResponseMessage

inline fun <reified T : Any> functionWithJsonRequestBody(
    request: HttpRequestMessage<String?>,
    noinline block: AzureFunction<T>,
): HttpResponseMessage = exceptionToHttpResponseMessage(request) { request.convertJsonBodyTo<T>().block() }
