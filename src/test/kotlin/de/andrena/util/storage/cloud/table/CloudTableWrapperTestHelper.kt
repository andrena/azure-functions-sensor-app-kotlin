package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.TableEntity
import io.mockk.*

fun <T : TableEntity> setupCloudTable(name: String, entities: List<T> = emptyList()) = mockk<CloudTableWrapper> {
    every { CloudTableWrapper.cloudTable(name) } returns this
    every { insert(any()) } just Runs
    every { update(any()) } just Runs
    every { query<T>(any(), any()) } returns entities
}

inline fun <reified T : TableEntity> CloudTableWrapper.verifyInsert(noinline verifyEntity: (T) -> Unit = {}) {
    verify { insert(withArg<T> { verifyEntity(it) }) }
}

inline fun <reified T : TableEntity> CloudTableWrapper.verifyUpdate(noinline verifyEntity: (T) -> Unit = {}) {
    verify { update(withArg<T> { verifyEntity(it) }) }
}
