package de.andrena.sensorapp.validation

import de.andrena.sensorapp.sensor.Sensor
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
class AggregatedSensorData(
    val sensorBoxId: String,
    @Contextual
    val timestamp: OffsetDateTime,
    val sensorType: String,
    val min: Double,
    val max: Double,
    val average: Double,
    val standardDeviation: Double,
) {
    fun isValidFor(sensor: Sensor): Boolean =
        min >= sensor.min && max <= sensor.max
}
