package io.ktor.common.client.http

class Url(
        val protocol: String,
        val host: String,
        val port: Int,
        val path: String,
        val queryParameters: Map<String, String>
) {
    override fun toString(): String = "$protocol://$host:$port$path${queryParameters.asStringParameters()}"
}

class UrlBuilder {
    var protocol: String = "http"
    var host: String = "0.0.0.0"
    var port: Int = 80

    var path: String = "/"
    var queryParameters: MutableMap<String, String> = mutableMapOf()

    fun build(): Url = Url(protocol, host, port, path, queryParameters)
}

private fun Map<String, String>.asStringParameters(): String =
        if (isEmpty()) "" else entries.joinToString(separator = "&") { (key, value) -> "$key=$value" }
