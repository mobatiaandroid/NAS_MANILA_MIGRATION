package com.mobatia.nasmanila.fragments.calendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants


class CalendarWebViewFragment(s: String, tabCalendarReg: String) : Fragment() {
    private var mRootView: View? = null
    private var mContext: Context? = null
    private var relMain: ConstraintLayout? = null
    var activity: Activity? = null
    var mTitle: String? = null
    var mTabId: String? = null
    private var mWebView: WebView? = null
    private lateinit var progressBar: ProgressBar
    private var mwebSettings: WebSettings? = null
    private var loadingFlag = true
    private var mLoadUrl =
        "https://www.nordangliaeducation.com/our-schools/philippines/manila/international/news-and-insights/school-calendar"
    private var mErrorFlag = false
    var anim: RotateAnimation? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_calendar_web_view,
            container, false
        )
        mContext = getActivity()
        activity = getActivity()
        initialiseUI()
        getWebViewSettings()
        return mRootView
    }

    private fun getWebViewSettings() {
        progressBar.visibility = View.VISIBLE
        anim = RotateAnimation(
            0F, 360F, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim!!.setInterpolator(mContext, android.R.interpolator.linear)
        anim!!.repeatCount = Animation.INFINITE
        anim!!.duration = 1000
        progressBar.animation = anim
        progressBar.startAnimation(anim)
        mWebView!!.isFocusable = true
        mWebView!!.isFocusableInTouchMode = true
        mWebView!!.setBackgroundColor(0X00000000)
        mWebView!!.isVerticalScrollBarEnabled = false
        mWebView!!.isHorizontalScrollBarEnabled = false
        mWebView!!.webChromeClient = WebChromeClient()

        mwebSettings = mWebView!!.settings
        mwebSettings!!.saveFormData = true
        mwebSettings!!.builtInZoomControls = false
        mwebSettings!!.setSupportZoom(false)

        mwebSettings!!.pluginState = WebSettings.PluginState.ON
        mwebSettings!!.setRenderPriority(WebSettings.RenderPriority.HIGH)
        mwebSettings!!.javaScriptCanOpenWindowsAutomatically = true
        mwebSettings!!.domStorageEnabled = true
        mwebSettings!!.databaseEnabled = true
        mwebSettings!!.defaultTextEncodingName = "utf-8"
        mwebSettings!!.loadsImagesAutomatically = true

//        mWebView!!.settings.setAppCacheMaxSize((10 * 1024 * 1024).toLong()) // 5MB

//        mWebView!!.settings.setAppCachePath(
//            mContext!!.cacheDir.absolutePath
//        )
        mWebView!!.settings.allowFileAccess = true
//        mWebView!!.settings.setAppCacheEnabled(true)
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        mWebView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            override fun onPageFinished(view: WebView, url: String) {
                progressBar.clearAnimation()
                // progressBar.visibility = View.GONE
                if (AppUtils.checkInternet(mContext!!) && loadingFlag) {
                    view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    view.loadUrl(url)
                    loadingFlag = false
                } else if (!AppUtils.checkInternet(mContext!!) && loadingFlag) {
                    view.settings.cacheMode = WebSettings.LOAD_CACHE_ONLY
                    view.loadUrl(url)
                    println("CACHE LOADING")
                    loadingFlag = false
                }
            }

            override fun onReceivedError(
                view: WebView, errorCode: Int,
                description: String, failingUrl: String
            ) {
                progressBar.clearAnimation()
                progressBar.visibility = View.GONE
                if (AppUtils.checkInternet(mContext!!)) {
                    AppUtils.showAlertFinish(
                        mContext as Activity?, resources
                            .getString(R.string.common_error), "",
                        resources.getString(R.string.ok), false
                    )
                }
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }

        mErrorFlag = mLoadUrl == ""
        if (mLoadUrl != null && !mErrorFlag) {
            println("BISAD load url $mLoadUrl")
            mWebView!!.loadUrl(mLoadUrl)
        } else {
            progressBar.clearAnimation()
            //progressBar.visibility = View.GONE
            AppUtils.showAlertFinish(
                mContext as Activity?, resources
                    .getString(R.string.common_error_loading_page), "",
                resources.getString(R.string.ok), false
            )
        }
        mWebView!!.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun initialiseUI() {
        mLoadUrl =
            "https://www.nordangliaeducation.com/our-schools/philippines/manila/international/news-and-insights/school-calendar"
        var mTitleTextView: TextView? = null
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as ConstraintLayout
        mWebView = mRootView!!.findViewById<View>(R.id.webView) as WebView
        mTitleTextView = mRootView!!.findViewById(R.id.titleTextView)
        mTitleTextView.text = NaisClassNameConstants.CALENDAR
        progressBar = mRootView!!.findViewById<View>(R.id.progressBar) as ProgressBar
        relMain!!.setOnClickListener(View.OnClickListener { })
    }



}