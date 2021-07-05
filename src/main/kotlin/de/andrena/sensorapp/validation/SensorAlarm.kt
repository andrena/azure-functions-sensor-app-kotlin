package de.andrena.sensorapp.validation

import com.microsoft.azure.storage.table.Ignore
import com.microsoft.azure.storage.table.TableServiceEntity
import de.andrena.sensorapp.sensor.Sensor
import de.andrena.util.annotation.NoArg
import de.andrena.util.storage.cloud.table.TableEntityExt
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@NoArg
class SensorAlarm(
    sensorBoxId: String,
    var sensorType: String,
    var status: String
) : TableServiceEntity(sensorBoxId, UUID.randomUUID().toString()) {
    companion object {
        const val BOX_ID_COLUMN = TableEntityExt.PARTITION_KEY
        const val TYPE_COLUMN = "SensorType"
        const val STATUS_COLUMN = "Status"
        const val INVALID_DATA = "InvalidData"
        const val DEAD = "Dead"

        fun dead(sensor: Sensor): SensorAlarm {
            return SensorAlarm(sensor.sensorBoxId, sensor.type, DEAD)
        }

        fun invalidData(sensor: Sensor): SensorAlarm {
            return SensorAlarm(sensor.sensorBoxId, sensor.type, INVALID_DATA)
        }
    }

    val sensorBoxId: String
        get() = partitionKey

    @Suppress("unused")
    var firedAt: OffsetDateTime
        @Ignore
        get() = OffsetDateTime.from(timeStamp.toInstant().atOffset(ZoneOffset.UTC))
        @Ignore
        set(value) {
            timeStamp = Date.from(value.toInstant())
        }

    init {
        timestamp = Date.from(OffsetDateTime.now().toInstant())
    }
}
