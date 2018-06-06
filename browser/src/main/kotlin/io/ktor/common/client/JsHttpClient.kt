package io.ktor.common.client

import kotlinx.coroutines.experimental.*
import org.w3c.fetch.*
import kotlin.browser.*

actual class HttpClient actual constructor() : Closeable {
    actual suspend fun request(request: HttpRequest): HttpResponse = HttpResponseBuilder(request).apply {
        val response = fetch(request)
        statusCode = response.status.toInt()
        headers.putAll(response.headers.entries())
        body = response.receiveBody()
    }.build()

    override fun close() {
    }
}

private suspend fun Response.receiveBody(): String = suspendCancellableCoroutine { continuation ->
    text().then { continuation.resume(it) }
}

private suspend fun fetch(request: HttpRequest): Response = suspendCancellableCoroutine { continuation ->
    val headers = js("({})")
    request.headers.forEach { (key, values) ->
        headers[key] = values
    }

    val rawRequest = js("({})")
    rawRequest["method"] = request.method.value
    rawRequest["headers"] = headers
    request.body?.let { rawRequest["body"] = it }

    window.fetch(request.url.build(), rawRequest as RequestInit).then { continuation.resume(it) }
}

private fun Headers.entries(): Map<String, List<String>> {
    val result = mutableMapOf<String, List<String>>()

    asDynamic().forEach { value, key ->
        result[key as String] = listOf(value as String)
        Unit
    }

    return result
}
