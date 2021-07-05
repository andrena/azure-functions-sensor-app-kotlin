package de.andrena.util

import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpMethod.POST
import com.microsoft.azure.functions.HttpRequestMessage
import io.mockk.every
import io.mockk.mockk

fun preconfiguredPostRequest(body: String?): HttpRequestMessage<String?> =
    preconfiguredRequest<String?>(httpMethod = POST).also {
        every { it.body } returns body
    }

private fun <T> preconfiguredRequest(
    httpMethod: HttpMethod,
): HttpRequestMessage<T> = mockk<HttpRequestMessage<T>>().also {
    every { it.httpMethod } returns httpMethod
    every { it.createResponseBuilder(any()) } answers {
        HttpResponseMessageStub.Builder(httpStatus = firstArg())
    }
}
