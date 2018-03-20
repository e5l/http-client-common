package io.ktor.common.client

import kotlinx.coroutines.experimental.*
import kotlin.js.*
import kotlin.test.*

class JsTests {
    @Test
    fun testGet(): Promise<Unit> = promise {
        RequestTests.testGet()
    }
}
