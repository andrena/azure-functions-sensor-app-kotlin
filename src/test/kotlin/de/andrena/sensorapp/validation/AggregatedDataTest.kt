package de.andrena.sensorapp.validation

import de.andrena.sensorapp.sensor.Sensor
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

internal class AggregatedDataTest {

    @Test
    fun `min value is validated against sensor minimum`() {
        val data = AggregatedSensorData("test", OffsetDateTime.now(), "Temperature", -15.0, 20.0, 18.0, 5.0)
        val sensor = Sensor("test", "Temperature", -10.0, 50.0)

        data.isValidFor(sensor) shouldBe false
    }

    @Test
    fun `max value is validated against sensor maximum`() {
        val data = AggregatedSensorData("test", OffsetDateTime.now(), "Temperature", 0.0, 80.0, 18.0, 5.0)
        val sensor = Sensor("test", "Temperature", -10.0, 50.0)

        data.isValidFor(sensor) shouldBe false
    }

}