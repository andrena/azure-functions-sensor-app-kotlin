package de.andrena.sensorapp.input

import de.andrena.util.lang.averageOrNull
import de.andrena.util.lang.stdDev
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
class Input(
    val sensorBoxId: String,
    @Contextual
    val timestamp: OffsetDateTime,
    val data: List<SensorData>,
) {

    fun aggregate(): List<AggregatedInput> =
        data.map { data ->
            val sensorType = data.type
            val sensorValues = data.values

            if (sensorValues.isEmpty()) {
                return@map null
            }

            AggregatedInput(
                sensorBoxId = sensorBoxId,
                timestamp = timestamp,
                sensorType = sensorType,
                min = sensorValues.minOrNull()!!,
                max = sensorValues.maxOrNull()!!,
                average = sensorValues.averageOrNull()!!,
                standardDeviation = sensorValues.stdDev()!!,
            )
        }.filterNotNull()

    @Serializable
    class SensorData(
        val type: String,
        val values: List<Double>,
    )
}

@Serializable
class AggregatedInput(
    private val sensorBoxId: String,
    @Contextual
    private val timestamp: OffsetDateTime,
    private val sensorType: String,
    private val min: Double,
    private val max: Double,
    private val average: Double,
    private val standardDeviation: Double,
)
