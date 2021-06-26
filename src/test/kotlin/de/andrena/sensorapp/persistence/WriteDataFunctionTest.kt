package de.andrena.sensorapp.persistence

import de.andrena.sensorapp.startup.Startup
import de.andrena.sensorapp.validation.AggregatedSensorData
import de.andrena.util.json.encodedAsJson
import de.andrena.util.mockContext
import io.mockk.every
import io.mockk.mockk
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
            single { repositoryMock }
        })
        every { repositoryMock.insertBatch(any()) } returns PersistenceResult.Ok

        dataPersistence(validatedData.encodedAsJson(), mockContext())

        repositoryMock.verifyInsertBatchWasCalledWith(
            listOf(
                validatedData.toSensorData(aggregationType = "Mean", value = 50.0),
                validatedData.toSensorData(aggregationType = "Min", value = 0.0),
                validatedData.toSensorData(aggregationType = "Max", value = 100.0),
                validatedData.toSensorData(aggregationType = "StandardDeviation", value = 5.0),
            )
        )
    }
}
