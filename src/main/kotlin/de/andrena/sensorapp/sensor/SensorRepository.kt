package de.andrena.sensorapp.sensor

import com.microsoft.azure.storage.table.CloudTable
import de.andrena.sensorapp.sensor.Sensor.Companion.BOX_ID_COLUMN
import de.andrena.sensorapp.sensor.Sensor.Companion.TYPE_COLUMN
import de.andrena.util.storage.cloud.table.CloudTableClient
import de.andrena.util.storage.cloud.table.TableQueryExt.column
import de.andrena.util.storage.cloud.table.insert
import de.andrena.util.storage.cloud.table.queryFirstOrNull

object SensorRepository {

    fun insert(sensor: Sensor) {
        sensorsTable().insert(sensor)
    }

    fun getByIdAndType(id: String, type: String): Sensor? =
        sensorsTable().queryFirstOrNull {
            val hasId = column(BOX_ID_COLUMN) hasValue id
            val hasType = column(TYPE_COLUMN) hasValue type

            hasId and hasType
        }

    private fun sensorsTable(): CloudTable =
        CloudTableClient.table("sensors")

}
