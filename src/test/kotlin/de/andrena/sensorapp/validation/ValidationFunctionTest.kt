package de.andrena.sensorapp.validation

import de.andrena.sensorapp.sensor.Sensor
import de.andrena.sensorapp.sensor.preconfiguredSensor
import de.andrena.sensorapp.sensor.shouldBeEquivalentTo
import de.andrena.util.json.encodedAsJson
import de.andrena.util.mockContext
import de.andrena.util.storage.cloud.table.CloudTableTest
import de.andrena.util.storage.cloud.table.cloudTableTest
import de.andrena.util.storage.cloud.table.verifyInsert
import de.andrena.util.storage.cloud.table.verifyUpdate
import io.kotest.matchers.date.shouldBeAfter
import io.mockk.spyk
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

internal class ValidationFunctionTest {

    @Test
    fun `updates sensor and sets lastSeen while validating data`() = cloudTableTest {
        val previousLastSeen = OffsetDateTime.now().minusHours(1)
        val sensor = preconfiguredSensor(min = -10.0, max = 200.0, lastSeenDateTime = previousLastSeen)
        val sensorsTable = setupSensorsTable(sensors = listOf(sensor))

        validate(preconfiguredAggregatedSensorData(sensor = sensor))

        sensorsTable.verifyUpdate<Sensor> {
            it shouldBeEquivalentTo sensor
            it.lastSeenDateTime shouldBeAfter previousLastSeen
        }
    }

    @Test
    fun `creates alarm for invalid data`() = cloudTableTest {
        val sensor = preconfiguredSensor(max = 80.0).also { setupSensorsTable(sensors = listOf(it)) }
        val sensorAlarmsTable = setupSensorAlarmsTable()

        validate(preconfiguredAggregatedSensorData(sensor = sensor, max = sensor.max + 1))

        sensorAlarmsTable.verifyInsert<SensorAlarm> { it shouldBeEquivalentTo SensorAlarm.invalidData(sensor) }
    }

    private fun CloudTableTest.setupSensorsTable(sensors: List<Sensor> = emptyList()) =
        setupCloudTable("sensors", entities = sensors)

    private fun setupSensorAlarmsTable() =
        CloudTableTest.setupCloudTable<SensorAlarm>("sensoralarms")

    private fun validate(aggregatedSensorData: AggregatedSensorData) {
        validate(aggregatedSensorData.encodedAsJson(), spyk(), mockContext())
    }

}
