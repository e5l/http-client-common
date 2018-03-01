package org.jetbrains.kotlin.common.httpclient

expect class HttpClient() : Closeable {
    fun request(request: HttpRequest, block: (HttpResponse) -> Unit)
}
