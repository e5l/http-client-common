package org.jetbrains.kotlin.androidtestapplication

import android.support.v7.app.*
import android.os.Bundle
import android.widget.*
import kotlinx.coroutines.experimental.android.*
import kotlinx.coroutines.experimental.*
import org.jetbrains.kotlin.common.httpclient.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlField = findViewById<EditText>(R.id.urlText)
        val responseView = findViewById<TextView>(R.id.contentView)

        findViewById<Button>(R.id.getButton).setOnClickListener {
            launch(CommonPool) {
                val client = HttpClient()
                client.request({
                    with(url) {
                        host = urlField.text.toString()
                    }
                }) { response ->
                    val text = Utils.decode(response.body, "windows-1251")
                    launch(UI) {
                        responseView.text = text
                    }
                    client.close()
                }
            }
        }
    }

}
