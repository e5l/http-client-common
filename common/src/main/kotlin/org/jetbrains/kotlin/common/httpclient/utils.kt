package org.jetbrains.kotlin.common.httpclient

interface Closeable {
    fun close()
}

fun <T : Closeable, R> T.use(block: (T) -> R): R =
        try {
            block(this)
        } finally {
            close()
        }

expect class Utils {
    companion object {
        fun decode(array: ByteArray, charset: String): String
    }
}