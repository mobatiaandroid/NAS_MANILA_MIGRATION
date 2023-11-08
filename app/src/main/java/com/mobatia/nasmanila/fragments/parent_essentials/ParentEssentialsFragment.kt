package com.mobatia.nasmanila.fragments.parent_essentials

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.parent_essential.ParentEssentialActivity
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.*
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.parent_essentials.model.ParentEssentialsModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ParentEssentialsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParentEssentialsFragment(title: String, tabId: String) : Fragment() {

    var mTitleTextView: TextView? = null
    private var mRootView: View? = null
    private var mContext: Context? = null
    private var mListView: RecyclerView? = null
    private val mTitle: String? = title
    private val mTabId: String? = tabId
    var bannerImagePager: ImageView? = null
    private val mBannerImage: ImageView? = null
    var bannerUrlImageArray: ArrayList<String>? = null
    private val newsLetterModelArrayList = ArrayList<ParentEssentialsModel>()
    var relMain: RelativeLayout? = null
    var mtitle: RelativeLayout? = null
    var text_content: TextView? = null
    var text_dialog: TextView? = null
    var description = ""
    var contactEmail = ""
    var descriptionTV: TextView? = null
    var descriptionTitle: TextView? = null
    var sendEmail: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        mRootView =
            inflater.inflate(
                R.layout.fragment_parent_essentials, container,
                false
            )
        mContext = activity
        initialiseUI()
        return mRootView
        return inflater.inflate(R.layout.fragment_parent_essentials, container, false)
    }

    private fun initialiseUI() {
        mtitle = mRootView!!.findViewById<View>(R.id.title) as RelativeLayout
        descriptionTV = mRootView!!.findViewById<View>(R.id.descriptionTV) as TextView
        descriptionTitle = mRootView!!.findViewById<View>(R.id.descriptionTitle) as TextView
        sendEmail = mRootView!!.findViewById<View>(R.id.sendEmail) as ImageView
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView!!.text = NaisClassNameConstants.PARENT_ESSENTIALS
        mListView = mRootView!!.findViewById<View>(R.id.mListView) as RecyclerView
        mListView!!.isNestedScrollingEnabled = false
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImagePager) as ImageView
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener {

        }
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        mListView!!.addItemDecoration(DividerItemDecoration(resources.getDrawable(R.drawable.list_divider)))

        mListView!!.layoutManager = llm
        if (AppUtils.checkInternet(mContext!!)) {
            getNewslettercategory()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        mListView!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (newsLetterModelArrayList[position].title.equals(
                        "Bus Service",
                        ignoreCase = true
                    ) || newsLetterModelArrayList[position].title.equals(
                        "Lunch Menu",
                        ignoreCase = true
                    )
                ) {
                    val intent = Intent(mContext, ParentEssentialActivity::class.java)
                    intent.putExtra(
                        "submenuArray",
                        newsLetterModelArrayList[position].newsLetterModelArrayList
                    )
                    intent.putExtra("tab_type", NaisClassNameConstants.PARENT_ESSENTIALS)
                    intent.putExtra("tab_typeName", newsLetterModelArrayList[position].title)
                    intent.putExtra("bannerImage", newsLetterModelArrayList[position].bannerImage)
                    intent.putExtra("contactEmail", newsLetterModelArrayList[position].contactEmail)
                    intent.putExtra("description", newsLetterModelArrayList[position].description)
                    mContext!!.startActivity(intent)
                } else if (newsLetterModelArrayList[position].title.equals(
                        "Parent Portal",
                        ignoreCase = true
                    )
                ) {
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                    intent.putExtra("tab_type", NaisClassNameConstants.PARENT_ESSENTIALS)
                    intent.putExtra("url", newsLetterModelArrayList[position].link)
                    (mContext as Activity).startActivity(intent)
                } else {
                    val intent = Intent(mContext, ParentEssentialActivity::class.java)
                    intent.putExtra(
                        "submenuArray",
                        newsLetterModelArrayList[position].newsLetterModelArrayList
                    )
                    intent.putExtra("tab_type", NaisClassNameConstants.PARENT_ESSENTIALS)
                    intent.putExtra("tab_typeName", newsLetterModelArrayList[position].title)
                    intent.putExtra("bannerImage", newsLetterModelArrayList[position].bannerImage)
                    (mContext as Activity).startActivity(intent)
                }
            }

        })
        sendEmail!!.setOnClickListener {
            if (!PreferenceManager.getUserCode(mContext as Activity).equals("", ignoreCase = true)) {
                val dialog = Dialog(mContext as Activity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.alert_send_email_dialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val dialogCancelButton = dialog.findViewById<View>(R.id.cancelButton) as Button
                val submitButton = dialog.findViewById<View>(R.id.submitButton) as Button
                text_dialog = dialog.findViewById<View>(R.id.text_dialog) as EditText
                text_content = dialog.findViewById<View>(R.id.text_content) as EditText
                (text_dialog as EditText).setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        (text_dialog as EditText).hint = ""
                        (text_dialog as EditText).gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                        (text_dialog as EditText).setPadding(5, 5, 0, 0)
                    } else {
                        (text_dialog as EditText).hint = "Enter your subject here..."
                        (text_dialog as EditText).gravity = Gravity.CENTER
                    }
                })
                (text_content as EditText).onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            (text_content as EditText).gravity = Gravity.LEFT
                        } else {
                            (text_content as EditText).gravity = Gravity.CENTER
                        }
                    }
                dialogCancelButton.setOnClickListener { dialog.dismiss() }
                submitButton.setOnClickListener {
                    if ((text_dialog as EditText).text.equals("")) {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            (mContext as Activity).getString(R.string.alert_heading),
                            "Please enter  subject",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if ((text_content as EditText).text.toString() == "") {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            (mContext as Activity).getString(R.string.alert_heading),
                            "Please enter  content",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        if (AppUtils.checkInternet(mContext as Activity)) {
                            sendEmailToStaff()
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Network Error",
                                (mContext as Activity).getString(R.string.no_internet),
                                R.drawable.nonetworkicon,
                                R.drawable.roundred
                            )
                        }
                    }
                }
                dialog.show()
            } else {
                AppUtils.showDialogAlertDismiss(
                    mContext as Activity?,
                    (mContext as Activity).getString(R.string.alert_heading),
                    "This feature is available only for registered users. Login/register to see contents.",
                    R.drawable.exclamationicon,
                    R.drawable.round
                )
            }
        }
    }

    private fun getNewslettercategory() {
        val call: Call<ResponseBody> = ApiClient.getApiService().newsLetterCategoryCall()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addNewsLetterDetails(dataObject: JSONObject?): ParentEssentialsModel {
        val model = ParentEssentialsModel()
//        model.title = dataObject!!.optString(JSONConstants.JTAG_TAB_NAME)
//        if (dataObject.has(JSONConstants.JTAG_BANNER_IMAGE)) {
//            model.bannerImage = dataObject.optString(JSONConstants.JTAG_BANNER_IMAGE)
//        } else {
//            model.bannerImage = ""
//        }
//        if (dataObject.has(JSONConstants.JTAG_DESCRIPTION)) {
//            model.description = dataObject.optString(JSONConstants.JTAG_DESCRIPTION)
//        } else {
//            model.description = ""
//        }
//        if (dataObject.has(JSONConstants.JTAG_CONTACT_EMAIL)) {
//            model.contactEmail = dataObject.optString(JSONConstants.JTAG_CONTACT_EMAIL)
//        } else {
//            model.contactEmail = ""
//        }
//        if (dataObject.has("link")) {
//            model.link = dataObject.optString("link")
//        } else {
//            model.link = ""
//        }
//        val submenuArray: JSONArray = dataObject.getJSONArray(JSONConstants.JTAG_TAB_SUBMENU_ARRAY)
//        val subMenNewsLetterModels = ArrayList<ParentEssentialsModel>()
//        for (i in 0 until submenuArray.length()) {
//            val subObj = submenuArray.getJSONObject(i)
//            val newsModel = ParentEssentialsModel()
//            newsModel.filename = subObj.optString("filename")
//            newsModel.submenu = subObj.optString("submenu")
//            subMenNewsLetterModels.add(newsModel)
//        }
//        model.newsLetterModelArrayList = subMenNewsLetterModels
        return model
    }

    private fun sendEmailToStaff() {
        val call: Call<ResponseBody> = ApiClient.getApiService().sendEmailToStaffCall(
            contactEmail!!,
            text_dialog!!.text.toString(),
            text_content!!.text.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Success",
                                "Successfully sent email to staff",
                                R.drawable.tick,
                                R.drawable.round
                            )
                        } else {
                            val toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                } else {
                    AppUtils.showDialogAlertDismiss(
                        mContext as Activity?,
                        "Alert",
                        (mContext as Activity).getString(R.string.common_error),
                        R.drawable.exclamationicon,
                        R.drawable.round
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}