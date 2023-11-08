package com.mobatia.nasmanila.activities.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Secure.*
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var context: Context
    lateinit var userNameEdtTxt: EditText
    lateinit var passwordEdtTxt: EditText
    lateinit var needPasswordBtn: Button
    lateinit var guestUserButton: Button
    lateinit var loginBtn: Button
    lateinit var signUpBtn: Button
    lateinit var mailEdtText: EditText
    lateinit var helpButton: Button
    lateinit var progressBar: ProgressBar
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this
        PreferenceManager.setIsFirstLaunch(context as LoginActivity, false)
        initialiseUI()
        setListeners()
    }

    private fun initialiseUI() {
        progressBar = (findViewById<View>(R.id.progressBar) as ProgressBar?)!!
        userNameEdtTxt = findViewById<View>(R.id.userEditText) as EditText
        dialog = Dialog(context, R.style.NewDialog)
        progressBar = findViewById(R.id.progressBar)
        userNameEdtTxt.setOnEditorActionListener { v, actionId, event ->
            userNameEdtTxt.isFocusable = false
            userNameEdtTxt.isFocusableInTouchMode = false
            false
        }
        passwordEdtTxt = findViewById<View>(R.id.passwordEditText) as EditText
        passwordEdtTxt.setOnEditorActionListener { v, actionId, event ->
            passwordEdtTxt.isFocusable = false
            passwordEdtTxt.isFocusableInTouchMode = false
            false
        }
        helpButton = findViewById<View>(R.id.helpButton) as Button
        needPasswordBtn = findViewById<View>(R.id.forgotPasswordButton) as Button
        guestUserButton = findViewById<View>(R.id.guestButton) as Button
        loginBtn = findViewById<View>(R.id.loginBtn) as Button
        signUpBtn = findViewById<View>(R.id.signUpButton) as Button

        needPasswordBtn.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                context,
                R.drawable.forgotpassword,
                R.drawable.forgotpasswordpress
            )
        )
        guestUserButton.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                context,
                R.drawable.guest,
                R.drawable.guestpress
            )
        )
        loginBtn.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                context,
                R.drawable.login,
                R.drawable.loginpress
            )
        )
        signUpBtn.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                context,
                R.drawable.signup_new,
                R.drawable.signuppress_new
            )
        )
        helpButton.setBackgroundDrawable(
            AppUtils.getButtonDrawableByScreenCategory(
                context,
                R.drawable.help,
                R.drawable.helppress
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        userNameEdtTxt.setOnTouchListener { v, event ->
            userNameEdtTxt.isFocusable = true
            userNameEdtTxt.isFocusableInTouchMode = true
            false
        }
        passwordEdtTxt.setOnTouchListener { v, event ->
            passwordEdtTxt.isFocusable = true
            passwordEdtTxt.isFocusableInTouchMode = true
            false
        }
        loginBtn.setOnClickListener {
            AppUtils.hideKeyboard(context)
            if (userNameEdtTxt.text.toString().trim().equals("", ignoreCase = true))
                AppUtils.showDialogAlertDismiss(
                    context as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            else if (!AppUtils.isValidEmail(userNameEdtTxt?.text.toString()))
                AppUtils.showDialogAlertDismiss(
                    context as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_valid_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            else if (passwordEdtTxt.text.toString().equals("", ignoreCase = true))
                AppUtils.showDialogAlertDismiss(
                    context as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_password
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            else
                loginApiCall()
        }
        guestUserButton.setOnClickListener {
            AppUtils.hideKeyboard(context)
            PreferenceManager.setUserID(context, "")
            val homeIntent = Intent(context, HomeListActivity::class.java)
            startActivity(homeIntent)
        }
        signUpBtn.setOnClickListener {
            AppUtils.hideKeyboard(context)
            if (context?.let { AppUtils.checkInternet(it) } == true)
                showSignUpAlertDialog()
            else
                AppUtils.showDialogAlertDismiss(
                    context as Activity?, "Network Error", getString(
                        R.string.no_internet
                    ), R.drawable.nonetworkicon, R.drawable.roundred
                )
        }
        helpButton.setOnClickListener { v ->
            var emailIntent = Intent(Intent.ACTION_SEND)
            val deliveryAddress = "appsupport@naismanila.edu.ph"
    //            emailIntent.putExtra(Intent.EXTRA_EMAIL, Uri.parse(deliveryAddress))
    //            emailIntent.data = Uri.parse("mailto:")
    //            emailIntent.type = "text/plain"
    //            emailIntent.putExtra(Intent.EXTRA_EMAIL, "appsupport@naismanila.edu.ph")
    //            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "The subject");
    //            emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body");
    //            emailIntent.putExtra(Intent.EXTRA_CC,"sabusanju2@gmail.com")
    //            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    //            var packageManager: PackageManager = v!!.context.packageManager
    //            v.context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:$deliveryAddress"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
            intent.putExtra(Intent.EXTRA_TEXT, "message")
            v.context.startActivity(intent)
    //            val activityList: List<ResolveInfo> = packageManager.queryIntentActivities(emailIntent, 0)
    //            for (app in activityList) {
    //                if (app.activityInfo.name.contains("com.google.android.gm")) {
    //                    val activity = app.activityInfo
    //                    val name = ComponentName(
    //                            activity.applicationInfo.packageName,
    //                            activity.name
    //                    )
    //                    emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    //                    emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
    //                            or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
    //                    emailIntent.component = name
    //                    v.context.startActivity(emailIntent)
    //                    break
    //                }
    //            }
        }
        needPasswordBtn.setOnClickListener {
            AppUtils.hideKeyboard(context)
            if (context.let { AppUtils.checkInternet(it) })
                forgotPasswordApiCall()
            else
                AppUtils.showDialogAlertDismiss(
                    context as Context, "Network Error", getString(
                        R.string.no_internet
                    ), R.drawable.nonetworkicon, R.drawable.roundred
                )
        }
    }

    private fun forgotPasswordApiCall() {
        val dialog = Dialog(context!!, R.style.NewDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_forgot_password)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        mailEdtText = dialog.findViewById<View>(R.id.text_dialog) as EditText
//        mMailEdtText!!.setOnTouchListener(this)
        val alertHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        val dialogSubmitButton = dialog.findViewById<View>(R.id.btn_signup) as Button
        dialogSubmitButton.setOnClickListener {
            AppUtils.hideKeyboard(context)
            if (!mailEdtText!!.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
                if (AppUtils.isValidEmail(mailEdtText!!.text.toString())) {
                    if (AppUtils.checkInternet(context!!))
                        sendForgotPassword()
                    else
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?, "Network Error", getString(
                                R.string.no_internet
                            ), R.drawable.nonetworkicon, R.drawable.roundred
                        )
                    dialog.dismiss()
                } else
                    AppUtils.showDialogAlertDismiss(
                        context as Activity?, getString(R.string.alert_heading), getString(
                            R.string.invalid_email
                        ), R.drawable.exclamationicon, R.drawable.round
                    )
            } else
                AppUtils.showDialogAlertDismiss(
                    context as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
        }
        val cancelButton = dialog.findViewById<View>(R.id.button2) as Button
        cancelButton.setOnClickListener {
            AppUtils.hideKeyboard(context)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendForgotPassword() {
        val call: Call<ResponseBody> = ApiClient.getApiService().forgotPassword(
            mailEdtText.text.toString()
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status: Int = jsonObject.optInt("status")
                        val pass: String = jsonObject.optString("pass")

                        if (status == 100) {
                            AppUtils.showDialogAlertDismiss(
                                context as Activity?, "Success", getString(
                                    R.string.frgot_success_alert
                                ), R.drawable.tick, R.drawable.round
                            )
                        } else if (status == 114) {
                            AppUtils.showDialogAlertDismiss(
                                context as Activity?, "Success",
                                "User not found in our database"
                                    , R.drawable.tick, R.drawable.round
                            )
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                context as Activity?, "Alert", context!!.getString(
                                    R.string.common_error
                                ), R.drawable.exclamationicon, R.drawable.round
                            )
                        }
                    }
                }
//                val responseData = response.body()
//                if (responseData != null) {
//                val responseString = response.body()!!.string()
//                val jsonObject = JSONObject(responseString)
//                val statusCode = jsonObject.getString("status")
//                if (statusCode.equals("100")) {
//                    val responseJSONObject = jsonObject.getJSONObject(JTAG_RESPONSE)
//                    val statusCode = responseJSONObject.getString(JTAG_STATUSCODE)
//                    if (statusCode.equals("303", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, "Success", getString(
//                                R.string.frgot_success_alert
//                            ), R.drawable.tick, R.drawable.round
//                        )
//                    } else if (statusCode.equals("301", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.missing_parameter
//                            ), R.drawable.infoicon, R.drawable.round
//                        )
//                    } else if (statusCode.equals("304", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.email_exists
//                            ), R.drawable.infoicon, R.drawable.round
//                        )
//                    } else if (statusCode.equals("305", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.incrct_usernamepswd
//                            ), R.drawable.exclamationicon, R.drawable.round
//                        )
//                    } else if (statusCode.equals("306", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.invalid_email
//                            ), R.drawable.exclamationicon, R.drawable.round
//                        )
//                    } else {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?, getString(R.string.error_heading), getString(
//                                R.string.common_error
//                            ), R.drawable.exclamationicon, R.drawable.round
//                        )
//                    }
//                } else if (statusCode.equals("500", ignoreCase = true)) {
//                    appUtils.showDialogAlertDismiss(
//                        mContext as Activity?, "Alert", mContext!!.getString(
//                            R.string.common_error
//                        ), R.drawable.exclamationicon, R.drawable.round
//                    )
//                } else if (statusCode.equals("400", ignoreCase = true)) {
//                    appUtils.getToken(mContext!!)
//                    sendForgotPassword()
//                } else if (statusCode.equals("401", ignoreCase = true)) {
//                    appUtils.getToken(mContext!!)
//                    sendForgotPassword()
//                } else if (statusCode.equals("402", ignoreCase = true)) {
//                    appUtils.getToken(mContext!!)
//                    sendForgotPassword()
//                } else
//                    appUtils.showDialogAlertDismiss(
//                        mContext as Activity?, "Alert", mContext!!.getString(
//                            R.string.common_error
//                        ), R.drawable.exclamationicon, R.drawable.round
//                    )
////                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

    private fun showSignUpAlertDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout_signup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        mailEdtText = dialog.findViewById<View>(R.id.text_dialog) as EditText
        val dialogSubmitButton = dialog.findViewById<View>(R.id.btn_signup) as Button
        dialogSubmitButton.setOnClickListener {
            AppUtils.hideKeyboard(context)
            if (!mailEdtText!!.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)) {
                if (AppUtils.isValidEmail(mailEdtText!!.text.toString())) {
                    if (AppUtils.checkInternet(context!!))
                        sendSignUpRequest()
                    else
                        AppUtils.showDialogAlertDismiss(
                            context as Activity?, "Network Error", getString(
                                R.string.no_internet
                            ), R.drawable.nonetworkicon, R.drawable.roundred
                        )
                }
                else
                    AppUtils.showDialogAlertDismiss(
                        context as Activity?, getString(R.string.alert_heading), getString(
                            R.string.enter_valid_email
                        ), R.drawable.exclamationicon, R.drawable.round
                    )
            } else {
                AppUtils.showDialogAlertDismiss(
                    context as Activity?, getString(R.string.alert_heading), getString(
                        R.string.enter_email
                    ), R.drawable.exclamationicon, R.drawable.round
                )
            }
        }

        val maybeLaterButton = dialog.findViewById<View>(R.id.button2) as Button
        maybeLaterButton.setOnClickListener {
            AppUtils.hideKeyboard(context)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun sendSignUpRequest() {
//        val call: Call<ResponseBody> = ApiClient.getApiService().signUp()
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
////                val responseData = response.body()
////                if (responseData != null)
//                    val responseString = response.body()!!.string()
//                    val jsonObject = JSONObject(responseString)
//                    val responseCode: String = jsonObject.getString(JTAG_RESPONSECODE)
//                    if (responseCode.equals("200", ignoreCase = true)) {
//                        val responseJSONObject: JSONObject = jsonObject.getJSONObject(JTAG_RESPONSE)
//                        val statusCode = responseJSONObject.getString(JTAG_STATUSCODE)
//                        if (statusCode.equals("303", ignoreCase = true)) {
//                            dialog.dismiss()
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                "Success",
//                                getString(R.string.signup_success_alert),
//                                R.drawable.tick,
//                                R.drawable.round
//                            )
//                        } else if (statusCode.equals("301", ignoreCase = true)) {
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                getString(R.string.error_heading),
//                                getString(R.string.missing_parameter),
//                                R.drawable.infoicon,
//                                R.drawable.round
//                            )
//                        } else if (statusCode.equals("304", ignoreCase = true)) {
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                getString(R.string.error_heading),
//                                getString(R.string.email_exists),
//                                R.drawable.infoicon,
//                                R.drawable.round
//                            )
//                        } else if (statusCode.equals("306", ignoreCase = true)) {
//                            appUtils.showDialogAlertDismiss(
//                                mContext as Activity?,
//                                getString(R.string.error_heading),
//                                getString(R.string.invalid_email_first) + mMailEdtText!!.text.toString() + getString(
//                                    R.string.invalid_email_last
//                                ),
//                                R.drawable.exclamationicon,
//                                R.drawable.round
//                            )
//                        }
//                    } else if (responseCode.equals("500", ignoreCase = true)) {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?,
//                            "Alert",
//                            mContext!!.getString(R.string.common_error),
//                            R.drawable.exclamationicon,
//                            R.drawable.round
//                        )
//                    } else if (responseCode.equals("400", ignoreCase = true)) {
//                        appUtils.getToken(mContext!!)
//                        sendSignUpRequest(URL_PARENT_SIGNUP)
//                    } else if (responseCode.equals("401", ignoreCase = true)) {
//                        appUtils.getToken(mContext!!)
//                        sendSignUpRequest(URL_PARENT_SIGNUP)
//                    } else if (responseCode.equals("402", ignoreCase = true)) {
//                        appUtils.getToken(mContext!!)
//                        sendSignUpRequest(URL_PARENT_SIGNUP)
//                    } else {
//                        appUtils.showDialogAlertDismiss(
//                            mContext as Activity?,
//                            "Alert",
//                            mContext!!.getString(R.string.common_error),
//                            R.drawable.exclamationicon,
//                            R.drawable.round
//                        )
//                    }
//
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            }
//
//        })
    }

    @SuppressLint("HardwareIds")
    private fun loginApiCall() {
        val androidID = getString(
            this.contentResolver,
            ANDROID_ID
        )
        val call: Call<ResponseBody> = ApiClient.getApiService().loginCall(
            userNameEdtTxt!!.text.toString(),
            passwordEdtTxt!!.text.toString(),
            "2",
//            FirebaseInstanceId.getInstance().id ,
            "0000000000",
            androidID
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            val responseArray: JSONObject = jsonObject.optJSONObject("responseArray")
                            val userCode = responseArray.optString("user_code")
                            val token = responseArray.optString("token")
                            PreferenceManager.setUserID(context, userCode)
                            PreferenceManager.setAccessToken(context, token)
                            showDialogSignUpAlert((context as Activity?)!!, "Success", "Successfully Logged In", R.drawable.tick, R.drawable.round)
                        } else if (status == 110) {
                            AppUtils.showDialogAlertDismiss(context as Activity?,
                                getString(R.string.error_heading),
                                "Incorrect Username or Password",
                                R.drawable.exclamationicon,
                                R.drawable.round)
                        } else if (status == 103) {
                            AppUtils.showDialogAlertDismiss(context as Activity?,
                                getString(R.string.error_heading),
                                "Validation Error",
                                R.drawable.exclamationicon,
                                R.drawable.round)
                        } else {
                            AppUtils.showDialogAlertDismiss(context as Activity?,
                                "Alert",
                                context!!.getString(R.string.common_error),
                                R.drawable.exclamationicon,
                                R.drawable.round)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }

    private fun showDialogSignUpAlert(activity: Activity, messageHead: String, message: String, ico: Int, bgIcon: Int) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as? TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as? TextView
        if (text != null) {
            text.text = message
        }
        if (textHead != null) {
            textHead.text = messageHead
        }

        val dialogButton = dialog.findViewById<View>(R.id.btnOK) as Button
        dialogButton.setOnClickListener {
            dialog.dismiss()
            val homeIntent = Intent(context, HomeListActivity::class.java)
            startActivity(homeIntent)
            finish()
        }

        dialog.show()
    }


}