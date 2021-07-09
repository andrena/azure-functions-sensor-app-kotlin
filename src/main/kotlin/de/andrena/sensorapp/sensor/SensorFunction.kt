package de.andrena.sensorapp.sensor

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import de.andrena.util.function
import de.andrena.util.functionWithJsonRequestBody
import de.andrena.util.json.encodedAsJson
import de.andrena.util.mandatoryQueryParam
import de.andrena.util.respondWith
import de.andrena.util.web.NotFoundException

@FunctionName("CreateSensor")
fun createSensor(
    @HttpTrigger(name = "request", methods = [HttpMethod.POST], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<String?>,
    context: ExecutionContext,
): HttpResponseMessage = functionWithJsonRequestBody<SensorTO>(request) {
    context.logger.fine("Creating sensor ${body}.")

    SensorRepository.insert(body.toSensor())

    respondWith(status = HttpStatus.CREATED)
}

@FunctionName("GetSensor")
fun getSensor(
    @HttpTrigger(name = "request", methods = [HttpMethod.GET], authLevel = AuthorizationLevel.ANONYMOUS)
    request: HttpRequestMessage<Unit>,
    context: ExecutionContext,
): HttpResponseMessage = function(request) {
    val sensorBoxId = mandatoryQueryParam("sensorBoxId")
    val type = mandatoryQueryParam("type")

    context.logger.fine("Getting sensor.")

    val sensor = SensorRepository.getByIdAndType(sensorBoxId, type)
        ?: throw NotFoundException("Sensor with id=$sensorBoxId and type=$type does not exist")

    respondWith(status = HttpStatus.OK, body = sensor.toSensorTO().encodedAsJson())
}
