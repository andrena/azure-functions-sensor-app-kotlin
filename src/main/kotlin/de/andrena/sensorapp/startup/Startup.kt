package de.andrena.sensorapp.startup

import de.andrena.sensorapp.persistence.SensorDataRepository
import de.andrena.sensorapp.persistence.TableStorageSensorDataRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module

object Startup {
    var writeData: org.koin.core.module.Module = module(override = true) {
        single<SensorDataRepository> {
            TableStorageSensorDataRepository()
        }
    }

    init {
        injectDependencies()
    }
}

fun injectDependencies() {
    startKoin {
        modules(Startup.writeData)
    }
}