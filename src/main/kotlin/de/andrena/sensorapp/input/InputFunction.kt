package de.andrena.sensorapp.input

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import de.andrena.util.deserializeBodyAs
import de.andrena.util.respondJson

@FunctionName("aggregate-input")
fun aggregateInput(
    @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<String?>,
    context: ExecutionContext,
): HttpResponseMessage = request.deserializeBodyAs<Input> {
    context.logger.info("Processing input.")

    val aggregatedInput = body.aggregate()

    return respondJson(status = HttpStatus.OK, body = aggregatedInput)
}
