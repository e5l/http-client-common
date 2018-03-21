package io.ktor.common.client

import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.experimental.io.jvm.javaio.*
import java.net.*

actual class HttpClient actual constructor() : Closeable {
    private val client = io.ktor.client.HttpClient(CIO)

    actual suspend fun request(request: HttpRequest): HttpResponse {
        val response = client.request<io.ktor.client.response.HttpResponse> {
            method = HttpMethod.parse(request.method.value)
            url.takeFrom(URL(request.url.build()))
            headers {
                request.headers.forEach { key, values -> appendAll(key, values) }
            }
            request.body?.let { body = it }
        }

        return HttpResponseBuilder(request).apply {
            statusCode = response.status.value
            response.headers.entries().forEach { (key, values) -> headers[key] = values }
            body = response.content
                    .toInputStream()
                    .reader(response.charset() ?: Charsets.UTF_8).readText()
        }.build()
    }

    override fun close() {
        client.close()
    }
}

