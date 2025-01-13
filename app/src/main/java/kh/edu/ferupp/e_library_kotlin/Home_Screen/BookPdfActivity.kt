package kh.edu.ferupp.e_library_kotlin.Home_Screen

import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kh.edu.ferupp.e_library_kotlin.R

class BookPdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_pdf)

        // Get the PDF URL from the intent
        val pdfUrl: String? = intent.getStringExtra("book_pdf_url")

        // Log the value of pdfUrl
        Log.d("BookPdfActivity", "PDF URL: " + pdfUrl)

        // Load PDF in WebView
        val webView: WebView = findViewById(R.id.webView)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Log a message when the PDF finishes loading
                Log.d("WebView", "PDF loaded successfully")
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                // Log any errors that occur during PDF loading
                Log.e("WebView", "Error loading PDF: $description")
            }
        }

        // Load the PDF URL
        pdfUrl?.let {
            webView.loadUrl(it)
        }
    }
}