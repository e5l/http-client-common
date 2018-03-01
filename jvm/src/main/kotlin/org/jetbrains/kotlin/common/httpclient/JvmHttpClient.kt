package org.jetbrains.kotlin.common.httpclient

import io.ktor.cio.*
import kotlinx.coroutines.experimental.*
import java.net.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*

actual class HttpClient actual constructor() : Closeable {
    private val client = io.ktor.client.HttpClient(CIO)

    actual fun request(request: HttpRequest, block: (HttpResponse) -> Unit) = runBlocking {
        val response = client.request<io.ktor.client.response.HttpResponse> {
            method = HttpMethod.parse(request.method.name)
            url.takeFrom(URL(request.url.toString()))
            headers {
                request.headers.forEach { key, values -> appendAll(key, values) }
            }
            body = object : OutgoingContent.ByteArrayContent() {
                override fun bytes(): ByteArray = request.body
            }
        }


        block(HttpResponseBuilder(request).apply {
            statusCode = response.status.value
            response.headers.entries().forEach { (key, values) -> headers[key] = values }
            body = response.receiveContent().readChannel().toByteArray()
        }.build())
    }

    override fun close() {
        client.close()
    }
}

