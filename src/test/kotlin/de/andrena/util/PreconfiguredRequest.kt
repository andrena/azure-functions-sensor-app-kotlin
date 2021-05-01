package de.andrena.util

import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpMethod.GET
import com.microsoft.azure.functions.HttpMethod.POST
import com.microsoft.azure.functions.HttpRequestMessage
import de.andrena.HttpResponseMessageMock
import io.mockk.every
import io.mockk.mockk

fun preconfiguredGetRequest(queryParameters: Map<String, String>): HttpRequestMessage<Nothing> =
    preconfiguredRequest<Nothing>(httpMethod = GET).also {
        every { it.queryParameters } returns queryParameters
    }

fun <T> preconfiguredPostRequest(body: T): HttpRequestMessage<T> =
    preconfiguredRequest<T>(httpMethod = POST).also {
        every { it.body } returns body
    }

private fun <T> preconfiguredRequest(
    httpMethod: HttpMethod,
): HttpRequestMessage<T> = mockk<HttpRequestMessage<T>>().also {
    every { it.httpMethod } returns httpMethod
    every { it.createResponseBuilder(any()) } answers {
        HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(firstArg())
    }
}
