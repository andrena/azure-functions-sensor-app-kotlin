package de.andrena.sensorapp.sensor

import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus.BAD_REQUEST
import com.microsoft.azure.functions.HttpStatus.CREATED
import de.andrena.util.json.encodedAsJson
import de.andrena.util.mockContext
import de.andrena.util.preconfiguredPostRequest
import de.andrena.util.storage.cloud.table.CloudTableWrapper
import de.andrena.util.storage.cloud.table.setupCloudTable
import de.andrena.util.storage.cloud.table.verifyInsert
import io.kotest.matchers.shouldBe
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class CreateSensorTest {

    @AfterEach
    fun afterEach() {
        unmockkAll()
    }

    @Test
    fun `without Sensor returns BadRequest`() {
        val response = createSensor(sensor = null)

        response.status shouldBe BAD_REQUEST
    }

    @Test
    fun `saves Sensor to sensors Table`() {
        mockkObject(CloudTableWrapper)
        val sensorsTable = setupCloudTable("sensors")

        val sensor = preconfiguredSensorTO()
        val response = createSensor(sensor = sensor)

        sensorsTable.verifyInsert<Sensor> { it shouldBeEquivalentTo sensor.toSensor() }
        response.status shouldBe CREATED
    }

    private fun createSensor(sensor: SensorTO?): HttpResponseMessage {
        val request = preconfiguredPostRequest(body = sensor?.encodedAsJson())

        return createSensor(request, mockContext())
    }

}
