package io.ktor.common.client

import kotlin.coroutines.experimental.*

class Promise<T> {
    private var handler: (T) -> Unit = {}

    fun complete(result: T) {
        handler(result)
    }

    fun then(block: (T) -> Unit) {
        handler = block
    }
}

private class ContextlessContinuation<T>(private val promise: Promise<T>) : Continuation<T> {
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
