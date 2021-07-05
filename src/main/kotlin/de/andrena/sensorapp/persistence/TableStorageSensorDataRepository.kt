package de.andrena.sensorapp.persistence

import com.microsoft.azure.storage.StorageException
import de.andrena.util.storage.cloud.table.CloudTableWrapper
import org.koin.core.component.KoinComponent

internal class TableStorageSensorDataRepository : SensorDataRepository, KoinComponent {

    override fun insertBatch(sensorData: List<SensorData>): PersistenceResult {
        return try {
            sensorDataTable().insertBatch(sensorData)
            PersistenceResult.Ok
        } catch (e: StorageException) {
            PersistenceResult.Error(e.message ?: "")
        }
    }

    private fun sensorDataTable(): CloudTableWrapper =
        CloudTableWrapper.cloudTable("sensordata")
}
