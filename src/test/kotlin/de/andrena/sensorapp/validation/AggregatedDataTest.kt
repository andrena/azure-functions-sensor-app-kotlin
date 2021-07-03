package de.andrena.sensorapp.validation

import de.andrena.sensorapp.sensor.preconfiguredSensor
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AggregatedDataTest {

    @Test
    fun `min value is validated against sensor minimum`() {
        val data = preconfiguredAggregatedSensorData(min = -15.0)
        val sensor = preconfiguredSensor(min = -10.0)

        data.isValidFor(sensor) shouldBe false
    }

    @Test
    fun `max value is validated against sensor maximum`() {
        val data = preconfiguredAggregatedSensorData(max = 80.0)
        val sensor = preconfiguredSensor(max = 50.0)

        data.isValidFor(sensor) shouldBe false
    }

}
