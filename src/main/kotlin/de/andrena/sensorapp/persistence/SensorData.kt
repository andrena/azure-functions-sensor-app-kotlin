package de.andrena.sensorapp.persistence

import com.microsoft.azure.storage.table.Ignore
import com.microsoft.azure.storage.table.TableServiceEntity
import de.andrena.util.annotation.NoArg
import kotlinx.serialization.Contextual
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@NoArg
class SensorData(
    sensorBoxId: String,
    var sensorType: String, // var necessary for Storage
    var aggregationType: String, // var necessary for Storage
    var value: Double, // var necessary for Storage
) : TableServiceEntity(sensorBoxId, UUID.randomUUID().toString()) {

    val sensorBoxId: String
        get() = partitionKey

    @Contextual
    var createdAt: OffsetDateTime
        @Ignore
        get() = OffsetDateTime.from(timeStamp.toInstant().atOffset(ZoneOffset.UTC))
        @Ignore
        set(value) {
            timeStamp = Date.from(value.toInstant())
        }
}