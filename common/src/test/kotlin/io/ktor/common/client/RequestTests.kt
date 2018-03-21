package io.ktor.common.client

import io.ktor.common.client.http.*

object RequestTests {
    private val HEADER = "---==="

    suspend fun testGet() {
        println("$HEADER start testGet")
        val client = HttpClient()
        val response = client.request {
            url.apply {
                protocol = URLProtocol.HTTPS
                host = "cors-anywhere.herokuapp.com"
                encodedPath = "/google.ru"
                port = 443
            }
        }
        println("$HEADER request: ${response.request.url}")
        println("$HEADER response status: ${response.statusCode}")
        println("$HEADER headers:")
        response.headers.forEach { (key, values) ->
            println("  -$key: ${values.joinToString()}")
        }
        println("$HEADER body:")
        println(response.body.take(300))
    }
}
