package io.ktor.common.client

import io.ktor.common.client.http.*

internal val EmptyByteArray = ByteArray(0)

class HttpRequest(
        val url: Url,
        val method: HttpMethod,
        val headers: Map<String, List<String>>,
        val body: ByteArray
)

class HttpRequestBuilder {
    val url: UrlBuilder = UrlBuilder()
    var method: HttpMethod = HttpMethod.GET
    var body: ByteArray = EmptyByteArray
    var headers: MutableMap<String, List<String>> = mutableMapOf()

    fun build(): HttpRequest = HttpRequest(url.build(), method, headers, body)
}
