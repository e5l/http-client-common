cmake_minimum_required(VERSION 3.8)

set(CMAKE_MODULE_PATH ${CMAKE_CURRENT_LIST_DIR}/KotlinCMakeModule)

project(http-client-common Kotlin)

konanc_library(
    NAME http-client-common
    SOURCES
        common/src/main/kotlin/org/jetbrains/kotlin/common/httpclient/http/HttpMethod.kt
        common/src/main/kotlin/org/jetbrains/kotlin/common/httpclient/http/Url.kt
        common/src/main/kotlin/org/jetbrains/kotlin/common/httpclient/utils.kt
        common/src/main/kotlin/org/jetbrains/kotlin/common/httpclient/HttpRequest.kt
        common/src/main/kotlin/org/jetbrains/kotlin/common/httpclient/HttpClient.kt
        common/src/main/kotlin/org/jetbrains/kotlin/common/httpclient/Builders.kt
        common/src/main/kotlin/org/jetbrains/kotlin/common/httpclient/HttpResponse.kt
        native/src/main/kotlin/NativeHttpClient.kt
        native/src/main/kotlin/NativeUtils.kt
        native/src/main/kotlin/macbook.kt)

