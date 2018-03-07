package org.jetbrains.kotlin.common.httpclient

import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.uncheckedCast
import kotlin.coroutines.experimental.*
import platform.Foundation.*

actual class Utils {
    actual companion object {
        actual fun decode(array: ByteArray, charset: String): String = when (charset) {
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

class Promise<T> {
    private var handler: (T) -> Unit = {}

    fun complete(result: T) {
        handler(result)
    }

    fun then(block: (T) -> Unit) {
        handler = block
    }
}

class ContextlessContinuation<T>(private val promise: Promise<T>) : Continuation<T> {
    override val context: CoroutineContext get() = EmptyCoroutineContext

    override fun resume(value: T) {
        promise.complete(value)
    }

    override fun resumeWithException(exception: Throwable) {}
}

fun <T> runSuspend(block: suspend () -> T): Promise<T> {
    val result = Promise<T>()
    block.startCoroutine(ContextlessContinuation<T>(result))
    return result
}
