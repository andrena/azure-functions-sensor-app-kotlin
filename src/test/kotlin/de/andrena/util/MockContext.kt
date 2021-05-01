package de.andrena.util

import com.microsoft.azure.functions.ExecutionContext
import io.mockk.every
import io.mockk.mockk
import java.util.logging.Logger

fun mockContext(): ExecutionContext = mockk<ExecutionContext>().also {
    every { it.logger } returns Logger.getGlobal()
}
