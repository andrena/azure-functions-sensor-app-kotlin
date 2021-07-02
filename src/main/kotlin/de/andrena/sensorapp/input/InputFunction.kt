package de.andrena.sensorapp.input

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import com.microsoft.azure.functions.annotation.QueueOutput
import de.andrena.util.functionWithJsonRequestBody
import de.andrena.util.json.encodedAsJson
import de.andrena.util.respondWith

typealias AggregatedSensorDataOutput = OutputBinding<List<String>>

@FunctionName("SensorInput")
fun sensorInput(
    @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<String?>,
    @QueueOutput(name = "output", queueName = "aggregated-sensor-data", connection = "AzureWebJobsStorage")
    output: AggregatedSensorDataOutput,
    context: ExecutionContext,
): HttpResponseMessage = functionWithJsonRequestBody<Input>(request) {
    context.logger.fine("Processing input of sensor ${body.sensorBoxId}.")

    val aggregatedInput = body.aggregate()

    output.value = aggregatedInput.map { it.encodedAsJson() }

    respondWith(status = HttpStatus.OK)
}
