package de.andrena

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.BindingName
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import de.andrena.util.DeserializationResult.Error
import de.andrena.util.DeserializationResult.Ok
import de.andrena.util.decodeBodyAs
import de.andrena.util.respondBadRequest
import de.andrena.util.respondWith
import kotlinx.serialization.Serializable

@Serializable
class Body(
    val name: String,
)

class Function {

    @FunctionName("get")
    fun get(
        @HttpTrigger(name = "request", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.FUNCTION)
        request: HttpRequestMessage<Nothing>,
        context: ExecutionContext,
        @BindingName("name")
        name: String,
    ): HttpResponseMessage = request.run {
        context.logger.info("HTTP trigger processed a GET request.")

        return respondWith(status = HttpStatus.OK, body = "Hello, $name!")
    }

    @FunctionName("post")
    fun post(
        @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.FUNCTION)
        request: HttpRequestMessage<String?>,
        context: ExecutionContext,
    ): HttpResponseMessage = request.run {
        context.logger.info("HTTP trigger processed a POST request.")

        val body = when (val result = decodeBodyAs<Body>()) {
            is Error -> return respondBadRequest(message = result.message)
            is Ok -> result.value
        }

        return respondWith(status = HttpStatus.OK, body = "Hello, ${body.name}!")
    }

}
