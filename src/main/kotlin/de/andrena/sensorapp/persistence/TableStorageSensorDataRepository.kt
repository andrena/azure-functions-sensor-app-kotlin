package de.andrena.sensorapp.persistence

import com.microsoft.azure.storage.StorageException
import com.microsoft.azure.storage.table.CloudTable
import de.andrena.util.storage.cloud.table.CloudTableClient
import de.andrena.util.storage.cloud.table.insertBatch
import org.koin.core.component.KoinComponent

internal class TableStorageSensorDataRepository : SensorDataRepository, KoinComponent {

    override fun insertBatch(sensorData: List<SensorData>): PersistenceResult {
        return try {
            sensorDataTable().insertBatch(sensorData)
            PersistenceResult.Ok
        } catch (e: StorageException) {
            PersistenceResult.Error(e.message!!)
        }
    }

    private fun sensorDataTable(): CloudTable =
        CloudTableClient.table("sensordata")
}