package io.ktor.common.client

import io.ktor.common.client.http.*
import kotlinx.coroutines.experimental.*

fun main(args: Array<String>) {
    val client = HttpClient()
    promise {
        client.request {
            url.apply {
                protocol = URLProtocol.HTTPS
                host = "cors-anywhere.herokuapp.com"
                encodedPath = "/google.ru"
                port = 443
            }
        }
    }.then {
        println(it.body)
    }
}
