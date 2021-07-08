package de.andrena.sensorapp.sensor

import com.microsoft.azure.storage.table.Ignore
import com.microsoft.azure.storage.table.TableServiceEntity
import de.andrena.util.annotation.NoArg
import de.andrena.util.storage.cloud.table.TableEntityExt.PARTITION_KEY
import de.andrena.util.storage.cloud.table.TableEntityExt.ROW_KEY
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

fun Sensor.toSensorTO() = SensorTO(
    boxId = sensorBoxId,
    type = type,
    min = min,
    max = max,
)

@Serializable
class SensorTO(
    private val boxId: String,
    private val type: String,
    private val min: Double,
    private val max: Double,
) {
    fun toSensor() = Sensor(
        sensorBoxId = boxId,
        type = type,
        min = min,
        max = max,
    )
}

@NoArg
class Sensor(
    sensorBoxId: String,
    type: String,
    var min: Double, // var necessary for Storage
    var max: Double, // var necessary for Storage
) : TableServiceEntity(sensorBoxId, type) {
    companion object {
        const val BOX_ID_COLUMN = PARTITION_KEY
        const val TYPE_COLUMN = ROW_KEY
        const val LAST_SEEN_COLUMN = "LastSeen"
    }

    val sensorBoxId: String
        get() = partitionKey
    val type: String
        get() = rowKey

    @Deprecated("Only to persist in storage table, don't use directly")
    @Suppress("MemberVisibilityCanBePrivate")
    var lastSeen: Date = Date(0L)

    var lastSeenDateTime: OffsetDateTime
        @Ignore
        get() = OffsetDateTime.from(lastSeen.toInstant().atOffset(ZoneOffset.UTC))
        @Ignore
        set(value) {
            lastSeen = Date.from(value.toInstant())
        }
}
