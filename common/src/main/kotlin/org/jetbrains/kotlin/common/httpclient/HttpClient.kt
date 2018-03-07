package org.jetbrains.kotlin.common.httpclient

expect class HttpClient() : Closeable {
    suspend fun request(request: HttpRequest): HttpResponse
}
