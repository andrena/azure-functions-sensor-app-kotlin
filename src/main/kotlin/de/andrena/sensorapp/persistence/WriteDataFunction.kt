package de.andrena.sensorapp.persistence

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.QueueTrigger
import de.andrena.sensorapp.startup.Startup
import de.andrena.sensorapp.validation.AggregatedSensorData
import de.andrena.util.json.decodeJson
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteDataFunction : KoinComponent {

    @Suppress("unused")
    private val startup = Startup // is used to initialize dependencies

    private val sensorDataRepository: SensorDataRepository by inject()

    @FunctionName("SensorDataWriteFunction")
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
            context.logger.severe("Unable to decode AggregatedSensorData=$validatedSensorData: ${ex.message}")
            return
        }

        when (val persistenceResult = writeData(decodedValidatedSensorData, context)) {
            is PersistenceResult.Error -> {
                context.logger.severe("Unable to persist sensor data ${persistenceResult.message}")
            }
            PersistenceResult.Ok -> context.logger.fine("Successfully persisted sensor data.")
        }

    }

    private fun writeData(data: AggregatedSensorData, context: ExecutionContext): PersistenceResult {
        context.logger.fine("Persisting aggregatedSensorData. $data")

        val sensorData = listOf(
            data.toSensorData("Min", data.min),
            data.toSensorData("Max", data.max),
            data.toSensorData("Mean", data.average),
            data.toSensorData("StandardDeviation", data.standardDeviation)
        )
        return sensorDataRepository.insertBatch(sensorData)
    }

    private fun AggregatedSensorData.toSensorData(aggregationType: String, value: Double) = SensorData(
        sensorBoxId = sensorBoxId,
        sensorType = sensorType,
        aggregationType = aggregationType,
        value = value,
    ).apply {
        createdAt = this@toSensorData.timestamp
    }

}
