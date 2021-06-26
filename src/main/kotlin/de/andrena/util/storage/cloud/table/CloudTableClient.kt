package de.andrena.util.storage.cloud.table

import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.table.CloudTable
import com.microsoft.azure.storage.table.CloudTableClient as AzureCloudTableClient

object CloudTableClient {
    private val storageConnectionString: String by lazy {
        System.getenv("AzureWebJobsStorage")
    }

    private val cloudTableClient: AzureCloudTableClient by lazy {
        CloudStorageAccount.parse(storageConnectionString).createCloudTableClient()
    }

    fun table(name: String): CloudTable =
        cloudTableClient.getTableReference(name)

}
