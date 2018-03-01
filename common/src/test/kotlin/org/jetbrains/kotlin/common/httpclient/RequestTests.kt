package org.jetbrains.kotlin.common.httpclient

import kotlin.test.*

class RequestTests {
    val HEADER = "---==="

    @Test
    fun testGet() {
        println("$HEADER start testGet")
        HttpClient().use { client ->
            client.request({
                with(url) {
                    host = "www.google.ru"
                }
            }) { response ->
                println("$HEADER org.jetbrains.kotlin.common.httpclient.request: ${response.request.url}")
                println("$HEADER response status: ${response.statusCode}")
                println("$HEADER headers:")
                response.headers.forEach { (key, values) ->
                    println("  -$key: ${values.joinToString()}")
                }
                println("$HEADER body:")
                println(Utils.decode(response.body, "windows-1251"))
            }
        }
    }
}
