package io.ktor.common.client

import kotlinx.cinterop.*
import platform.Foundation.*
import kotlin.coroutines.experimental.*

actual class HttpClient actual constructor() : Closeable {

    actual suspend fun request(request: HttpRequest): HttpResponse = suspendCoroutine<HttpResponse> { continuation ->
        val delegate = object : NSObject(), NSURLSessionDataDelegateProtocol {
            val receivedData = NSMutableData()

            override fun URLSession(session: NSURLSession, dataTask: NSURLSessionDataTask, didReceiveData: NSData) {
                receivedData.appendData(didReceiveData)
            }

            override fun URLSession(session: NSURLSession, task: NSURLSessionTask, didCompleteWithError: NSError?) {
                val rawResponse = task.response
                if (rawResponse == null || didCompleteWithError != null) {
                    return
                }

                val responseData = rawResponse.reinterpret<NSHTTPURLResponse>()
                val headersDict = responseData.allHeaderFields
                val headersKeys = headersDict.allKeys.toArray()

                val response = HttpResponseBuilder(request).apply {
                    statusCode = responseData.statusCode.toInt()
                    headersKeys.forEach { key ->
                        headers[key.uncheckedCast()] = listOf(headersDict.objectForKey(key).uncheckedCast())
                    }

                    body = receivedData.decode(NSWindowsCP1251StringEncoding)
                }.build()

                continuation.resume(response)
            }
        }

        val session = NSURLSession.sessionWithConfiguration(
                NSURLSessionConfiguration.defaultSessionConfiguration(),
                delegate,
                delegateQueue = NSOperationQueue.mainQueue()
        )


        val url = NSURL(URLString = request.url.build())
        val nativeRequest = NSMutableURLRequest.requestWithURL(url)
        nativeRequest.setHTTPMethod(request.method.value)

        request.body?.let {
            nativeRequest.setHTTPBody(it.uncheckedCast<NSString>().dataUsingEncoding(NSWindowsCP1251StringEncoding))
        }

        session.dataTaskWithRequest(nativeRequest).resume()
    }

    override fun close() {
    }
}

private fun NSArray.toArray(): Array<ObjCObject> = Array(count.toInt(), { it ->
    objectAtIndex(it.toLong())!!
})

private fun NSData.decode(encoding: NSStringEncoding): String {
    val nsStringMeta: NSObjectMeta = NSString
    val result = nsStringMeta.alloc()!!.reinterpret<NSString>()
    return result.initWithData(uncheckedCast(), encoding)!!
}
