package de.andrena.sensorapp.persistence

import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.verify

infix fun SensorDataRepository.verifyInsertBatchWasCalledWith(batch: List<SensorData>) {
    verify {
        insertBatch(withArg {
            it shouldHaveSize batch.size
            batch.forEach { sensorData -> it shouldContain sensorData }
        })
    }
}
