package io.ktor.common.client

import kotlin.coroutines.experimental.*

typealias ErrorHandler = (Throwable) -> Unit

class Promise<T> {
    private var handler: (T) -> Unit = {}
    private var errorHandler: ErrorHandler = {}

    internal fun complete(result: T) {
        handler(result)
    }

    internal fun completeWithException(exception: Throwable) {
        errorHandler(exception)
    }

    fun then(block: (T) -> Unit): Promise<T> {
        handler = block
        return this
    }

    fun catch(errHandler: ErrorHandler): Promise<T> {
        errorHandler = errHandler
        return this
    }
}

private class ContextlessContinuation<T>(private val promise: Promise<T>) : Continuation<T> {
    override val context: CoroutineContext get() = EmptyCoroutineContext

    override fun resume(value: T) {
        promise.complete(value)
    }

    override fun resumeWithException(exception: Throwable) {
        promise.completeWithException(exception)
    }
}

fun <T> runSuspend(block: suspend () -> T): Promise<T> {
    val result = Promise<T>()
    block.startCoroutine(ContextlessContinuation<T>(result))
    return result
}
