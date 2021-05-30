package de.andrena

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.BindingName
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import de.andrena.util.respondWith

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

        val name = body
            ?: return respondWith(status = HttpStatus.BAD_REQUEST, body = "Please pass a name in the request body")

        return respondWith(status = HttpStatus.OK, body = "Hello, $name!")
    }

}
