package de.andrena.util

import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus

fun <T> HttpRequestMessage<T>.respondWith(status: HttpStatus, body: Any): HttpResponseMessage =
    this.createResponseBuilder(status).body(body).build()
