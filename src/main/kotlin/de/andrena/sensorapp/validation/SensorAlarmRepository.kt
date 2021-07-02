package de.andrena.sensorapp.validation

import com.microsoft.azure.storage.table.CloudTable
import de.andrena.util.storage.cloud.table.CloudTableClient
import de.andrena.util.storage.cloud.table.TableQueryExt.column
import de.andrena.util.storage.cloud.table.delete
import de.andrena.util.storage.cloud.table.insert
import de.andrena.util.storage.cloud.table.queryFirstOrNull

object SensorAlarmRepository {

    fun insert(alarm: SensorAlarm, singleton: Boolean) {
        if (singleton) {
            val existingSensorAlarm = getByIdAndTypeAndStatus(alarm.sensorBoxId, alarm.sensorType, alarm.status)
            if (existingSensorAlarm != null) {
                return
            }
        }

        sensorAlarmsTable().insert(alarm)
    }

    fun getByIdAndTypeAndStatus(sensorBoxId: String, sensorType: String, status: String): SensorAlarm? {
        return sensorAlarmsTable().queryFirstOrNull {
            val hasId = column(SensorAlarm.BOX_ID_COLUMN) hasValue sensorBoxId
            val hasType = column(SensorAlarm.TYPE_COLUMN) hasValue sensorType
            val hasStatus = column(SensorAlarm.STATUS_COLUMN) hasValue status

            hasId and hasType and hasStatus
        }
    }

    fun delete(alarm: SensorAlarm) {
        sensorAlarmsTable().delete(alarm)
    }

    private fun sensorAlarmsTable(): CloudTable =
        CloudTableClient.table("sensoralarms")

}