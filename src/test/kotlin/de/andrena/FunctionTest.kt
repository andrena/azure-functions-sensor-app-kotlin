package de.andrena

import com.microsoft.azure.functions.HttpStatus.OK
import de.andrena.util.mockContext
import de.andrena.util.preconfiguredGetRequest
import de.andrena.util.preconfiguredPostRequest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FunctionTest {

    @Test
    fun `get with queryParameter name returns OK`() {
        val request = preconfiguredGetRequest(queryParameters = mapOf("name" to "Azure"))

        val response = Function().get(request, mockContext())

        response.status shouldBe OK
    }

    @Test
    fun `post with name in body returns OK`() {
        val request = preconfiguredPostRequest<String?>(body = "Azure")

        val response = Function().post(request, mockContext())

        response.status shouldBe OK
    }

}
