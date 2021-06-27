package de.andrena.sensorapp.persistence

import de.andrena.util.storage.cloud.table.CloudTableClient
import de.andrena.util.storage.cloud.table.setupCloudTable
import de.andrena.util.storage.cloud.table.verifyBatchInsert
import io.mockk.mockkObject
import org.junit.jupiter.api.Test

class TableStorageSensorDataRepositoryTest {

    @Test
    fun `saves Sensor Data to sensordata Table`() {
        mockkObject(CloudTableClient)
        val sensorsTable = CloudTableClient.setupCloudTable("sensordata")

        TableStorageSensorDataRepository().insertBatch(listOf(SensorData("test", "Temperature", "Mean", 100.0)))

        sensorsTable.verifyBatchInsert()
    }

}