package de.andrena.sensorapp.sensor

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import de.andrena.util.deserializeBodyAs
import de.andrena.util.respondWith

@FunctionName("CreateSensor")
fun createSensor(
    @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<String?>,
    context: ExecutionContext,
): HttpResponseMessage = request.deserializeBodyAs<SensorTO> {
    context.logger.info("Creating Sensor.")

    SensorRepository.insert(this.body.toSensor())

    return respondWith(status = HttpStatus.CREATED)
}
