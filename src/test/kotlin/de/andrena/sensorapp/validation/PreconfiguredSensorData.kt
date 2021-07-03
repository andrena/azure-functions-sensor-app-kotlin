package de.andrena.sensorapp.validation

import java.time.OffsetDateTime

fun preconfiguredAggregatedSensorData(
    sensorBoxId: String = "box-1",
    timestamp: OffsetDateTime = OffsetDateTime.now(),
    sensorType: String = "Temperature",
    min: Double = -10.0,
    max: Double = 40.0,
    average: Double = 18.0,
    standardDeviation: Double = 5.0,
) = AggregatedSensorData(
    sensorBoxId = sensorBoxId,
    timestamp = timestamp,
    sensorType = sensorType,
    min = min,
    max = max,
    average = average,
    standardDeviation = standardDeviation,
)
