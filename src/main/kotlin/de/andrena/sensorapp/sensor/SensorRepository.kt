package de.andrena.sensorapp.sensor

import com.microsoft.azure.storage.table.CloudTable
import de.andrena.sensorapp.sensor.Sensor.Companion.BOX_ID_COLUMN
import de.andrena.sensorapp.sensor.Sensor.Companion.LAST_SEEN_COLUMN
import de.andrena.sensorapp.sensor.Sensor.Companion.TYPE_COLUMN
import de.andrena.util.storage.cloud.table.CloudTableClient
import de.andrena.util.storage.cloud.table.CloudTableWrapper.Companion.cloudTable
import de.andrena.util.storage.cloud.table.TableQueryExt.column
import de.andrena.util.storage.cloud.table.query
import java.time.OffsetDateTime
import java.util.*

object SensorRepository {

    fun insert(sensor: Sensor) {
        cloudTable("sensors").insert(sensor)
    }

    fun update(sensor: Sensor) {
        cloudTable("sensors").update(sensor)
    }

    fun getByIdAndType(id: String, type: String): Sensor? =
        cloudTable("sensors").queryFirstOrNull {
            val hasId = column(BOX_ID_COLUMN) hasValue id
            val hasType = column(TYPE_COLUMN) hasValue type

            hasId and hasType
        }

    fun getDeadSensors(deadLine: OffsetDateTime): List<Sensor> {
        return sensorsTable().query {
            column(LAST_SEEN_COLUMN) isOnOrBefore Date.from(deadLine.toInstant())
        }
    }

    fun getActiveSensors(deadLine: OffsetDateTime): List<Sensor> {
        return sensorsTable().query {
            column(LAST_SEEN_COLUMN) isAfter Date.from(deadLine.toInstant())
        }
    }

    private fun sensorsTable(): CloudTable =
        CloudTableClient.table("sensors")

}
