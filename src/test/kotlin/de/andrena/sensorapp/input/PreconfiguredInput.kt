package de.andrena.sensorapp.input

import de.andrena.util.lang.averageOrNull
import de.andrena.util.lang.stdDev
import java.time.OffsetDateTime

fun preconfiguredInput(
    sensorBoxId: String = "box-1",
    timestamp: OffsetDateTime = OffsetDateTime.now(),
    data: List<Input.SensorData> = emptyList(),
) = Input(sensorBoxId = sensorBoxId, timestamp = timestamp, data = data)

fun preconfiguredHumiditySensorData(
    values: List<Double> = emptyList(),
) = Input.SensorData(
    type = "humidity",
    values = values,
)

fun aggregatedInputFrom(
    input: Input,
    sensorData: Input.SensorData,
) = AggregatedInput(
    sensorBoxId = input.sensorBoxId,
    timestamp = input.timestamp,
    sensorType = sensorData.type,
    min = sensorData.values.minOrNull()!!,
    max = sensorData.values.maxOrNull()!!,
    average = sensorData.values.averageOrNull()!!,
    standardDeviation = sensorData.values.stdDev()!!,
)
