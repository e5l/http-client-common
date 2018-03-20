package io.ktor.common.client

expect class HttpClient() : Closeable {
    suspend fun request(request: HttpRequest): HttpResponse
}
