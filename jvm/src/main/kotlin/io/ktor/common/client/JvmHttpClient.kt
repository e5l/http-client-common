package io.ktor.common.client

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.experimental.io.*
import kotlinx.coroutines.experimental.io.jvm.javaio.*
import java.net.*

actual class HttpClient actual constructor() : Closeable {
    private val client = io.ktor.client.HttpClient(CIO.config {
        https.randomAlgorithm = "SHA1PRNG"
    })

    actual suspend fun request(request: HttpRequest): HttpResponse {
        val contentType = request.headers["ContentType"]?.let { ContentType.parse(it.first()) }

        val response = client.request<io.ktor.client.response.HttpResponse> {
            method = HttpMethod.parse(request.method.value)
            url.takeFrom(URL(request.url.build()))
            headers {
                request.headers.forEach { key, values -> appendAll(key, values) }
            }

            body = object : OutgoingContent.ReadChannelContent() {
                override val contentLength: Long? = request.body?.length?.toLong()
                override val contentType: ContentType? = contentType

                override fun readFrom(): ByteReadChannel =
                        request.body?.let { ByteReadChannel(it.toByteArray()) }
                                ?: EmptyByteReadChannel

            }
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

