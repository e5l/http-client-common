package com.example.jetbrains.android_test_application

import android.os.*
import android.support.v7.app.*
import android.text.*
import android.text.method.*
import android.webkit.*
import android.widget.*
import io.ktor.common.client.*
import io.ktor.common.client.http.URLProtocol
import io.ktor.http.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.*

class MainActivity : AppCompatActivity() {
    private val client = HttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<TextView>(R.id.content).apply {
            movementMethod = ScrollingMovementMethod()
        }

        promise {
            client.request {
                url.apply {
                    protocol = URLProtocol.HTTP
                    host = "kotlinlang.org"
                }
            }
        }.then {
            val content = Html.fromHtml(it.body, Html.FROM_HTML_MODE_COMPACT)
            launch(UI) { view.text = content }
        }.catch {
            Toast.makeText(this, it.cause.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
