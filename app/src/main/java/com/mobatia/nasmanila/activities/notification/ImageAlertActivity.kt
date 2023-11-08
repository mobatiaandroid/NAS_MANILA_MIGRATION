package com.mobatia.nasmanila.activities.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageAlertActivity : AppCompatActivity() {
    private var mContext: Context? = null
    private var mWebView: WebView? = null
    private var progressBar: ProgressBar? = null
    private var mwebSettings: WebSettings? = null
    private var loadingFlag = true
    private var mLoadUrl: String? = null
    private var mErrorFlag = false
    private var extras: Bundle? = null
    private var relativeHeader: LinearLayout? = null
    private var headermanager: HeaderManager? = null
    private var back: ImageView? = null
    var home: ImageView? = null
    var anim: RotateAnimation? = null
    private var pushID = ""
    var id = ""
    var title = ""
    var message = ""
    var url = ""
    var date = ""
    var type = ""
    var day = ""
    var month = ""
    var year = ""
    var pushDate = ""
    var progressBarDialog: ProgressBarDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_alert)
        mContext = this
        extras = intent.extras
        if (extras != null) {
            pushID = extras!!.getString("PushID")!!
            day = extras!!.getString("Day")!!
            month = extras!!.getString("Month")!!
            year = extras!!.getString("Year")!!
            pushDate = extras!!.getString("PushDate")!!
        }
        initialiseUI()
        if (AppUtils.checkInternet(mContext as ImageAlertActivity)) {
            callPushNotification(pushID)
        } else {
            AppUtils.showDialogAlertDismiss(mContext as Activity?, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred)
        }
    }

    private fun callPushNotification(pushID: String) {
        lateinit var progressBar: ProgressBar
        val call: Call<ResponseBody> = ApiClient.getApiService().pushNotificationDetail(
            pushID
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressBar.visibility = View.GONE
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            val responseArray: JSONObject = jsonObject.optJSONObject("responseArray")
                            for (i in 0 until responseArray.length()) {
                                val dataObject = responseArray.getJSONObject(i.toString())
                                id = dataObject.optString("alert_type")
                                title = dataObject.optString("title")
                                message = dataObject.optString("message")
                                url = dataObject.optString("url")
                                date = dataObject.optString("created_at")
                                var pushNotificationDetail = "<!DOCTYPE html>\n" +
                                        "<html>\n" +
                                        "<head>\n" +
                                        "<style>\n" +
                                        "\n" +
                                        "@font-face {\n" +
                                        "font-family: SourceSansPro-Semibold;" +
                                        "src: url(SourceSansPro-Semibold.ttf);" +

                                        "font-family: SourceSansPro-Regular;" +
                                        "src: url(SourceSansPro-Regular.ttf);" +
                                        "}" +
                                        ".title {" +
                                        "font-family: SourceSansPro-Regular;" +
                                        "font-size:16px;" +
                                        "text-align:left;" +
                                        "color:	#46C1D0;" +
                                        "text-align: ####TEXT_ALIGN####;" +
                                        "}" +
                                        ".description {" +
                                        "font-family: SourceSansPro-Semibold;" +
                                        "text-align:justify;" +
                                        "font-size:14px;" +
                                        "color: #000000;" +
                                        "text-align: ####TEXT_ALIGN####;" +
                                        "}" +
                                        "</style>\n" + "</head>" +
                                        "<body>" +
                                        "<p class='title'>" + message
                                pushNotificationDetail = "$pushNotificationDetail<p class='description'>$day-$month-$year $pushDate</p>"
                                if (!url.equals("", ignoreCase = true)) {
                                    pushNotificationDetail = "$pushNotificationDetail<center><img src='$url'width='100%', height='auto'>"
                                }
                                pushNotificationDetail = """
                                $pushNotificationDetail</body>
                                </html>
                                """.trimIndent()
                                mLoadUrl = pushNotificationDetail
                                getWebViewSettings()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun initialiseUI() {
        relativeHeader = findViewById<View>(R.id.relativeHeader) as? LinearLayout
        mWebView = findViewById<View>(R.id.webView) as? WebView
        progressBar = findViewById<View>(R.id.progressDialog) as? ProgressBar
        progressBarDialog = ProgressBarDialog(mContext!!)
        headermanager = HeaderManager(this@ImageAlertActivity, "Notification")
        headermanager!!.getHeader(relativeHeader!!, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(R.drawable.back,
            R.drawable.back)
        back!!.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
            finish()
        }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val intent = Intent(mContext, HomeListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun getWebViewSettings() {
        progressBar!!.visibility = View.GONE
        anim = RotateAnimation(0F, 360F, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f)
        anim!!.setInterpolator(mContext, android.R.interpolator.linear)
        anim!!.repeatCount = Animation.INFINITE
        anim!!.duration = 1000
        progressBar!!.animation = anim
        progressBar!!.startAnimation(anim)
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

        mwebSettings!!.pluginState = WebSettings.PluginState.OFF
        mwebSettings!!.setRenderPriority(WebSettings.RenderPriority.HIGH)
        mwebSettings!!.javaScriptCanOpenWindowsAutomatically = true
        mwebSettings!!.domStorageEnabled = true
        mwebSettings!!.databaseEnabled = true
        mwebSettings!!.defaultTextEncodingName = "utf-8"
        mwebSettings!!.loadsImagesAutomatically = true

//        mWebView!!.settings.setAppCacheMaxSize((10 * 1024 * 1024).toLong()) // 5MB

//        mWebView!!.settings.setAppCachePath(
//            mContext!!.cacheDir.absolutePath)
        mWebView!!.settings.allowFileAccess = true
//        mWebView!!.settings.setAppCacheEnabled(true)
        mWebView!!.settings.javaScriptEnabled = true
        mWebView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        mWebView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                return if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("www.")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                    true
                } else {
                    view.loadDataWithBaseURL("file:///android_asset/fonts/", mLoadUrl!!, "text/html; charset=utf-8", "utf-8", "about:blank")
                    true
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBar!!.clearAnimation()
                if (AppUtils.checkInternet(mContext!!) && loadingFlag) {
                    view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    view.loadDataWithBaseURL("file:///android_asset/", mLoadUrl!!, "text/html; charset=utf-8", "utf-8", "about:blank")
                    loadingFlag = false
                } else if (!AppUtils.checkInternet(mContext!!) && loadingFlag) {
                    view.settings.cacheMode = WebSettings.LOAD_CACHE_ONLY

                    view.loadDataWithBaseURL("file:///android_asset/", mLoadUrl!!, "text/html; charset=utf-8", "utf-8", "about:blank")
                    loadingFlag = false
                }
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
            override fun onReceivedError(view: WebView, errorCode: Int,
                                         description: String, failingUrl: String) {
                progressBar!!.clearAnimation()
                progressBar!!.visibility = View.GONE
                if (AppUtils.checkInternet(mContext!!)) {
                    AppUtils.showAlertFinish(mContext as Activity?, resources
                        .getString(R.string.common_error), "",
                        resources.getString(R.string.ok), false)
                }
                super.onReceivedError(view, errorCode, description, failingUrl)
            }
        }
        mErrorFlag = mLoadUrl.equals("")
        if (mLoadUrl != null && !mErrorFlag) {
            mWebView!!.loadDataWithBaseURL("file:///android_asset/", mLoadUrl!!, "text/html; charset=utf-8", "utf-8", "about:blank")
        } else {
            progressBar!!.clearAnimation()
            AppUtils.showAlertFinish(mContext as Activity?, resources
                .getString(R.string.common_error_loading_page), "",
                resources.getString(R.string.ok), false)
        }
        mWebView!!.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressBar!!.progress = newProgress
                if (newProgress == 100) {
                    progressBar!!.visibility = View.GONE
                }
            }
        }
    }
}