package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableBatchOperation
import io.mockk.verify

fun CloudTable.verifyBatchInsert() {
    verify { execute(any<TableBatchOperation>()) }
}
