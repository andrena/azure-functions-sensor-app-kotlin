package de.andrena.sensorapp.validation

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.OutputBinding
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.QueueOutput
import com.microsoft.azure.functions.annotation.QueueTrigger
import de.andrena.sensorapp.Sensor
import de.andrena.util.json.DeserializationResult
import de.andrena.util.json.decodeJson

@FunctionName("validation")
fun validation(
    @QueueTrigger(name = "aggregatedSensorData", queueName = "aggregated-sensor-data", connection = "AzureWebJobsStorage")
    aggregatedSensorData: String,
    @QueueOutput(name = "output", queueName = "validated-sensor-data", connection = "AzureWebJobsStorage")
    output: OutputBinding<String>,
    context: ExecutionContext,
) {
    val validationResult = when (val result = aggregatedSensorData.decodeJson<AggregatedSensorData>()) {
        is DeserializationResult.Error -> {
            context.logger.severe("Unable to decode AggregatedSensorData={$aggregatedSensorData}: ${result.message}")
            return
        }
        is DeserializationResult.Ok -> validate(result.value, context)
    }

    when (validationResult) {
        is ValidationResult.Error -> {
            context.logger.severe("Validation failed for AggregatedSensorData={$aggregatedSensorData} ${validationResult.message}")
        }
        ValidationResult.Ok -> output.value = aggregatedSensorData
    }
}

private fun validate(aggregatedSensorData: AggregatedSensorData, context: ExecutionContext): ValidationResult {
    context.logger.info("Validating aggregatedSensorData. $aggregatedSensorData")

    val sensor = Sensor(min = 1.0, max = 5.0) // TODO: get Sensor from storage

    if (aggregatedSensorData.isValidFor(sensor))
        return ValidationResult.Ok

    return ValidationResult.Error("Invalid data")
}

sealed class ValidationResult {
    object Ok : ValidationResult()
    class Error(val message: String) : ValidationResult()
}
