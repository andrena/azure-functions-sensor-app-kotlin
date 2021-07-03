package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.ResultContinuation
import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.TableEntity
import com.microsoft.azure.storage.table.TableOperation
import com.microsoft.azure.storage.table.TableQuery
import de.andrena.util.storage.cloud.table.TableQueryExt.where
import kotlin.reflect.KClass

class CloudTableWrapper private constructor(private val wrapped: CloudTable) {

    companion object {
        fun cloudTable(name: String) = CloudTableWrapper(CloudTableClient.table(name))
    }

    fun <T : TableEntity> insert(entity: T) {
        wrapped.createIfNotExists()
        wrapped.execute(TableOperation.insert(entity))
    }

    fun <T : TableEntity> update(entity: T) {
        wrapped.createIfNotExists()
        entity.etag = "*"
        wrapped.execute(TableOperation.merge(entity))
    }

    inline fun <reified T : TableEntity> queryFirstOrNull(noinline block: () -> TableQueryExt.QueryCondition): T? =
        query<T>(block).firstOrNull()

    inline fun <reified T : TableEntity> query(noinline block: () -> TableQueryExt.QueryCondition): List<T> =
        query(block, T::class)

    fun <T : TableEntity> query(block: () -> TableQueryExt.QueryCondition, clazz: KClass<T>): List<T> {
        wrapped.createIfNotExists()

        val entities = ArrayList<T>()
        var continuationToken = null as ResultContinuation?
        do {
            val segment = wrapped.executeSegmented(TableQuery.from(clazz.java).where(block = block), continuationToken)
            continuationToken = segment.continuationToken
            entities.addAll(segment.results)
        } while (continuationToken != null)

        return entities
    }

}
