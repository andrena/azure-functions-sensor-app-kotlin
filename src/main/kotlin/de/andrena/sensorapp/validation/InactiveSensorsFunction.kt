package de.andrena.sensorapp.validation

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.TimerTrigger
import de.andrena.sensorapp.sensor.SensorRepository
import java.time.OffsetDateTime

@FunctionName("InactiveSensors")
fun inactiveSensors(
    @TimerTrigger(name = "inactiveSensors", schedule = "0 */1 * * * *")
    timerInfo: String,
    context: ExecutionContext,
) {
    context.logger.fine("Check for dead and alive sensors")
    val deadLine = OffsetDateTime.now().minusMinutes(5)
    val deadSensors = SensorRepository.getDeadSensors(deadLine)
    context.logger.info("Found ${deadSensors.size} dead sensors")
    deadSensors.forEach {
        SensorAlarmRepository.insertSingleton(SensorAlarm.dead(it))
    }

    val activeSensors = SensorRepository.getActiveSensors(deadLine)
    context.logger.info("Found ${activeSensors.size} active sensors")
    activeSensors.forEach {
        val deadSensorAlarm = SensorAlarmRepository.getByIdAndTypeAndStatus(it.sensorBoxId, it.type, SensorAlarm.DEAD)
        if (deadSensorAlarm != null) {
            SensorAlarmRepository.delete(deadSensorAlarm)
        }
    }
}
