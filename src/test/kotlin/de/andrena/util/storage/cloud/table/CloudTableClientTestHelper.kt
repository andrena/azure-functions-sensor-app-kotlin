package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableBatchOperation
import com.microsoft.azure.storage.table.TableOperation
import io.mockk.every
import io.mockk.mockk

fun CloudTableClient.setupCloudTable(name: String): CloudTable = mockk<CloudTable> {
    every { createIfNotExists() } returns true
    every { execute(any<TableOperation>()) } returns mockk()
    every { execute(any<TableBatchOperation>()) } returns mockk()
}.also {
    every { table(name) } returns it
}
