package com.mobatia.nasmanila.fragments.nas_today

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.nas_today.model.NasTodayModel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NasTodayFragment(nasToday: String, tabNasToday: String) : Fragment() {
    var mTitleTextView: TextView? = null
    private var mRootView: View? = null
    private var mContext: Context? = null
    private var mNasTodayListView: ListView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    private var relMain: RelativeLayout? = null
    private val mBannerImage: ImageView? = null
    private val mListViewArray: ArrayList<NasTodayModel>? = null
    var bannerImagePager: ImageView? = null

    var bannerUrlImageArray: ArrayList<String>? = null
    var myFormatCalender = "yyyy-MM-dd"
    var sdfcalender: SimpleDateFormat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_nas_today, container,
            false
        )

        mContext = activity
        myFormatCalender = "yyyy-MM-dd hh:mm:ss"
        sdfcalender = SimpleDateFormat(myFormatCalender, Locale.ENGLISH)
        initialiseUI()
        return mRootView

    }

    private fun initialiseUI() {
        mTitleTextView = mRootView!!.findViewById<View>(R.id.titleTextView) as TextView
        mNasTodayListView = mRootView!!.findViewById<View>(R.id.mNasTodayListView) as ListView
        mTitleTextView!!.setText(NaisClassNameConstants.NAS_TODAY)
        bannerImagePager = mRootView!!.findViewById<View>(R.id.bannerImageViewPager) as ImageView
        relMain = mRootView!!.findViewById<View>(R.id.relMain) as RelativeLayout
        relMain!!.setOnClickListener(View.OnClickListener { })
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
    }

    private fun getList() {
        val call: Call<ResponseBody> = ApiClient.getApiService().nasTodayListCall()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getSearchValues(dataObject: JSONObject?): NasTodayModel {
        val model = NasTodayModel()
//        model.id = (dataObject!!.getString(JSONConstants.JTAG_ID))
//        model.image = (dataObject.getString(JSONConstants.JTAG_IMAGE))
//        model.title = (dataObject.getString(JSONConstants.JTAG_TITLE))
//        model.description = (dataObject.getString(JSONConstants.JTAG_DESCRIPTION))
//        model.date = (dataObject.getString(JSONConstants.JTAG_DATE))
//        model.pdf = (dataObject.getString(JSONConstants.JTAG_PDF))
//        var mNoticeDate: Date? = Date()
//        val mDate: String = dataObject.optString(JSONConstants.JTAG_DATE)
//        try {
//            mNoticeDate = sdfcalender!!.parse(mDate)
//        } catch (ex: ParseException) {
//            Log.e("Date", "Parsing error")
//        }
//        val mDateTime: String = dataObject.getString(JSONConstants.JTAG_DATE)
//        var mTime: Date? = Date()
//
//        try {
//            mTime = sdfcalender!!.parse(mDateTime)
//            val format2 = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
//            val startTime = format2.format(mTime)
//            model.time = (startTime)
//        } catch (ex: ParseException) {
//            Log.e("Date", "Parsing error")
//        }
//
//        val day = DateFormat.format("dd", mNoticeDate) as String
//
//        val monthString = DateFormat.format("MMM", mNoticeDate) as String
//
//        val year = DateFormat.format("yyyy", mNoticeDate) as String
//
//        model.day = (day)
//        model.month = (monthString.toUpperCase())
//        model.year = (year)
        return model
    }
}