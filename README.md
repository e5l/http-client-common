# http-client-common
Kotlin multiplatform http client

```kotlin
val client = HttpClient()

promise {
    client.request {
        url.apply {
            protocol = URLProtocol.HTTPS
            host = "https://en.wikipedia.org"
            encodedPath = "/wiki/Kotlin_(programming_language)"
            port = 443
        }
    }
}.then {
    println(it.body)
}
```

## Dependencies

### Gradle
```groovy
repositories {
    maven { url 'https://dl.bintray.com/e5l/http-client-common' }
}

dependencies {
    // common
    compile 'io.ktor.common.client:common:0.1.5'
    
    // javascript
    compile 'io.ktor.common.client:browser:0.1.5'
    
    // native
    compile 'io.ktor.common.client:http_client_native:0.1.5'
    
    // jvm + android
    compile 'io.ktor.common.client:jvm:0.1.5'
}
```
