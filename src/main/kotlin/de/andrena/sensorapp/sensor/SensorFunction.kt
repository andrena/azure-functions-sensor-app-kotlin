package de.andrena.sensorapp.sensor

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import de.andrena.util.functionWithRequestBody
import de.andrena.util.respondWith

@FunctionName("CreateSensor")
fun createSensor(
    @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<String?>,
    context: ExecutionContext,
): HttpResponseMessage = functionWithRequestBody<SensorTO>(request) {
    context.logger.info("Creating Sensor.")

    SensorRepository.insert(body.toSensor())

    respondWith(status = HttpStatus.CREATED)
}
