package org.jetbrains.kotlin.common.httpclient

import org.khronos.webgl.*

actual class Utils {
    actual companion object {
        actual fun decode(array: ByteArray, charset: String): String =
                TextDecoder(charset).decode(array.asDynamic())
    }
}

external class TextDecoder(charset: String) {
    fun decode(buffer: ArrayBuffer): String
}
