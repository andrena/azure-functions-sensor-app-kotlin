package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableEntity
import com.microsoft.azure.storage.table.TableOperation

class CloudTableWrapper private constructor(private val wrapped: CloudTable) {

    companion object {
        fun cloudTable(name: String) = CloudTableWrapper(CloudTableClient.table(name))
    }

    fun <T : TableEntity> insert(entity: T) {
        wrapped.createIfNotExists()
        wrapped.execute(TableOperation.insert(entity))
    }

}
