package de.andrena.sensorapp.input

import de.andrena.util.lang.averageOrNull
import de.andrena.util.lang.stdDev
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class Input(
    private val sensorBoxId: String,
    @Contextual
    private val timestamp: Instant,
    private val data: List<SensorData>,
) {

    fun aggregate(): List<AggregatedInput> =
        data.map { data ->
            if (data.values.isEmpty()) {
                return@map null
            }

            AggregatedInput(
                sensorBoxId = sensorBoxId,
                timestamp = timestamp,
                sensorType = data.type,
                min = data.values.minOrNull(),
                max = data.values.maxOrNull(),
                average = data.values.averageOrNull(),
                standardDeviation = data.values.stdDev(),
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
    val sensorBoxId: String,
    @Contextual
    val timestamp: Instant,
    val sensorType: String,
    val min: Double?,
    val max: Double?,
    val average: Double?,
    val standardDeviation: Double?,
)
