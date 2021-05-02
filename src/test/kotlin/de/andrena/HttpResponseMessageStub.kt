package de.andrena

import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatusType

class HttpResponseMessageStub(
    private val httpStatus: HttpStatusType,
    private val headers: Map<String, String>,
    private val body: String,
) : HttpResponseMessage {

    override fun getStatus(): HttpStatusType = this.httpStatus

    override fun getStatusCode(): Int = httpStatus.value()

    override fun getHeader(key: String): String? = this.headers[key]

    override fun getBody(): String = this.body

    class Builder(
        private var httpStatus: HttpStatusType,
    ) : HttpResponseMessage.Builder {
        private var body: Any? = null
        private val headers: MutableMap<String, String>? = null

        override fun status(httpStatusType: HttpStatusType): HttpResponseMessage.Builder {
            this.httpStatus = httpStatusType
            return this
        }

        override fun header(key: String, value: String): HttpResponseMessage.Builder {
            this.headers!![key] = value
            return this
        }

        override fun body(body: Any): HttpResponseMessage.Builder {
            this.body = body
            return this
        }

        override fun build() =
            HttpResponseMessageStub(this.httpStatus, this.headers.orEmpty(), this.body.toString())
    }
}
