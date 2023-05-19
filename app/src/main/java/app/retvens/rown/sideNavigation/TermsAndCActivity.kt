package app.retvens.rown.sideNavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import app.retvens.rown.R

class TermsAndCActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_c)

        // Find the WebView by its unique ID
        val webView = findViewById<WebView>(R.id.web)

        // loading http://www.google.com url in the WebView.
        webView.loadUrl("https://www.r-own.com/terms-and-conditions")

        // this will enable the javascript.
        webView.settings.javaScriptEnabled = true

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

    }
}