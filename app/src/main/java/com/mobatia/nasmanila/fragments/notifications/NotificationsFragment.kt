package com.mobatia.nasmanila.fragments.notifications

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.notification.AudioAlertActivity
import com.mobatia.nasmanila.activities.notification.ImageAlertActivity
import com.mobatia.nasmanila.activities.notification.TextAlertActivity
import com.mobatia.nasmanila.activities.notification.VideoAlertActivity
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.notifications.adapter.PushNotificationListAdapter
import com.mobatia.nasmanila.fragments.notifications.model.PushNotificationModel
import com.mobatia.nasmanila.common.common_classes.DividerItemDecoration
import com.mobatia.nasmanila.manager.recyclermanager.ItemOffsetDecoration
import com.mobatia.nasmanila.common.common_classes.OnItemClickListener
import com.mobatia.nasmanila.common.common_classes.addOnItemClickListener
import me.leolin.shortcutbadger.ShortcutBadger
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationsFragment(tabTitle: String, tabId: String) : Fragment() {

    lateinit var mRootView: View
    lateinit var mContext: Context
    lateinit var notificationRecycler: RecyclerView
    lateinit var swipeNotification: SwipeRefreshLayout
    private val mTitle: String = tabTitle
    private val mTabId: String = tabId
    private lateinit var constraintMain: ConstraintLayout
    private lateinit var mBannerImage: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var mTitleTextView: TextView
    lateinit var pushNotificationArrayList: ArrayList<PushNotificationModel>
    lateinit var mPushNotificationListAdapter: PushNotificationListAdapter
    var mIntent: Intent? = null
    var myFormatCalender = "yyyy-MM-dd HH:mm:ss"
    var sdfcalendar: SimpleDateFormat? = null
    var isFromBottom = false
    var swipeRefresh = false
    var pageFrom = ""
    var scrollTo = ""
    var notificationSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_notifications, container,
            false
        )

        mContext = requireActivity()
        myFormatCalender = "yyyy-MM-dd HH:mm:ss"
        sdfcalendar = SimpleDateFormat(myFormatCalender, Locale.ENGLISH)
        ShortcutBadger.applyCount(mContext, 0) //badge

        initialiseUI()
        if (AppUtils.checkInternet(mContext!!)) {
//            clearBadge()
            val isFromBottom = false
            callPushNotification(
                pageFrom,
                scrollTo
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
        return mRootView
    }

    private fun callPushNotification(
        start: String,
        limit: String
    ) {
        val call: Call<ResponseBody> = ApiClient.getApiService().pushNotificationsCall(
            0,15
        )
        progressBar.visibility = View.VISIBLE
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
                            val dataArray: JSONArray = responseArray.optJSONArray("notifications")
                            if (dataArray.length() > 0) {
                                notificationSize = dataArray.length()
                                for (i in 0 until dataArray.length()) {
                                    val dataObject = dataArray.getJSONObject(i)
                                    if (pushNotificationArrayList.size == 0) {
                                        pushNotificationArrayList.add(getSearchValues(dataObject))
                                    } else {
                                        var isFound = false
                                        for (j in pushNotificationArrayList.indices) {
                                            val listId = dataObject.optString("id")
                                            if (listId.equals(
                                                    pushNotificationArrayList[j].id,
                                                    ignoreCase = true
                                                )
                                            ) {
                                                isFound = true
                                            }
                                        }
                                        if (!isFound) {
                                            pushNotificationArrayList.add(getSearchValues(dataObject))
                                        }
                                    }
                                }
                                mPushNotificationListAdapter = PushNotificationListAdapter(
                                    mContext,
                                    pushNotificationArrayList
                                )
                                notificationRecycler.adapter =
                                    mPushNotificationListAdapter
                                if (isFromBottom) {
                                    notificationRecycler.scrollToPosition(pushNotificationArrayList.size - 16)
                                }
                                if (swipeRefresh) {
                                    notificationRecycler.scrollToPosition(pushNotificationArrayList.size)
                                }
                                mPushNotificationListAdapter.onBottomReachedListener =
                                    object : OnBottomReachedListener {
                                        override fun onBottomReached(position: Int) {
                                            isFromBottom = true
                                            val listSize = pushNotificationArrayList.size
                                            this@NotificationsFragment.pageFrom = pushNotificationArrayList[listSize - 1].id!!
                                            val scroll = "old"
                                            if (notificationSize == 15) {
                                                if (AppUtils.checkInternet(mContext)) {
                                                    callPushNotification(
                                                        this@NotificationsFragment.pageFrom,
                                                        scroll
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
                                    }
                            } else {
                                if (swipeRefresh) {
                                } else {
                                    Toast.makeText(
                                        mContext,
                                        "Currently you do not have any notification.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressBar!!.visibility = View.GONE
            }

        })
        notificationRecycler!!.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                if (pushNotificationArrayList!![position].pushType.equals(
                        "",
                        ignoreCase = true
                    )
                ) {
                    mIntent = Intent(context, TextAlertActivity::class.java)
                    mIntent!!.putExtra("position", position)
                    mIntent!!.putExtra("PushID", pushNotificationArrayList!![position].id)
                    mIntent!!.putExtra("Day", pushNotificationArrayList!![position].day)
                    mIntent!!.putExtra(
                        "Month",
                        pushNotificationArrayList!![position].monthString
                    )
                    mIntent!!.putExtra("Year", pushNotificationArrayList!![position].year)
                    mIntent!!.putExtra(
                        "PushDate",
                        pushNotificationArrayList!![position].pushTime
                    )
                    context!!.startActivity(mIntent)
                }
                if (pushNotificationArrayList!![position].pushType.equals("Image", ignoreCase = true) || pushNotificationArrayList!![position].pushType.equals("Text", ignoreCase = true)) {
                    mIntent = Intent(context, ImageAlertActivity::class.java)
                    mIntent!!.putExtra("PushID", pushNotificationArrayList!![position].id)
                    mIntent!!.putExtra("Day", pushNotificationArrayList!![position].day)
                    mIntent!!.putExtra(
                        "Month",
                        pushNotificationArrayList!![position].monthString
                    )
                    mIntent!!.putExtra("Year", pushNotificationArrayList!![position].year)
                    mIntent!!.putExtra(
                        "PushDate",
                        pushNotificationArrayList!![position].pushTime
                    )
                    println("pushID" + pushNotificationArrayList!![position].id)
                    context!!.startActivity(mIntent)
                }
                if (pushNotificationArrayList!![position].pushType.equals(
                        "Voice",
                        ignoreCase = true
                    )
                ) {
                    mIntent = Intent(context, AudioAlertActivity::class.java)
                    mIntent!!.putExtra("PushID", pushNotificationArrayList!![position].id)
                    mIntent!!.putExtra("Day", pushNotificationArrayList!![position].day)
                    mIntent!!.putExtra(
                        "Month",
                        pushNotificationArrayList!![position].monthString
                    )
                    mIntent!!.putExtra("Year", pushNotificationArrayList!![position].year)
                    mIntent!!.putExtra(
                        "PushDate",
                        pushNotificationArrayList!![position].pushTime
                    )
                    context!!.startActivity(mIntent)
                }
                if (pushNotificationArrayList!![position].pushType.equals(
                        "Video",
                        ignoreCase = true
                    )
                ) {
                    mIntent = Intent(context, VideoAlertActivity::class.java)
                    mIntent!!.putExtra("PushID", pushNotificationArrayList!![position].id)
                    mIntent!!.putExtra("Day", pushNotificationArrayList!![position].day)
                    mIntent!!.putExtra(
                        "Month",
                        pushNotificationArrayList!![position].monthString
                    )
                    mIntent!!.putExtra("Year", pushNotificationArrayList!![position].year)
                    mIntent!!.putExtra(
                        "PushDate",
                        pushNotificationArrayList!![position].pushTime
                    )
                    context!!.startActivity(mIntent)
                }
            }

        })
    }

    private fun getSearchValues(dataObject: JSONObject?): PushNotificationModel {
        val mPushNotificationModel = PushNotificationModel()
        mPushNotificationModel.pushType = dataObject!!.optString("alert_type")
        mPushNotificationModel.id = dataObject.optString("id")
        mPushNotificationModel.headTitle = dataObject.optString("title")
        mPushNotificationModel.pushDate = dataObject.optString("time_Stamp")
        val mDate: String? = mPushNotificationModel.pushDate
        var mEventDate = Date()
        mEventDate = sdfcalendar!!.parse(mDate)
        val format2 = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val startTime = format2.format(mEventDate)
        mPushNotificationModel.pushTime = startTime
        val dayOfTheWeek = DateFormat.format("EEEE", mEventDate) as String // Thursday
        val day = DateFormat.format("dd", mEventDate) as String // 20
        val monthString = DateFormat.format("MMM", mEventDate) as String // June
        val monthNumber = DateFormat.format("MM", mEventDate) as String // 06
        val year = DateFormat.format("yyyy", mEventDate) as String // 2013
        mPushNotificationModel.dayOfTheWeek = dayOfTheWeek
        mPushNotificationModel.day = day
        mPushNotificationModel.monthString = monthString
        mPushNotificationModel.monthNumber = monthNumber
        mPushNotificationModel.year = year
        return mPushNotificationModel
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initialiseUI() {
        mTitleTextView = mRootView.findViewById<View>(R.id.titleTextView) as TextView
        mTitleTextView.text = NaisClassNameConstants.NOTIFICATIONS
        isFromBottom = false
        swipeRefresh = false
        pushNotificationArrayList = ArrayList()
        notificationRecycler = mRootView.findViewById(R.id.notification_recycler)
//        swipeNotification = mRootView.findViewById(R.id.swipe_notification)
        constraintMain = mRootView.findViewById<View>(R.id.relMain) as ConstraintLayout
        progressBar = mRootView.findViewById(R.id.progressBar)
        constraintMain.setOnClickListener {  }
        notificationRecycler.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(mContext)
        val spacing = 10
        val itemDecoration = ItemOffsetDecoration(mContext, spacing)
        notificationRecycler.addItemDecoration(
            DividerItemDecoration(mContext.resources.getDrawable(R.drawable.list_divider))
        )
        notificationRecycler.addItemDecoration(itemDecoration)

        notificationRecycler.layoutManager = linearLayoutManager

    }


}