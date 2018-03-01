package org.jetbrains.kotlin.common.httpclient

/**
 * Execute [HttpRequest] from [block]
 */
fun HttpClient.request(builder: HttpRequestBuilder.() -> Unit, block: (HttpResponse) -> Unit) =
        request(HttpRequestBuilder().apply(builder).build(), block)
