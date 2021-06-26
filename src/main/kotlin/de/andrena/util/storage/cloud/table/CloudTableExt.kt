package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableBatchOperation
import com.microsoft.azure.storage.table.TableEntity
import com.microsoft.azure.storage.table.TableOperation
import de.andrena.util.storage.cloud.table.TableQueryExt.QueryCondition
import de.andrena.util.storage.cloud.table.TableQueryExt.tableQuery
import de.andrena.util.storage.cloud.table.TableQueryExt.where

inline fun <reified T : TableEntity> CloudTable.insert(entity: T) {
    createIfNotExists()
    execute(TableOperation.insert(entity))
}

inline fun <reified T : TableEntity> CloudTable.insertBatch(entities: List<T>) {
    createIfNotExists()
    val batch = TableBatchOperation()
    entities.forEach {
        batch.add(TableOperation.insert(it))
    }
    execute(batch)
}

inline fun <reified T : TableEntity> CloudTable.queryFirstOrNull(noinline block: () -> QueryCondition): T? =
    query<T>(block).firstOrNull()

inline fun <reified T : TableEntity> CloudTable.query(noinline block: () -> QueryCondition): List<T> {
    createIfNotExists()
    return execute(tableQuery<T>().where(block = block)).toList()
}
