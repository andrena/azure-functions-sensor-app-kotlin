package de.andrena.sensorapp.persistence

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.QueueTrigger
import de.andrena.sensorapp.startup.Startup
import de.andrena.sensorapp.validation.AggregatedSensorData
import de.andrena.util.json.decodeJson

@Suppress("unused")
private val startup = Startup // is used to initialize dependencies

@FunctionName("dataPersistence")
fun dataPersistence(
    @QueueTrigger(
        name = "validatedSensorData",
        queueName = "validated-sensor-data",
        connection = "AzureWebJobsStorage"
    )
    validatedSensorData: String,
    context: ExecutionContext,
) {
    val decodedValidatedSensorData = try {
        validatedSensorData.decodeJson<AggregatedSensorData>()
    } catch (ex: IllegalArgumentException) {
        context.logger.severe("Unable to decode AggregatedSensorData={$validatedSensorData}: ${ex.message}")
        return
    }

    when (val persistenceResult = writeData(decodedValidatedSensorData, context)) {
        is PersistenceResult.Error -> {
            context.logger.severe("Unable to persist sensor data ${persistenceResult.message}")
        }
        PersistenceResult.Ok -> context.logger.info("Successfully persisted sensor data")
    }

}

private fun writeData(data: AggregatedSensorData, context: ExecutionContext): PersistenceResult {
    context.logger.info("Persisting aggregatedSensorData. $data")

    val sensorData = listOf(
        createSensorData(data, "Min", data.min),
        createSensorData(data, "Max", data.max),
        createSensorData(data, "Mean", data.average),
        createSensorData(data, "StandardDeviation", data.standardDeviation)
    )
    return SensorDataService().insertBatch(sensorData)
}

private fun createSensorData(data: AggregatedSensorData, aggregationType: String, value: Double): SensorData {
    val sensorData = SensorData(
        data.sensorBoxId,
        sensorType = data.sensorType,
        aggregationType = aggregationType,
        value = value,
    )
    sensorData.createdAt = data.timestamp
    return sensorData
}