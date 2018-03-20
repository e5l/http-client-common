package io.ktor.common.client

object RequestTests {
    private val HEADER = "---==="

    suspend fun testGet() {
        println("$HEADER start testGet")
        val client = HttpClient()
        val response = client.request {
            url.apply {
                protocol = "https"
                host = "cors-anywhere.herokuapp.com"
                path = "/google.ru"
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
        println(Utils.decode(response.body, "windows-1251").take(300))
    }
}
