package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.TableBatchOperation
import com.microsoft.azure.storage.table.TableEntity
import com.microsoft.azure.storage.table.TableOperation

fun <T : TableEntity> batchInsertOperation(entities: List<T>): TableBatchOperation {
    val batch = TableBatchOperation()
    batch.addAll(entities.map { TableOperation.insert(it) })
    return batch
}
