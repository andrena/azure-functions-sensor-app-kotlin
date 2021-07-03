package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.TableEntity
import io.mockk.*

fun setupCloudTable(name: String) = mockk<CloudTableWrapper> {
    every { CloudTableWrapper.cloudTable(name) } returns this
    every { insert(any()) } just Runs
}

inline fun <reified T : TableEntity> CloudTableWrapper.verifyInsert(noinline verifyEntity: (T) -> Unit = {}) {
    verify { insert(withArg<T> { verifyEntity(it) }) }
}
