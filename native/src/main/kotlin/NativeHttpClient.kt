package org.jetbrains.kotlin.common.httpclient

import kotlinx.cinterop.*
import platform.Foundation.*

actual class HttpClient actual constructor() : Closeable {

    actual fun request(request: HttpRequest, block: (HttpResponse) -> Unit) {
        val delegate = object : NSObject(), NSURLSessionDataDelegateProtocol {
            val receivedData = NSMutableData()

            override fun URLSession(session: NSURLSession, dataTask: NSURLSessionDataTask, didReceiveData: NSData) {
                receivedData.appendData(didReceiveData)
            }

            override fun URLSession(session: NSURLSession, task: NSURLSessionTask, didCompleteWithError: NSError?) {
                val response = task.response
                if (response == null || didCompleteWithError != null) {
                    return
                }

                val responseData = response.reinterpret<NSHTTPURLResponse>()
                val headersDict = responseData.allHeaderFields
                val headersKeys = headersDict.allKeys.toArray()

                block(HttpResponseBuilder(request).apply {
                    statusCode = responseData.statusCode.toInt()
                    headersKeys.forEach { key ->
                        headers[key.uncheckedCast()] = listOf(headersDict.objectForKey(key).uncheckedCast())
                    }
                    body = receivedData.uncheckedCast()
                }.build())
            }
        }

        val session = NSURLSession.sessionWithConfiguration(
                NSURLSessionConfiguration.defaultSessionConfiguration(),
                delegate,
                delegateQueue = NSOperationQueue.mainQueue()
        )

        session.dataTaskWithURL(NSURL(URLString = request.url.toString())).resume()
    }

    override fun close() {
    }
}

private fun NSArray.toArray(): Array<ObjCObject> = Array(count.toInt(), { it ->
    objectAtIndex(it.toLong())!!
})
