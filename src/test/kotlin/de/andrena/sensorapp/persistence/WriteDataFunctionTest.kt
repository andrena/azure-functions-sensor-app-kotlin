package de.andrena.sensorapp.persistence

import de.andrena.sensorapp.startup.Startup
import de.andrena.sensorapp.validation.AggregatedSensorData
import de.andrena.util.json.encodedAsJson
import de.andrena.util.mockContext
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.time.OffsetDateTime

internal class WriteDataFunctionTest {

    @Suppress("unused")
    private val startup = Startup

    @Test
    fun `creates sensor data for each aggregation type`() {
        val validatedData = AggregatedSensorData("test", OffsetDateTime.now(), "Temperature", 0.0, 100.0, 50.0, 5.0)
        val repositoryMock = mockk<SensorDataRepository>()
        loadKoinModules(module(override = true) {
            single { repositoryMock as SensorDataRepository }
        })
        every { repositoryMock.insertBatch(any()) } returns PersistenceResult.Ok

        dataPersistence(validatedData.encodedAsJson(), mockContext())

        verify {
            repositoryMock.insertBatch(withArg { batch ->
                assert(batch.size == 4)
                assert(batch.any { it.aggregationType == "Mean" && it.value == 50.0 })
                assert(batch.any { it.aggregationType == "Min" && it.value == 0.0 })
                assert(batch.any { it.aggregationType == "Max" && it.value == 100.0 })
                assert(batch.any { it.aggregationType == "StandardDeviation" && it.value == 5.0 })
            })
        }
    }
}