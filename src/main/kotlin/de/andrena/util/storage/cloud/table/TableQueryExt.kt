package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.TableEntity
import com.microsoft.azure.storage.table.TableQuery

object TableQueryExt {

    inline fun <reified T : TableEntity> tableQuery(): TableQuery<T> =
        TableQuery.from(T::class.java)

    fun <T : TableEntity> TableQuery<T>.where(block: () -> QueryCondition): TableQuery<T> =
        where(block().value)

    fun column(name: String) =
        TableQueryBuilder(name)

    class TableQueryBuilder(private val columnName: String) {

        infix fun hasValue(value: String) =
            QueryCondition(TableQuery.generateFilterCondition(columnName, TableQuery.QueryComparisons.EQUAL, value))

    }

    class QueryCondition(val value: String) {

        infix fun and(other: QueryCondition) =
            QueryCondition(TableQuery.combineFilters(value, TableQuery.Operators.AND, other.value))

    }

}
