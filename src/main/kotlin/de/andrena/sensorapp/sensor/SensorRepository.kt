package de.andrena.sensorapp.sensor

import de.andrena.sensorapp.sensor.Sensor.Companion.BOX_ID_COLUMN
import de.andrena.sensorapp.sensor.Sensor.Companion.LAST_SEEN_COLUMN
import de.andrena.sensorapp.sensor.Sensor.Companion.TYPE_COLUMN
import de.andrena.util.storage.cloud.table.CloudTableWrapper
import de.andrena.util.storage.cloud.table.CloudTableWrapper.Companion.cloudTable
import de.andrena.util.storage.cloud.table.TableQueryExt.column
import java.time.OffsetDateTime
import java.util.*

object SensorRepository {

    fun insert(sensor: Sensor) {
        sensorsTable().insert(sensor)
    }

    fun update(sensor: Sensor) {
        sensorsTable().update(sensor)
    }

    fun getByIdAndType(id: String, type: String): Sensor? =
        sensorsTable().queryFirstOrNull {
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

    private fun sensorsTable(): CloudTableWrapper =
        cloudTable("sensors")

}
