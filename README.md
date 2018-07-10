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

1. Kotlin native compiler version: 0.8-dev-2701

### Gradle
```groovy
buildscript {
    ext.http_client_version = '0.1.14'
}

repositories {
    maven { url 'https://dl.bintray.com/e5l/http-client-common' }
}

dependencies {
    // common
    compile "io.ktor.common.client:common:$http_client_version"

    // javascript(browser)
    compile "io.ktor.common.client:browser:$http_client_version"

    // jvm + android
    compile "io.ktor.common.client:jvm:$http_client_version"
    
    // ios
    implementation "io.ktor.common.client:ios:$http_client_version"
}
```

```groovy
// enable gradle metadata in settings.gradle
enableFeaturePreview('GRADLE_METADATA')
```

## Samples
1. [ios](samples/ios-test-application)
2. [android](samples/android-test-application)
3. [js(browser)](samples/web-test-application)
