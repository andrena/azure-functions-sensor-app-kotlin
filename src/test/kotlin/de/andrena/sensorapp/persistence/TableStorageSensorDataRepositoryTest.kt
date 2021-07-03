package de.andrena.sensorapp.persistence

import de.andrena.util.storage.cloud.table.cloudTableTest
import de.andrena.util.storage.cloud.table.setupCloudTable
import de.andrena.util.storage.cloud.table.verifyInsertBatch
import org.junit.jupiter.api.Test

class TableStorageSensorDataRepositoryTest {

    @Test
    fun `saves Sensor Data to sensordata Table`() = cloudTableTest {
        val sensorsTable = setupCloudTable<SensorData>("sensordata")

        val sensorData = SensorData("test", "Temperature", "Mean", 100.0)
        TableStorageSensorDataRepository().insertBatch(listOf(sensorData))

        sensorsTable.verifyInsertBatch<SensorData> { it shouldContain sensorData }
    }

}
