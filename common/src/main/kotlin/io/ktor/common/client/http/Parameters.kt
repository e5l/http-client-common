package io.ktor.common.client.http

typealias Parameters = Map<String, String>
typealias ParametersBuilder = MutableMap<String, String>

fun encodeURLQueryComponent(component: String): String = component // URLEncoder.encode(s, Charsets.UTF_8.name())

fun Parameters.formUrlEncodeTo(out: Appendable) {
    entries.map { (key, value) -> key to value }.formUrlEncodeTo(out)
}

fun List<Pair<String, String>>.formUrlEncodeTo(out: Appendable) {
    joinTo(out, "&") {
        "${encodeURLQueryComponent(it.first)}=${encodeURLQueryComponent(it.second)}"
    }
}

fun encodeURLPart(part: String): String = encodeURLQueryComponent(part)
        .replace("+", "%20")
        .replace("%2b", "+")
        .replace("%2B", "+")
        .replace("*", "%2A")
        .replace("%7E", "~")
