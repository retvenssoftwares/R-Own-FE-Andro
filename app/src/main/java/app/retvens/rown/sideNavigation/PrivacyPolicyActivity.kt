package app.retvens.rown.sideNavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import app.retvens.rown.R

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        findViewById<ImageView>(R.id.re_backBtn).setOnClickListener { onBackPressed() }

        // Find the WebView by its unique ID
        val webView = findViewById<WebView>(R.id.web)

        // loading http://www.google.com url in the WebView.
        webView.loadUrl("https://www.r-own.com/privacy-policy")

        // this will enable the javascript.
        webView.settings.javaScriptEnabled = true

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

    }
}