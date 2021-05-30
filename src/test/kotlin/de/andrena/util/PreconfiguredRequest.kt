package de.andrena.util

import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpMethod.GET
import com.microsoft.azure.functions.HttpMethod.POST
import com.microsoft.azure.functions.HttpRequestMessage
import de.andrena.util.json.jsonSerializer
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString

fun preconfiguredGetRequest(queryParameters: Map<String, String> = mapOf()): HttpRequestMessage<Nothing> =
    preconfiguredRequest<Nothing>(httpMethod = GET).also {
        every { it.queryParameters } returns queryParameters
    }

inline fun <reified T> preconfiguredPostAsJsonRequest(body: T): HttpRequestMessage<String?> =
    preconfiguredPostRequest(body = jsonSerializer.encodeToString(body))

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
