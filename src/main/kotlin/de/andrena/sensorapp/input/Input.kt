package de.andrena.sensorapp.input.domain

import de.andrena.util.lang.averageOrNull
import de.andrena.util.lang.stdDev
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class Input(
    val boxId: String,
    @Contextual
    val sensedAt: Instant,
    val data: List<Data>,
) {

    fun aggregate(): List<AggregatedInput> =
        data.map { data ->
            if (data.values.isEmpty()) {
                return@map null
            }

            AggregatedInput(
                boxId = boxId,
                sensedAt = sensedAt,
                sensorType = data.sensorType,
                min = data.values.minOrNull(),
                max = data.values.maxOrNull(),
                average = data.values.averageOrNull(),
                standardDeviation = data.values.stdDev(),
            )
        }.filterNotNull()

    @Serializable
    class Data(
        val sensorType: String,
        val values: List<Double>,
    )
}

@Serializable
class AggregatedInput(
    val boxId: String,
    @Contextual
    val sensedAt: Instant,
    val sensorType: String,
    val min: Double?,
    val max: Double?,
    val average: Double?,
    val standardDeviation: Double?,
)
