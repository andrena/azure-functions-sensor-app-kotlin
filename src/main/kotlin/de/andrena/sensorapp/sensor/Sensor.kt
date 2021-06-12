package de.andrena.sensorapp.sensor

import com.microsoft.azure.storage.table.TableServiceEntity
import de.andrena.util.annotation.NoArg
import de.andrena.util.storage.cloud.table.TableEntityExt.PARTITION_KEY
import de.andrena.util.storage.cloud.table.TableEntityExt.ROW_KEY
import kotlinx.serialization.Serializable

@Serializable
class SensorTO(
    private val boxId: String,
    private val type: String,
    private val min: Double,
    private val max: Double,
) {
    fun toSensor() = Sensor(
        boxId = boxId,
        type = type,
        min = min,
        max = max,
    )
}

@NoArg
class Sensor(
    boxId: String,
    type: String,
    var min: Double, // var necessary for Storage
    var max: Double, // var necessary for Storage
) : TableServiceEntity(boxId, type) {
    companion object {
        const val BOX_ID_COLUMN = PARTITION_KEY
        const val TYPE_COLUMN = ROW_KEY
    }
}
