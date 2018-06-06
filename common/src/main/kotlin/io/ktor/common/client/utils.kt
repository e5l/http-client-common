package io.ktor.common.client

interface Closeable {
    fun close()
}

inline fun <T : Closeable, R> T.use(block: (T) -> R): R = try {
    block(this)
} finally {
    close()
}

typealias ErrorHandler = (Throwable) -> Unit

class Promise<T> {
    private var handler: (T) -> Unit = {}
    private var errorHandler: ErrorHandler = {}

    fun complete(result: T) {
        handler(result)
    }

    fun completeWithException(exception: Throwable) {
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

expect fun <T> promise(block: suspend () -> T): Promise<T>
