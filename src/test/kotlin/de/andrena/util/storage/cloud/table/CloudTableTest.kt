package de.andrena.util.storage.cloud.table

import io.mockk.mockkObject
import io.mockk.unmockkObject

fun cloudTableTest(block: () -> Unit) {
    mockkObject(CloudTableWrapper)
    try {
        block()
    } finally {
        unmockkObject(CloudTableWrapper)
    }
}
