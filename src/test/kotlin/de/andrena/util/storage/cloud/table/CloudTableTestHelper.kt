package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableBatchOperation
import com.microsoft.azure.storage.table.TableOperation
import io.mockk.verify

fun CloudTable.verifyInsert() {
    verify { execute(any<TableOperation>()) }
}

fun CloudTable.verifyBatchInsert() {
    verify { execute(any<TableBatchOperation>()) }
}

fun CloudTable.verifyUpdate() {
    verify { execute(any<TableOperation>()) }
}