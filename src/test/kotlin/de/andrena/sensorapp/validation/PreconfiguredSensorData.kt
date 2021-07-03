package de.andrena.sensorapp.validation

import de.andrena.sensorapp.sensor.Sensor
import de.andrena.sensorapp.sensor.preconfiguredSensor
import de.andrena.util.lang.stdDev
import java.time.OffsetDateTime

fun preconfiguredAggregatedSensorData(
    sensor: Sensor = preconfiguredSensor(),
    sensorBoxId: String = sensor.sensorBoxId,
    timestamp: OffsetDateTime = OffsetDateTime.now(),
    sensorType: String = sensor.type,
    min: Double = sensor.min,
    max: Double = sensor.max,
    average: Double = listOf(min, max).average(),
    standardDeviation: Double = listOf(min, max).stdDev()!!,
) = AggregatedSensorData(
    sensorBoxId = sensorBoxId,
    timestamp = timestamp,
    sensorType = sensorType,
    min = min,
    max = max,
    average = average,
    standardDeviation = standardDeviation,
)
