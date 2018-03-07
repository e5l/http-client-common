package org.jetbrains.kotlin.common.httpclient

import kotlinx.coroutines.experimental.*
import kotlin.test.*

class JvmTests {

    @Test
    fun testGet() = runBlocking {
        RequestTests.testGet()
    }

}