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

1. Kotlin native compiler version: 0.7.1
2. Gradle version: 4.7

### Gradle
```groovy
buildscript {
    ext.http_client_version = '0.1.6'
}

repositories {
    maven { url 'https://dl.bintray.com/e5l/http-client-common' }
}

dependencies {
    // common
    compile "io.ktor.common.client:common:$http_client_version"

    // javascript
    compile "io.ktor.common.client:browser:$http_client_version"

    // jvm + android
    compile "io.ktor.common.client:jvm:$http_client_version"
}
```

and for iOS:

```groovy 
// enable gradle metadata in gradle.properties
enableFeaturePreview('GRADLE_METADATA')
```
```groovy
konanArtifacts {
    program('app') {
        dependencies {
            artifactapp "io.ktor.common.client:http_client_native:$http_client_version"
        }
    }
}
```

