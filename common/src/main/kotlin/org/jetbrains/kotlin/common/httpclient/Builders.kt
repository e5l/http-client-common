package org.jetbrains.kotlin.common.httpclient

/**
 * Execute [HttpRequest] from [block]
 */
suspend fun HttpClient.request(block: HttpRequestBuilder.() -> Unit): HttpResponse =
        request(HttpRequestBuilder().apply(block).build())

suspend fun HttpClient.request(host: String): HttpResponse = request(HttpRequestBuilder().apply {
    url.host = host
}.build())
