package de.andrena.sensorapp.input

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import de.andrena.sensorapp.input.domain.Input
import de.andrena.util.decodeBodyAs
import de.andrena.util.json.DeserializationResult
import de.andrena.util.respondBadRequest
import de.andrena.util.respondJson

@FunctionName("aggregate-input")
fun aggregateInput(
    @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<String?>,
    context: ExecutionContext,
): HttpResponseMessage = request.run {
    context.logger.info("Processing input.")

    val input = when (val result = decodeBodyAs<Input>()) {
        is DeserializationResult.Error -> return respondBadRequest(message = result.message)
        is DeserializationResult.Ok -> result.value
    }

    val aggregatedInput = input.aggregate()

    return respondJson(status = HttpStatus.OK, body = aggregatedInput)

}
