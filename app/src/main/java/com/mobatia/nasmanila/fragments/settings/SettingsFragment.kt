package com.mobatia.nasmanila.fragments.settings

import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.login.LoginActivity
import com.mobatia.nasmanila.activities.terms_of_service.TermsOfServiceActivity
import com.mobatia.nasmanila.activities.tutorial.TutorialActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.settings.adapter.CustomSettingsAdapter
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class SettingsFragment(settings: Any, tabSettings: String) : Fragment() {

    private var mRootView: View? = null
    private var mContext: Context? = null
    private var mSettingsList: ListView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private var relMain: RelativeLayout? = null
    private val mBannerImage: ImageView? = null
    private var versionText: TextView? = null
    var mTitleTextView: TextView? = null
    var text_currentpswd: EditText? = null
    var newpassword: EditText? = null
    var confirmpassword: EditText? = null
    var isRegUser = false
    var dialog: Dialog? = null
    var mSettingsListArray: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add("Change App Settings")
            add("Terms of Service")
            add("Email Us")
            add("Tutorial")
            add("Logout")
        }
    }
    var mSettingsListArrayRegistered: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add("Change App Settings")
            add("Terms of Service")
            add("Email Us")
            add("Tutorial")
            add("Change Password")
            add("Logout")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_settings_list, container,
            false
        )
        mContext = activity
        dialog = Dialog(mContext!!, R.style.NewDialog)
        initialiseUI()
        return mRootView
    }

    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        versionText = mRootView!!.findViewById<View>(R.id.versionText) as TextView
        mSettingsList = mRootView!!.findViewById<View>(R.id.mSettingsListView) as ListView
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener { }
        mTitleTextView!!.text = NaisClassNameConstants.SETTINGS
        if (PreferenceManager.getUserCode(mContext!!) == "") {
            isRegUser = false
            mSettingsList!!.adapter = CustomSettingsAdapter(requireActivity(), mSettingsListArray)
        } else {
            isRegUser = true
            mSettingsList!!.adapter = CustomSettingsAdapter(requireActivity(), mSettingsListArrayRegistered)
        }
        mSettingsList!!.setOnItemClickListener { parent, view, position, id ->
            if (isRegUser) {
                when(position) {
                    0 -> {
                        PreferenceManager.setGoToSettings(mContext!!, "1")

                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        mContext!!.startActivity(intent)
                    }
                    1 -> {
                        val mIntent = Intent(mContext, TermsOfServiceActivity::class.java)
                        mIntent.putExtra("tab_type", mSettingsListArray[position].toString())
                        mContext!!.startActivity(mIntent)
                    }
                    2 -> {
                        val to = "appsupport@naismanila.edu.ph"
                        val email = Intent(Intent.ACTION_SEND)
                        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
                        email.type = "message/rfc822"
                        startActivity(Intent.createChooser(email, "Choose an Email client :"))
                    }
                    3 -> {
                        val mIntent = Intent(mContext, TutorialActivity::class.java)
                        mIntent.putExtra("type", 0)
                        mContext!!.startActivity(mIntent)
                    }
                    4 -> {
                        if (AppUtils.checkInternet(mContext!!)) {
                            showChangePasswordAlert()
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Network Error",
                                getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }
                    5 -> {
                        if (AppUtils.checkInternet(mContext!!)) {
                            AppUtils.showDialogAlertLogout(
                                activity,
                                "Confirm?",
                                "Do you want to Logout?",
                                R.drawable.questionmark_icon,
                                R.drawable.round
                            )
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Network Error",
                                getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }
                }
            } else {
                when (position) {
                    0 -> {
                        PreferenceManager.setGoToSettings(mContext!!, "1")
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        mContext!!.startActivity(intent)
                    }
                    1 -> {
                        val mIntent = Intent(mContext, TermsOfServiceActivity::class.java)
                        mIntent.putExtra("tab_type", mSettingsListArray[position].toString())
                        mContext!!.startActivity(mIntent)
                    }
                    2 -> {
                        val emailIntent = Intent(
                            Intent.ACTION_SEND_MULTIPLE
                        )
                        val deliveryAddress = arrayOf("appsupport@naismanila.edu.ph")
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                        emailIntent.type = "text/plain"
                        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        val pm = mContext!!.packageManager
                        val activityList = pm.queryIntentActivities(
                            emailIntent, 0
                        )
                        println("packge size" + activityList.size)
                        for (app in activityList) {
                            println("packge name" + app.activityInfo.name)
                            if (app.activityInfo.name.contains("com.google.android.gm")) {
                                val activity = app.activityInfo
                                val name = ComponentName(
                                    activity.applicationInfo.packageName, activity.name
                                )
                                emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                        or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                emailIntent.component = name
                                mContext!!.startActivity(emailIntent)
                                break
                            }
                        }
                    }
                    3 -> {
                        val mIntent = Intent(mContext, TutorialActivity::class.java)
                        mIntent.putExtra("type", 0)

                        mContext!!.startActivity(mIntent)
                    }
                    4 -> {
                        AppUtils.showDialogAlertLogout(
                            activity,
                            "Confirm?",
                            "Do you want to Logout?",
                            R.drawable.questionmark_icon,
                            R.drawable.round
                        )
                    }

                }
            }
        }
    }

    private fun showChangePasswordAlert() {
        dialog = Dialog(mContext!!, R.style.NewDialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.custom_dialog_changepassword)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setCancelable(true)
        val sdk = Build.VERSION.SDK_INT
        text_currentpswd = dialog!!.findViewById<View>(R.id.text_currentpassword) as EditText
        newpassword = dialog!!.findViewById<View>(R.id.text_currentnewpassword)  as EditText
        confirmpassword = dialog!!.findViewById<View>(R.id.text_confirmpassword)  as EditText

        val dialogSubmitButton = dialog!!.findViewById<View>(R.id.btn_changepassword) as Button
        dialogSubmitButton.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
//            appUtils.hideKeyboard(mContext, text_currentpswd)
//            appUtils.hideKeyboard(mContext, confirmpassword)
            if (text_currentpswd!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_current_password),
                    R.drawable.infoicon,
                    R.drawable.round
                )
            } else if (newpassword!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_new_password),
                    R.drawable.infoicon,
                    R.drawable.round
                )

            } else if (confirmpassword!!.text.toString().trim { it <= ' ' }.isEmpty()) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.enter_confirm_password),
                    R.drawable.infoicon,
                    R.drawable.round
                )

            } else if (newpassword!!.text.toString()
                    .trim { it <= ' ' } != confirmpassword!!.text.toString()
                    .trim { it <= ' ' }
            ) {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    getString(R.string.alert_heading),
                    getString(R.string.password_mismatch),
                    R.drawable.infoicon,
                    R.drawable.round
                )
            } else {
                if (AppUtils.checkInternet(mContext!!)) {
                    callChangePasswordAPI()
                } else {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Network Error",
                        getString(R.string.no_internet),
                        R.drawable.nonetworkicon,
                        R.drawable.roundred
                    )
                }
            }
        }

        val dialogCancel = dialog!!.findViewById<View>(R.id.btn_cancel) as Button
        dialogCancel.setOnClickListener {
            AppUtils.hideKeyboard(mContext)
//            appUtils.hideKeyboard(mContext, text_currentpswd)
//            appUtils.hideKeyboard(mContext, confirmpassword)
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    private fun callChangePasswordAPI() {
//        val call: Call<ResponseBody> = ApiClient.getApiService().changePassword(
//
//        )
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//
//            }
//
//        })
    }

    private fun showDialogSignUpAlert(
        activity: Activity?,
        msgHead: String,
        msg: String,
        ico: Int,
        bgIcon: Int
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        val icon = dialog.findViewById<View>(R.id.iconImageView) as ImageView
        icon.setBackgroundResource(bgIcon)
        icon.setImageResource(ico)
        val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
        val textHead = dialog.findViewById<View>(R.id.alertHead) as TextView
        text.text = msg
        textHead.text = msgHead

        val dialogButton = dialog.findViewById<View>(R.id.btn_Ok) as Button
        dialogButton.setOnClickListener {
            PreferenceManager.setUserCode(activity)
            dialog.dismiss()
            val mIntent = Intent(activity, LoginActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(mIntent)
        }
        dialog.show()
    }
}