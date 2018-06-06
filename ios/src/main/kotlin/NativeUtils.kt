package io.ktor.common.client

import kotlin.coroutines.experimental.*

private class ContextlessContinuation<T>(private val promise: Promise<T>) : Continuation<T> {
    override val context: CoroutineContext get() = EmptyCoroutineContext

    override fun resume(value: T) {
        promise.complete(value)
    }

    override fun resumeWithException(exception: Throwable) {
        promise.completeWithException(exception)
    }
}

actual fun <T> promise(block: suspend () -> T): Promise<T> {
    val result = Promise<T>()
    block.startCoroutine(ContextlessContinuation<T>(result))
    return result
}
