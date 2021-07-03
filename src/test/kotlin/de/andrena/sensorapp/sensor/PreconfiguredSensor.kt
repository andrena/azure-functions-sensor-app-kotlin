package de.andrena.sensorapp.sensor

import java.time.OffsetDateTime

fun preconfiguredSensor(
    sensorBoxId: String = "box-1",
    type: String = "Temperature",
    min: Double = -10.0,
    max: Double = 50.0,
    lastSeenDateTime: OffsetDateTime? = null,
) = Sensor(sensorBoxId = sensorBoxId, type = type, min = min, max = max).apply {
    if (lastSeenDateTime != null) {
        this.lastSeenDateTime = lastSeenDateTime
    }
}
