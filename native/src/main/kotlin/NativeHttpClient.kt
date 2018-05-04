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

        val URLString = request.url.build()
        val url = NSURL(URLString = URLString)
        val nativeRequest = NSMutableURLRequest.requestWithURL(url)

        request.headers.forEach { (key, values) ->
            values.forEach {
                nativeRequest.setValue(it, key)
            }
        }

        nativeRequest.setHTTPMethod(request.method.value)
        request.body?.let { nativeRequest.setHTTPBody(it.encode()) }
        session.dataTaskWithRequest(nativeRequest).resume()
    }

    override fun close() {
    }
}

private fun NSArray.toArray(): Array<ObjCObject> = Array(count.toInt(), { it ->
    objectAtIndex(it.toLong())!!
})

private fun String.encode(): NSData =
        interpretObjCPointer<NSString>(CreateNSStringFromKString(this))
                .dataUsingEncoding(NSWindowsCP1251StringEncoding)!!

private fun NSData.decode(encoding: NSStringEncoding): String {
    val nsStringMeta: NSObjectMeta = NSString
    val result = nsStringMeta.alloc()!!.reinterpret<NSString>()
    return result.initWithData(uncheckedCast(), encoding)!!
}
