package io.ktor.common.client.http

class URLBuilder(
        var protocol: URLProtocol = URLProtocol.HTTP,
        var host: String = "localhost",
        var port: Int = protocol.defaultPort,
        var user: String? = null,
        var password: String? = null,
        var encodedPath: String = "/",
        val parameters: ParametersBuilder = mutableMapOf(),
        var fragment: String = "",
        var trailingQuery: Boolean = false
) {
    fun path(vararg components: String) {
        path(components.asList())
    }

    fun path(components: List<String>) {
        encodedPath = components.joinToString("/", prefix = "/") { encodeURLPart(it) }
    }

    private fun <A : Appendable> appendTo(out: A): A {
        out.append(protocol.name)
        out.append("://")
        user?.let { usr ->
            out.append(encodeURLPart(usr))
            password?.let { pwd ->
                out.append(":")
                out.append(encodeURLPart(pwd))
            }
            out.append("@")
        }
        out.append(host)

        if (port != protocol.defaultPort) {
            out.append(":")
            out.append(port.toString())
        }

        if (!encodedPath.startsWith("/")) {
            out.append('/')
        }

        out.append(encodedPath)

        val queryParameters = parameters
        if (!queryParameters.isEmpty() || trailingQuery) {
            out.append("?")
        }

        queryParameters.formUrlEncodeTo(out)

        if (fragment.isNotEmpty()) {
            out.append('#')
            out.append(encodeURLPart(fragment))
        }

        return out
    }

    fun build(): String = appendTo(StringBuilder(256)).toString()

    override fun toString() = build()

    companion object
}
