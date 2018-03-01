package org.jetbrains.kotlin.common.httpclient

import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.uncheckedCast
import platform.Foundation.*

actual class Utils {
    actual companion object {
        actual fun decode(array: ByteArray, charset: String): String = when(charset) {
            "windows-1251" -> array.decode(NSWindowsCP1251StringEncoding)
            else -> error("Unknown charset: $charset")
        }
    }
}

private fun ByteArray.decode(encoding: NSStringEncoding): String {
    val nsStringMeta: NSObjectMeta = NSString
    val result = nsStringMeta.alloc()!!.reinterpret<NSString>()
    return result.initWithData(uncheckedCast(), encoding)!!
}
