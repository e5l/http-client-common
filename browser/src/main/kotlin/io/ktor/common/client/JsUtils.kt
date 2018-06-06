package io.ktor.common.client

import kotlinx.coroutines.experimental.*

actual fun <T> promise(block: suspend () -> T): Promise<T> {
    val result = async {
        block()
    }

    return Promise<T>().also {
        result.invokeOnCompletion { cause ->
            if (cause == null) {
                it.complete(result.getCompleted())
            } else {
                it.completeWithException(cause)
            }
        }
    }
}
