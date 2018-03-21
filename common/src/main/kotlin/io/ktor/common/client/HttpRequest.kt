package io.ktor.common.client

import io.ktor.common.client.http.*

class HttpRequest(
        val url: URLBuilder,
        val method: HttpMethod,
        val headers: Map<String, List<String>>,
        val body: String?
)

class HttpRequestBuilder {
    val url: URLBuilder = URLBuilder()
    var method: HttpMethod = HttpMethod.Get
    var body: String? = null
    var headers: MutableMap<String, List<String>> = mutableMapOf()

    fun build(): HttpRequest = HttpRequest(url, method, headers, body)
}
