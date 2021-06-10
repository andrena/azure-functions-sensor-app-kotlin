package de.andrena.sensorapp.validation

import de.andrena.sensorapp.Sensor
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
class AggregatedSensorData(
    private val sensorBoxId: String,
    @Contextual
    private val timestamp: OffsetDateTime,
    private val sensorType: String,
    private val min: Double,
    private val max: Double,
    private val average: Double,
    private val standardDeviation: Double,
) {
    fun isValidFor(sensor: Sensor): Boolean =
        min >= sensor.min && max <= sensor.max
}
