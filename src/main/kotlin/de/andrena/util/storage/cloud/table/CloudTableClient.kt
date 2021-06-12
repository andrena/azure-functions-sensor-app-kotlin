package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.table.CloudTableClient

private val storageConnectionString: String =
    System.getenv("AzureWebJobsStorage").also { println("Conn: $it") }

val cloudTableClient: CloudTableClient = CloudStorageAccount.parse(storageConnectionString).createCloudTableClient()
