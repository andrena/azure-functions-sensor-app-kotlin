package de.andrena.sensorapp.validation

import com.microsoft.azure.functions.OutputBinding
import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableQuery
import de.andrena.sensorapp.sensor.Sensor
import de.andrena.sensorapp.sensor.preconfiguredSensor
import de.andrena.util.json.encodedAsJson
import de.andrena.util.mockContext
import de.andrena.util.storage.cloud.table.*
import io.kotest.matchers.date.shouldBeAfter
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class ValidationFunctionTest {

    @Test
    fun `updates sensor and sets lastSeen while validating data`() {
        mockkObject(CloudTableClient)
        val sensorsTable = CloudTableClient.setupCloudTable("sensors")
        val lastSeen = OffsetDateTime.of(2021, 6, 30, 0, 0, 0, 0, ZoneOffset.UTC)
        val sensor = preconfiguredSensor(min = -10.0, max = 200.0)
        sensor.lastSeenDateTime = lastSeen
        useSensor(sensorsTable, sensor)

        val dataToValidate = preconfiguredAggregatedSensorData(sensor = sensor)
        val output = spyk<OutputBinding<String>>()

        validation(dataToValidate.encodedAsJson(), output, mockContext())

        sensorsTable.verifyUpdate()
        sensor.lastSeenDateTime shouldBeAfter lastSeen
    }

    @Test
    fun `creates alarm for invalid data`() = cloudTableTest {
        mockkObject(CloudTableClient)
        val sensorsTable = CloudTableClient.setupCloudTable("sensors")
        val sensorAlarmsTable = setupCloudTable("sensoralarms")
        val sensor = preconfiguredSensor(max = 80.0)
        useSensor(sensorsTable, sensor)

        val dataToValidate = preconfiguredAggregatedSensorData(sensor = sensor, max = sensor.max + 1)
        val output = spyk<OutputBinding<String>>()

        validation(dataToValidate.encodedAsJson(), output, mockContext())

        sensorAlarmsTable.verifyInsert<SensorAlarm> { it shouldBeEquivalentTo SensorAlarm.invalidData(sensor) }
    }

    private fun useSensor(
        sensorsTable: CloudTable,
        sensor: Sensor
    ) {
        every { sensorsTable.executeSegmented(any<TableQuery<Sensor>>(), null) } returns mockk {
            every { continuationToken } returns null
            every { results } returns ArrayList(listOf(sensor))
        }
    }

}
