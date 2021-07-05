package de.andrena.sensorapp.validation

import de.andrena.util.storage.cloud.table.CloudTableWrapper
import de.andrena.util.storage.cloud.table.CloudTableWrapper.Companion.cloudTable
import de.andrena.util.storage.cloud.table.TableQueryExt.column

object SensorAlarmRepository {

    fun insertSingleton(alarm: SensorAlarm) {
        getByIdAndTypeAndStatus(alarm.sensorBoxId, alarm.sensorType, alarm.status) ?: insert(alarm)
    }

    fun insert(alarm: SensorAlarm) {
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

    private fun sensorAlarmsTable(): CloudTableWrapper =
        cloudTable("sensoralarms")

}
