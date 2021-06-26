package de.andrena.util

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import de.andrena.util.web.exceptionToHttpResponseMessage

inline fun <reified T : Any> functionWithRequestBody(
    request: HttpRequestMessage<String?>,
    noinline block: AzureFunction<T>,
): HttpResponseMessage = exceptionToHttpResponseMessage(request) { request.deserializeBodyAs(block) }
