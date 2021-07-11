package com.fastcampus.simplewebbrowser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.simplewebbrowser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        bindViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        binding.webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews() {
        binding.apply {
            etAddressBar.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val loadingUrl = v.text.toString()
                    if (URLUtil.isNetworkUrl(loadingUrl))
                        webView.loadUrl(loadingUrl)
                    else
                        webView.loadUrl("http://$loadingUrl")
                }
                false
            }

            btnBack.setOnClickListener {
                webView.goBack()
            }

            btnForward.setOnClickListener {
                webView.goForward()
            }

            btnHome.setOnClickListener {
                webView.loadUrl(DEFAULT_URL)
            }

            layoutWebView.setOnRefreshListener {
                webView.reload()
            }
        }
    }

    inner class WebViewClient : android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            binding.progressbar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            binding.apply {
                layoutWebView.isRefreshing = false
                progressbar.hide()
                btnBack.isEnabled = webView.canGoBack()
                btnForward.isEnabled = webView.canGoForward()
                etAddressBar.setText(url)
            }
        }
    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            binding.progressbar.progress = newProgress
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        private const val DEFAULT_URL = "http://www.google.com"
    }
}