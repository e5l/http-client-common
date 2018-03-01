package org.jetbrains.kotlin.common.httpclient

class HttpResponse(
        val statusCode: Int,
        val headers: Map<String, List<String>>,
        val body: ByteArray,
        val request: HttpRequest
)

class HttpResponseBuilder(val request: HttpRequest) {
    var statusCode: Int = -1
    var headers: MutableMap<String, List<String>> = mutableMapOf()
    var body = EmptyByteArray

    fun build(): HttpResponse = HttpResponse(statusCode, headers, body, request)
}
