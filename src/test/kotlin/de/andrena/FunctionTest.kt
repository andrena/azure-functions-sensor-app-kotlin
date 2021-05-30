package de.andrena

import com.microsoft.azure.functions.HttpStatus.BAD_REQUEST
import com.microsoft.azure.functions.HttpStatus.OK
import de.andrena.util.mockContext
import de.andrena.util.preconfiguredGetRequest
import de.andrena.util.preconfiguredPostRequest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FunctionTest {

    @Test
    fun `get with queryParameter name returns Ok`() {
        val response = Function().get(preconfiguredGetRequest(), mockContext(), name = "Azure")

        response.status shouldBe OK
    }

    @Test
    fun `post without name in body returns BadRequest`() {
        val request = preconfiguredPostRequest<String?>(body = null)

        val response = Function().post(request, mockContext())

        response.status shouldBe BAD_REQUEST
    }

    @Test
    fun `post with name in body returns Ok`() {
        val request = preconfiguredPostRequest<String?>(body = "Azure")

        val response = Function().post(request, mockContext())

        response.status shouldBe OK
    }

}
