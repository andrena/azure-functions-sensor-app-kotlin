package de.andrena.sensorapp.persistence

interface SensorDataRepository {

    fun insertBatch(sensorData: List<SensorData>): PersistenceResult

}

