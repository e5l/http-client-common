package org.jetbrains.kotlin.common.httpclient

object RequestTests {
    private val HEADER = "---==="

    suspend fun testGet() {
        println("$HEADER start testGet")
        val client = HttpClient()
        val response = client.request("www.google.ru")

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
