package io.ktor.common.client

import kotlinx.coroutines.experimental.*
import org.khronos.webgl.*
import org.w3c.fetch.*
import kotlin.browser.*

actual class HttpClient actual constructor() : Closeable {
    actual suspend fun request(request: HttpRequest): HttpResponse {
        val response = fetch(request)

        return HttpResponseBuilder(request).apply {
            statusCode = response.status.toInt()
            headers.putAll(response.headers.entries())
            body = response.receiveBody()
        }.build()
    }

    override fun close() {
    }
}

private suspend fun Response.receiveBody(): ByteArray = suspendCancellableCoroutine { continuation ->
    arrayBuffer().then {
        continuation.resume(Int8Array(it).asDynamic() as ByteArray)
    }
}

private suspend fun fetch(request: HttpRequest): Response = suspendCancellableCoroutine { continuation ->
    val headers = js("({})")
    request.headers.forEach { (key, values) ->
        headers[key] = values
    }

    val rawRequest = js("({})")
    rawRequest["method"] = request.method
    rawRequest["headers"] = headers
    if (!request.body.contentEquals(EmptyByteArray)) {
        rawRequest["body"] = request.body
    }

    window.fetch(request.url.toString(), rawRequest as RequestInit).then { continuation.resume(it) }
}

private fun Headers.entries(): Map<String, List<String>> {
    val result = mutableMapOf<String, List<String>>()

    asDynamic().forEach { value, key ->
        result[key as String] = listOf(value as String)
        Unit
    }

    return result
}
