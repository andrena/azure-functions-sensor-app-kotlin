package de.andrena.sensorapp.sensor

fun preconfiguredSensorTO(
    boxId: String = "box-1",
    type: String = "humidity",
    min: Double = 1.0,
    max: Double = 3.0,
) = SensorTO(boxId = boxId, type = type, min = min, max = max)
