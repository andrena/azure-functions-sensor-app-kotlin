package de.andrena.sensorapp.persistence

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SensorDataService : KoinComponent {

    private val repository: SensorDataRepository by inject()

    fun insertBatch(sensorData: List<SensorData>): PersistenceResult {
        return repository.insertBatch(sensorData)
    }

}