package de.andrena.sensorapp.sensor

fun preconfiguredSensor(
    sensorBoxId: String = "box-1",
    type: String = "Temperature",
    min: Double = -10.0,
    max: Double = 50.0,
) = Sensor(sensorBoxId = sensorBoxId, type = type, min = min, max = max)
