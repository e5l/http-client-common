cmake_minimum_required(VERSION 3.8)

set(CMAKE_MODULE_PATH ${CMAKE_CURRENT_LIST_DIR}/KotlinCMakeModule)

project(http-client-common Kotlin)

konanc_library(
    NAME http-client-common
    SOURCES
        common/src/main/kotlin/http/HttpMethod.kt
        common/src/main/kotlin/http/Url.kt
        common/src/main/kotlin/utils.kt
        common/src/main/kotlin/HttpRequest.kt
        common/src/main/kotlin/HttpClient.kt
        common/src/main/kotlin/Builders.kt
        common/src/main/kotlin/HttpResponse.kt
        native/src/main/kotlin/utils.kt
        native/src/main/kotlin/HttpClient.kt
        native/src/main/kotlin/macbook.kt)

