package de.andrena.sensorapp.persistence

import de.andrena.sensorapp.validation.AggregatedSensorData
import de.andrena.util.kotest.matchers.collections.shouldContain

fun AggregatedSensorData.toSensorData(aggregationType: String, value: Double) = SensorData(
    sensorBoxId = sensorBoxId,
    sensorType = sensorType,
    aggregationType = aggregationType,
    value = value,
)

infix fun Collection<SensorData>.shouldContain(sensorData: SensorData) {
    shouldContain(sensorData) {
        partitionKey == it.partitionKey && sensorType == it.sensorType && aggregationType == it.aggregationType && value == it.value
    }
}
