package de.andrena.sensorapp.input

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import com.microsoft.azure.functions.annotation.QueueOutput
import de.andrena.util.deserializeBodyAs
import de.andrena.util.json.encodedAsJson
import de.andrena.util.respondJson

typealias AggregatedSensorDataOutput = OutputBinding<List<String>>

@FunctionName("SensorInput")
fun sensorInput(
    @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<String?>,
    @QueueOutput(name = "output", queueName = "aggregated-sensor-data", connection = "AzureWebJobsStorage")
    output: AggregatedSensorDataOutput,
    context: ExecutionContext,
): HttpResponseMessage = request.deserializeBodyAs<Input> {
    context.logger.info("Processing input.")

    val aggregatedInput = body.aggregate()

    output.value = aggregatedInput.map { it.encodedAsJson() }

    return respondJson(status = HttpStatus.OK, body = aggregatedInput)
}
