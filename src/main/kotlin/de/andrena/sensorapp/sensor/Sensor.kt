package de.andrena.sensorapp.sensor

import com.microsoft.azure.storage.table.TableServiceEntity
import de.andrena.util.annotation.NoArg
import de.andrena.util.storage.cloud.table.TableEntityExt.PARTITION_KEY
import de.andrena.util.storage.cloud.table.TableEntityExt.ROW_KEY

@NoArg
class Sensor(
    boxId: String,
    type: String,
    val min: Double,
    val max: Double,
) : TableServiceEntity(boxId, type) {
    companion object {
        const val BOX_ID_COLUMN = PARTITION_KEY
        const val TYPE_COLUMN = ROW_KEY
    }
}
