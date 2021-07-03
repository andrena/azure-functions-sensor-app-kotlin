package de.andrena.sensorapp.validation

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.OutputBinding
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.QueueOutput
import com.microsoft.azure.functions.annotation.QueueTrigger
import de.andrena.sensorapp.sensor.SensorRepository
import de.andrena.util.json.decodeJson
import java.time.OffsetDateTime

@FunctionName("SensorValidation")
fun validation(
    @QueueTrigger(name = "aggregatedSensorData", queueName = "aggregated-sensor-data", connection = "AzureWebJobsStorage")
    aggregatedSensorData: String,
    @QueueOutput(name = "output", queueName = "validated-sensor-data", connection = "AzureWebJobsStorage")
    output: OutputBinding<String>,
    context: ExecutionContext,
) {
    val decodedAggregatedSensorData = try {
        aggregatedSensorData.decodeJson<AggregatedSensorData>()
    } catch (ex: IllegalArgumentException) {
        context.logger.severe("Unable to decode AggregatedSensorData=${aggregatedSensorData}: ${ex.message}")
        return
    }

    when (val validationResult = validateAndUpdateLastSeen(decodedAggregatedSensorData, context)) {
        is ValidationResult.Error -> {
            context.logger.severe("Validation failed for AggregatedSensorData=${aggregatedSensorData} ${validationResult.message}")
        }
        ValidationResult.Ok -> context.logger.fine("Successfully validated aggregatedSensorData. $aggregatedSensorData")
    }

    output.value = aggregatedSensorData
}

private fun validateAndUpdateLastSeen(aggregatedSensorData: AggregatedSensorData, context: ExecutionContext): ValidationResult {
    context.logger.fine("Validating aggregatedSensorData. $aggregatedSensorData")

    val sensor = SensorRepository.getByIdAndType(aggregatedSensorData.sensorBoxId, aggregatedSensorData.sensorType)
        ?: return ValidationResult.Error("No sensor found for sensor box id=${aggregatedSensorData.sensorBoxId} and type ${aggregatedSensorData.sensorType}")

    sensor.lastSeenDateTime = OffsetDateTime.now()
    SensorRepository.update(sensor)

    if (aggregatedSensorData.isValidFor(sensor))
        return ValidationResult.Ok

    SensorAlarmRepository.insert(SensorAlarm.invalidData(sensor))
    return ValidationResult.Error("Invalid data")
}

sealed class ValidationResult {
    object Ok : ValidationResult()
    class Error(val message: String) : ValidationResult()
}
