package de.andrena.sensorapp.persistence

sealed class PersistenceResult {
    object Ok : PersistenceResult()
    class Error(val message: String) : PersistenceResult()
}