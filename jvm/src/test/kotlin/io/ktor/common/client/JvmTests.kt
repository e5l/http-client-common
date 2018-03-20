package io.ktor.common.client

import kotlinx.coroutines.experimental.*
import kotlin.test.*

class JvmTests {

    @Test
    fun testGet() = runBlocking {
        RequestTests.testGet()
    }

}