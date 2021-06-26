package de.andrena.sensorapp.validation

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.OutputBinding
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.QueueOutput
import com.microsoft.azure.functions.annotation.QueueTrigger
import de.andrena.sensorapp.sensor.SensorRepository
import de.andrena.util.json.decodeJson

@FunctionName("validation")
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
        context.logger.severe("Unable to decode AggregatedSensorData={$aggregatedSensorData}: ${ex.message}")
        return
    }

    when (val validationResult = validate(decodedAggregatedSensorData, context)) {
        is ValidationResult.Error -> {
            context.logger.severe("Validation failed for AggregatedSensorData={$aggregatedSensorData} ${validationResult.message}")
        }
        ValidationResult.Ok -> output.value = aggregatedSensorData
    }
}

private fun validate(aggregatedSensorData: AggregatedSensorData, context: ExecutionContext): ValidationResult {
    context.logger.info("Validating aggregatedSensorData. $aggregatedSensorData")

    val sensor = SensorRepository.getByIdAndType(aggregatedSensorData.sensorBoxId, aggregatedSensorData.sensorType)
        ?: return ValidationResult.Error("No sensor found for id=${aggregatedSensorData.sensorBoxId}")

    if (aggregatedSensorData.isValidFor(sensor))
        return ValidationResult.Ok

    return ValidationResult.Error("Invalid data")
}

sealed class ValidationResult {
    object Ok : ValidationResult()
    class Error(val message: String) : ValidationResult()
}
