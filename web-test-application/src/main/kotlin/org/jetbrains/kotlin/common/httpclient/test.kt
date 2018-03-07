package org.jetbrains.kotlin.common.httpclient

import kotlinx.coroutines.experimental.*

fun main(args: Array<String>) {
    val client = HttpClient()
    promise {
        client.request {
            url.apply {
                protocol = "https"
                host = "cors-anywhere.herokuapp.com"
                path = "/google.ru"
                port = 443
            }
        }
    }.then {
        println(Utils.decode(it.body, "windows-1251"))
    }
}
