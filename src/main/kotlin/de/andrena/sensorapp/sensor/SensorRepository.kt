package de.andrena.sensorapp.sensor

import com.microsoft.azure.storage.table.CloudTable
import de.andrena.sensorapp.sensor.Sensor.Companion.BOX_ID_COLUMN
import de.andrena.sensorapp.sensor.Sensor.Companion.TYPE_COLUMN
import de.andrena.util.storage.cloud.table.TableQueryExt.column
import de.andrena.util.storage.cloud.table.cloudTableClient
import de.andrena.util.storage.cloud.table.queryFirstOrNull

object SensorRepository {

    fun getByIdAndType(id: String, type: String): Sensor? =
        sensorsTable().queryFirstOrNull {
            val hasId = column(BOX_ID_COLUMN) hasValue id
            val hasType = column(TYPE_COLUMN) hasValue type

            hasId and hasType
        }

    private fun sensorsTable(): CloudTable =
        cloudTableClient.getTableReference("sensors")

}
