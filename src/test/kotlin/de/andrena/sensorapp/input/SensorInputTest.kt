package de.andrena.sensorapp.input

import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus.BAD_REQUEST
import com.microsoft.azure.functions.HttpStatus.OK
import de.andrena.util.OutputBindingTestHelper.shouldBe
import de.andrena.util.json.encodedAsJson
import de.andrena.util.mockContext
import de.andrena.util.preconfiguredPostRequest
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import org.junit.jupiter.api.Test

internal class SensorInputTest {

    @Test
    fun `without input returns BadRequest`() {
        val response = sensorInput(input = null)

        response.status shouldBe BAD_REQUEST
    }

    @Test
    fun `without input data leaves output empty`() {
        val input = preconfiguredInput(data = emptyList())
        val output = outputSpy()

        val response = sensorInput(input, output)

        output shouldBe emptyList()
        response.status shouldBe OK
    }

    @Test
    fun `with input data without values leaves output empty`() {
        val sensorData = preconfiguredHumiditySensorData(values = emptyList())
        val input = preconfiguredInput(data = listOf(sensorData))
        val output = outputSpy()

        val response = sensorInput(input, output)

        output shouldBe emptyList()
        response.status shouldBe OK
    }

    @Test
    fun `populates output with aggregated data`() {
        val sensorDataValues = listOf(1.0, 3.0)
        val sensorData = preconfiguredHumiditySensorData(values = sensorDataValues)
        val input = preconfiguredInput(data = listOf(sensorData))
        val output = outputSpy()

        val response = sensorInput(input, output)

        output shouldBe listOf(aggregatedInputFrom(input, sensorData))
        response.status shouldBe OK
    }

    private fun sensorInput(input: Input?, output: AggregatedSensorDataOutput = outputSpy()): HttpResponseMessage {
        val request = preconfiguredPostRequest(body = input?.encodedAsJson())

        return sensorInput(request, output, mockContext())
    }

    private fun outputSpy() = spyk<AggregatedSensorDataOutput>()

    private infix fun AggregatedSensorDataOutput.shouldBe(value: List<AggregatedInput>) {
        this shouldBe value.map { it.encodedAsJson() }
    }

}

