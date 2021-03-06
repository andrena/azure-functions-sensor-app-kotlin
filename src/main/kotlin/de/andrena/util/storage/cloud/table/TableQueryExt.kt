package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.table.TableEntity
import com.microsoft.azure.storage.table.TableQuery
import java.util.*

object TableQueryExt {

    fun <T : TableEntity> TableQuery<T>.where(block: () -> QueryCondition): TableQuery<T> =
        where(block().value)

    fun column(name: String) =
        TableQueryBuilder(name)

    class TableQueryBuilder(private val columnName: String) {

        infix fun hasValue(value: String) =
            QueryCondition(TableQuery.generateFilterCondition(columnName, TableQuery.QueryComparisons.EQUAL, value))

        infix fun isOnOrBefore(value: Date) =
            QueryCondition(TableQuery.generateFilterCondition(columnName, TableQuery.QueryComparisons.LESS_THAN_OR_EQUAL, value))

        infix fun isAfter(value: Date) =
            QueryCondition(TableQuery.generateFilterCondition(columnName, TableQuery.QueryComparisons.GREATER_THAN, value))

    }

    class QueryCondition(val value: String) {

        infix fun and(other: QueryCondition) =
            QueryCondition(TableQuery.combineFilters(value, TableQuery.Operators.AND, other.value))

    }

}
