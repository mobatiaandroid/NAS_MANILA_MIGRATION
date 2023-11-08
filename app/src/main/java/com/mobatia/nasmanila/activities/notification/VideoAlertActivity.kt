package com.mobatia.nasmanila.activities.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.AppUtils.Companion.progressBarDialog
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoAlertActivity : AppCompatActivity() {

    var webView: WebView? = null
    var position = 0
    var proWebView: ProgressBar? = null
    private var back: ImageView? = null
    var home: ImageView? = null
    var mActivity: Activity? = null
    private var textcontent: TextView? = null
    private var relativeHeader: LinearLayout? = null
    private var headermanager: HeaderManager? = null
    var id = ""
    var title = ""
    var message = ""
    var url = ""
    var date = ""
    var day = ""
    var month = ""
    var year = ""
    var pushDate = ""
    private var pushID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_alert)
        mActivity = this

        val extra = intent.extras
        if (extra != null) {
            day = extra.getString("Day")!!
            month = extra.getString("Month")!!
            year = extra.getString("Year")!!
            pushDate = extra.getString("PushDate")!!
            pushID = extra.getString("PushID")!!
        }
        initialiseUI()
    }

    private fun initialiseUI() {
        webView = findViewById<View>(R.id.webView) as WebView

        proWebView = findViewById<View>(R.id.proWebView) as ProgressBar
        textcontent = findViewById(R.id.txtContent)
        textcontent!!.visibility = View.INVISIBLE

        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        headermanager = HeaderManager(mActivity, "Notification")
        headermanager!!.getHeader(relativeHeader!!, 0)
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(R.drawable.back,
            R.drawable.back)
        back!!.setOnClickListener { finish() }
        home = headermanager!!.getLogoButton()
        home!!.setOnClickListener {
            val `in` = Intent(mActivity, HomeListActivity::class.java)
            `in`.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(`in`)
        }

        if (AppUtils.checkInternet(mActivity!!)) {
            callPushNotification(pushID)
        } else {
            AppUtils.showDialogAlertDismiss(mActivity, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred)
        }
    }

    private fun callPushNotification(pushID: String) {
        val call: Call<ResponseBody> = ApiClient.getApiService().pushNotificationDetail(
            pushID
        )
        progressBarDialog!!.show()
        call.enqueue(object : Callback<ResponseBody> {
            @SuppressLint("SetJavaScriptEnabled")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            val responseArray: JSONObject = jsonObject.optJSONObject("responseArray")
                            for (i in 0 until responseArray.length()) {
                                val dataObject = responseArray.getJSONObject(i.toString())
                                id = dataObject.optString("id")
                                title = dataObject.optString("title")
                                message = dataObject.optString("message")
                                url = dataObject.optString("url")
                                date = dataObject.optString("date")
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
                                pushNotificationDetail += "</body>\n</html>"
                                    .trimIndent()
                                webView!!.webViewClient = HelloWebViewClient()
                                webView!!.settings.javaScriptEnabled = true
                                webView!!.settings.pluginState = WebSettings.PluginState.ON
                                webView!!.settings.builtInZoomControls = false
                                webView!!.settings.displayZoomControls = true
                                webView!!.webViewClient = HelloWebViewClient()
                                val frameVideo = "<html>" + "<br><iframe width=\"320\" height=\"250\" src=\""
                                val urlVideo = "$frameVideo$url\" frameborder=\"0\" allowfullscreen></iframe></body></html>"
                                val urlYoutube = urlVideo.replace("watch?v=", "embed/")
                                proWebView!!.visibility = View.VISIBLE
                                webView!!.loadData(urlYoutube, "text/html", "utf-8")
                                proWebView!!.visibility = View.GONE
                            }
                        } else {
                            Toast.makeText(mActivity, "Some Error Occured", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressBarDialog!!.dismiss()
            }
        })
    }

    class HelloWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

        }
    }
}