package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.TableEntity
import io.mockk.*

object CloudTableTest {
    fun <T : TableEntity> setupCloudTable(name: String, entities: List<T> = emptyList()) = mockk<CloudTableWrapper> {
        every { CloudTableWrapper.cloudTable(name) } returns this
        every { insert(any()) } just Runs
        every { insertBatch(any<List<T>>()) } just Runs
        every { update(any()) } just Runs
        every { query<T>(any(), any()) } returns entities
    }
}

fun cloudTableTest(block: CloudTableTest.() -> Unit) {
    mockkObject(CloudTableWrapper)
    try {
        CloudTableTest.block()
    } finally {
        unmockkObject(CloudTableWrapper)
    }
}

inline fun <reified T : TableEntity> CloudTableWrapper.verifyInsert(noinline verifyEntity: (T) -> Unit = {}) {
    verify { insert(withArg<T> { verifyEntity(it) }) }
}

inline fun <reified T : TableEntity> CloudTableWrapper.verifyInsertBatch(noinline verifyEntity: (List<T>) -> Unit = {}) {
    verify { insertBatch(withArg<List<T>> { verifyEntity(it) }) }
}

inline fun <reified T : TableEntity> CloudTableWrapper.verifyUpdate(noinline verifyEntity: (T) -> Unit = {}) {
    verify { update(withArg<T> { verifyEntity(it) }) }
}
