package com.mobatia.nasmanila.fragments.about_us

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
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsModel
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import com.mobatia.nasmanila.fragments.about_us.adapter.CustomAboutUsAdapter
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Use the [AboutUsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutUsFragment(tabTitle: String, tabId: String) : Fragment() {

    private var mRootView: View? = null
    private var mContext: Context? = null
    private var mAboutUsList: RecyclerView? = null
    private val mTitle: String? = tabTitle
    private val mTabId: String? = tabId
    var email_nas: String? = null
    var weburlString: String? = null
    var descriptionString: String? = null
    private var relMain: RelativeLayout? = null
    private var mtitleRel: RelativeLayout? = null
    private val mBannerImage: ImageView? = null
    private var sendEmail: ImageView? = null
    var mTitleTextView: TextView? = null
    var weburl: TextView? = null
    var description: TextView? = null
    var descriptionTitle: TextView? = null
    private var mAboutUsListArray: ArrayList<AboutUsModel>? = null
    private var mAboutUsModelListArray: ArrayList<AboutUsModel>? = null
    var dialog: Dialog? = null
    var text_dialog: EditText? = null
    var text_content: EditText? = null
    var scroolView: ScrollView? = null
    var bannerImagePager: ImageView? = null
    var bannerUrlImageArray: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_about_us, container,
            false
        )
        setHasOptionsMenu(true)
        mContext = activity
        initialiseUI()
        if (AppUtils.checkInternet(mContext!!)) {
            getList()
        } else {
            AppUtils.showDialogAlertDismiss(
                mContext as Activity?,
                "Network Error",
                getString(R.string.no_internet),
                R.drawable.nonetworkicon,
                R.drawable.roundred
            )
        }
        return mRootView
    }

    private fun getList() {
        var mAboutUsListArray: ArrayList<AboutUsModel>? = null
        val call: Call<ResponseBody> = ApiClient.getApiService().aboutUsListCall()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            val responseArray: JSONObject = jsonObject.optJSONObject("responseArray")
                            val banner: String = responseArray.optString("banner_images")
                            if (banner != "") {
                                Glide.with(mContext!!).load(AppUtils.replace(banner)).centerCrop()
                                    .into(mBannerImage!!)
                            } else {
                                mBannerImage!!.setBackgroundResource(R.drawable.default_bannerr)
                            }
                            val dataArray: JSONArray =
                                responseArray.getJSONArray("data")
                            val model = AboutUsModel()
                            model.description = (responseArray.optString("description"))
                            model.webUrl = (responseArray.optString("website_link"))
                            model.email = (responseArray.optString("contact_email"))
                            email_nas = responseArray.optString("contact_email")
                            description!!.text = responseArray.optString("description")
                            weburl!!.text = responseArray.optString("website_link")
                            weburlString = responseArray.optString("website_link")
                            descriptionString = responseArray.optString("description")
                            if (descriptionString.equals("", ignoreCase = true) && email_nas.equals(
                                    "",
                                    ignoreCase = true
                                )
                            ) {
                                mtitleRel!!.visibility = View.GONE
                            } else {
                                mtitleRel!!.visibility = View.VISIBLE
                            }
                            if (descriptionString.equals("", ignoreCase = true)) {
                                description!!.visibility = View.GONE
                                descriptionTitle!!.visibility = View.GONE
                            } else {
                                description!!.text = descriptionString
                                descriptionTitle!!.visibility = View.GONE
                                description!!.visibility = View.VISIBLE
                                mtitleRel!!.visibility = View.VISIBLE
                            }
                            if (email_nas.equals("", ignoreCase = true)) {
                                sendEmail!!.visibility = View.GONE
                            } else {
                                mtitleRel!!.visibility = View.VISIBLE
                                sendEmail!!.visibility = View.VISIBLE
                            }
                            if (weburlString.equals("", ignoreCase = true)) {
                                weburl!!.visibility = View.GONE
                            } else {
                                weburl!!.visibility = View.VISIBLE
                            }
                            if (dataArray.length() > 0) {
                                for (i in 0 until dataArray.length()) {
                                    if (i >= 0) {
                                        val dataObject = dataArray.getJSONObject(i)
                                        mAboutUsListArray!!.add(getSearchValues(dataObject))
                                    }
                                }
                                mAboutUsList!!.adapter =
                                    CustomAboutUsAdapter(activity, mAboutUsListArray)
                            } else {
                                AppUtils.showDialogAlertDismiss(
                                    mContext as Activity?,
                                    "Alert",
                                    getString(R.string.nodatafound),
                                    R.drawable.exclamationicon,
                                    R.drawable.round
                                )
                            }
                        } else {
                            AppUtils.showDialogAlertDismiss(
                                mContext as Activity?,
                                "Alert",
                                getString(R.string.common_error),
                                R.drawable.exclamationicon,
                                R.drawable.round
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun getSearchValues(dataObject: JSONObject?): AboutUsModel {
        val mAboutUsModel = AboutUsModel()
        mAboutUsModel.Id = (dataObject!!.getString("id"))
        mAboutUsModel.webUrl = (dataObject.getString("url"))
        mAboutUsModel.TabType = (dataObject.getString("tab_type"))
        mAboutUsModel.itemDesc = (dataObject.getString("description"))
        mAboutUsModel.itemImageUrl = (dataObject.getString("banner_image"))
        val itemArray: JSONArray = dataObject.getJSONArray("items")
        if (itemArray.length() > 0) {
            mAboutUsModelListArray = ArrayList()
            for (i in 0 until itemArray.length()) {
                val model = AboutUsModel()
                model.itemPdfUrl = (itemArray.getJSONObject(i).getString("url"))
                model.itemTitle = (itemArray.getJSONObject(i).getString("title"))
                mAboutUsModelListArray!!.add(model)
            }
            mAboutUsModel.aboutusModelArrayList = (mAboutUsModelListArray)
        }
        return mAboutUsModel
    }

    private fun sendEmailToStaff() {
        val call: Call<ResponseBody> = ApiClient.getApiService().sendEmailToStaffCall(
            email_nas!!,
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
                            dialog!!.dismiss()
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

    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mtitleRel = mRootView!!.findViewById<View>(R.id.title) as RelativeLayout
        mAboutUsList = mRootView!!.findViewById<View>(R.id.mAboutUsListView) as RecyclerView
        description = mRootView!!.findViewById<View>(R.id.description) as TextView
        descriptionTitle = mRootView!!.findViewById<View>(R.id.descriptionTitle) as TextView
        weburl = mRootView!!.findViewById<View>(R.id.weburl) as TextView
        sendEmail = mRootView!!.findViewById<View>(R.id.sendEmail) as ImageView
        scroolView = mRootView!!.findViewById<View>(R.id.scrollView) as ScrollView
        scroolView!!.fullScroll(View.FOCUS_DOWN)
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener { }
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImagePager) as ImageView
        mTitleTextView!!.text = NaisClassNameConstants.ABOUT_US
        mAboutUsList!!.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        mAboutUsList!!.layoutManager = llm
        val itemDecoration = ItemOffsetDecoration(mContext!!, 5)
        mAboutUsList!!.addItemDecoration(itemDecoration)
        mAboutUsList!!.addItemDecoration(
            DividerItemDecoration(mContext!!.resources.getDrawable(R.drawable.list_divider))
        )
        weburl!!.setOnClickListener {
            val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
            intent.putExtra("url", weburlString)
            intent.putExtra("tab_type", NaisClassNameConstants.ABOUT_US)
            mContext!!.startActivity(intent)
        }
        sendEmail!!.setOnClickListener {
            if (PreferenceManager.getUserCode(mContext!!) != "") {
                dialog = Dialog(mContext!!)
                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog!!.setContentView(R.layout.alert_send_email_dialog)
                dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                val dialogCancelButton =
                    dialog!!.findViewById<View>(R.id.cancelButton) as Button
                val submitButton =
                    dialog!!.findViewById<View>(R.id.submitButton) as Button
                text_dialog = dialog!!.findViewById<View>(R.id.text_dialog) as EditText
                text_content =
                    dialog!!.findViewById<View>(R.id.text_content) as EditText
                text_dialog!!.onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            text_dialog!!.hint = ""
                            text_dialog!!.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                            text_dialog!!.setPadding(5, 5, 0, 0)
                        } else {
                            text_dialog!!.hint = "Enter your subject here..."
                            text_dialog!!.gravity = Gravity.CENTER
                        }
                    }
                text_content!!.onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            text_content!!.gravity = Gravity.LEFT
                        } else {
                            text_content!!.gravity = Gravity.CENTER
                        }
                    }
                dialogCancelButton.setOnClickListener { dialog!!.dismiss() }
                submitButton.setOnClickListener {
                    if (text_dialog!!.text.toString() == "") {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            (mContext as Activity).getString(R.string.alert_heading),
                            "Please enter subject",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else if (text_content!!.text.toString() == "") {
                        AppUtils.showDialogAlertDismiss(
                            mContext as Activity?,
                            (mContext as Activity).getString(R.string.alert_heading),
                            "Please enter content",
                            R.drawable.exclamationicon,
                            R.drawable.round
                        )
                    } else {
                        if (AppUtils.checkInternet(mContext!!)) {
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
                dialog!!.show()
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
        mAboutUsList!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (mAboutUsListArray!![position].TabType.equals("Facilities")) {
                    val mIntent = Intent(activity, FacilityActivity::class.java)
                    mIntent.putExtra(
                        "array",
                        mAboutUsListArray!![position].aboutusModelArrayList
                    )
                    mIntent.putExtra("desc", mAboutUsListArray!![position].itemDesc)
                    mIntent.putExtra("title", mAboutUsListArray!![position].TabType)
                    mIntent.putExtra("banner_image", mAboutUsListArray!![position].itemImageUrl)
                    mContext!!.startActivity(mIntent)
                    System.out.println(
                        "faci array--" + mAboutUsListArray!![position].aboutusModelArrayList!!.size
                    )
                } else if (mAboutUsListArray!![position].TabType
                        .equals("Accreditations & Examinations")
                ) {
                    val mIntent = Intent(mContext, AccreditationsActivity::class.java)
                    mIntent.putExtra(
                        "array",
                        mAboutUsListArray!![position].aboutusModelArrayList
                    )
                    mIntent.putExtra("desc", mAboutUsListArray!![position].itemDesc)
                    mIntent.putExtra("title", mAboutUsListArray!![position].TabType)
                    mIntent.putExtra("banner_image", mAboutUsListArray!![position].itemImageUrl)
                    mContext!!.startActivity(mIntent)
                } else if (mAboutUsListArray!![position].TabType.equals("Admissions")) {
                    val mIntent = Intent(mContext, AccreditationsActivity::class.java)
                    mIntent.putExtra(
                        "array",
                        mAboutUsListArray!![position].aboutusModelArrayList
                    )
                    mIntent.putExtra("desc", mAboutUsListArray!![position].itemDesc)
                    mIntent.putExtra("title", mAboutUsListArray!![position].TabType)
                    mIntent.putExtra("banner_image", mAboutUsListArray!![position].itemImageUrl)
                    mContext!!.startActivity(mIntent)
                } else if (mAboutUsListArray!![position].TabType.equals("Admissions")) {
                    val mIntent = Intent(mContext, AccreditationsActivity::class.java)
                    mIntent.putExtra(
                        "array",
                        mAboutUsListArray!![position].aboutusModelArrayList
                    )
                    mIntent.putExtra("desc", mAboutUsListArray!![position].itemDesc)
                    mIntent.putExtra("title", mAboutUsListArray!![position].TabType)
                    mIntent.putExtra("banner_image", mAboutUsListArray!![position].itemImageUrl)
                    mContext!!.startActivity(mIntent)
                } else {
                    val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
                    intent.putExtra("url", mAboutUsListArray!![position].webUrl)
                    intent.putExtra("tab_type", NaisClassNameConstants.ABOUT_US)
                    mContext!!.startActivity(intent)
                }
            }
        })
//        fun onItemClick(
//            parent: AdapterView<*>?, view: View?, position: Int,
//            id: Long
//        ) {
////		if (position==0)
////		{
////			Intent mIntent=new Intent(getActivity(),StaffDirectoryActivity.class);
////			mContext.startActivity(mIntent);
////
////		}else
//            if (mAboutUsListArray!![position].getTabType().equals("Facilities")) {
//                val mIntent = Intent(activity, FacilityActivity::class.java)
//                mIntent.putExtra("array", mAboutUsListArray[position].getAboutusModelArrayList())
//                mIntent.putExtra("desc", mAboutUsListArray[position].getItemDesc())
//                mIntent.putExtra("title", mAboutUsListArray[position].getTabType())
//                mIntent.putExtra("banner_image", mAboutUsListArray[position].getImageUrl())
//                mContext!!.startActivity(mIntent)
//                System.out.println(
//                    "faci array--" + mAboutUsListArray[position].getAboutusModelArrayList().size()
//                )
//            } else if (mAboutUsListArray[position].getTabType()
//                    .equals("Accreditations & Examinations")
//            ) {
//                val mIntent = Intent(mContext, AccreditationsActivity::class.java)
//                mIntent.putExtra("array", mAboutUsListArray[position].getAboutusModelArrayList())
//                mIntent.putExtra("desc", mAboutUsListArray[position].getItemDesc())
//                mIntent.putExtra("title", mAboutUsListArray[position].getTabType())
//                mIntent.putExtra("banner_image", mAboutUsListArray[position].getImageUrl())
//                mContext!!.startActivity(mIntent)
//            } else {
//                val intent = Intent(mContext, LoadUrlWebViewActivity::class.java)
//                intent.putExtra("url", mAboutUsListArray[position].getUrl())
//                intent.putExtra("tab_type", mAboutUsListArray[position].getTabType())
//                mContext!!.startActivity(intent)
//            }
//        }
    }
}