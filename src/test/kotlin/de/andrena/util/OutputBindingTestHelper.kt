package de.andrena.util

import com.microsoft.azure.functions.OutputBinding
import io.mockk.verify

object OutputBindingTestHelper {

    infix fun <T> OutputBinding<T>.shouldBe(value: T) {
        verify { this@shouldBe[OutputBinding<T>::setValue.name].invoke(value) }
    }

}
