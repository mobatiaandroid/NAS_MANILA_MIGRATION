package com.mobatia.nasmanila.fragments.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.api.ApiClient
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.common_classes.ProgressBarDialog
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.common.constants.NaisTabConstants
import com.mobatia.nasmanila.fragments.about_us.AboutUsFragment
import com.mobatia.nasmanila.fragments.absence.AbsenceFragment
import com.mobatia.nasmanila.fragments.calendar.CalendarWebViewFragment
import com.mobatia.nasmanila.fragments.category_main.CategoryMainFragment
import com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment
import com.mobatia.nasmanila.fragments.home.adapter.ImagePagerDrawableAdapter
import com.mobatia.nasmanila.fragments.nas_today.NasTodayFragment
import com.mobatia.nasmanila.fragments.notifications.NotificationsFragment
import com.mobatia.nasmanila.fragments.parent_essentials.ParentEssentialsFragment
import com.mobatia.nasmanila.fragments.parents_meeting.ParentsMeetingFragment
import com.mobatia.nasmanila.fragments.social_media.SocialMediaFragment
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeScreenRegisteredUserFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeScreenRegisteredUserFragment2( s: String,
                                         mDrawerLayout: DrawerLayout,
                                         mHomeListView: ListView,
                                         mLinearLayout: LinearLayout,
                                         mListItemArray: Array<String>,
                                         mListImgArray: TypedArray) : Fragment() {
    lateinit var rootView: View
    private var mContext: Context? = null
    var title: String = s
    var drawerLayout: DrawerLayout? = mDrawerLayout
    var listView: ListView? = mHomeListView
    var listItemArray: Array<String> = mListItemArray
    var linearLayout: LinearLayout? = mLinearLayout
    var listImageArray: TypedArray = mListImgArray
    var bannerImagePager: ViewPager? = null
    var progressBarDialog: ProgressBarDialog? = null
    var currentPage = 0
    private var INTENT_TAB_ID: String? = null
    var intentTabIdToProceed = ""
    var versionName = ""
    var versionCode = -1
    private lateinit var mSectionText: Array<String?>
    private lateinit var homeBannerUrlImageArray: ArrayList<String>
    private var mRelOne: RelativeLayout? = null
    private var mRelTwo: RelativeLayout? = null
    private var mRelThree: RelativeLayout? = null
    private var mRelFour: RelativeLayout? = null
    private var mRelFive: RelativeLayout? = null
    private var mRelSix: RelativeLayout? = null
    private var mRelSeven: RelativeLayout? = null
    private var mRelEight: RelativeLayout? = null
    private var mRelNine: RelativeLayout? = null

    private var mTxtOne: TextView? = null
    private var mTxtTwo: TextView? = null
    private var mTxtThree: TextView? = null
    private var mTxtFour: TextView? = null
    private var mTxtFive: TextView? = null
    private var mTxtSix: TextView? = null
    private var mTxtSeven: TextView? = null
    private var mTxtEight: TextView? = null
    private var mTxtNine: TextView? = null
    private var mImgOne: ImageView? = null
    private var mImgTwo: ImageView? = null
    private var mImgThree: ImageView? = null
    private var mImgFour: ImageView? = null
    private var mImgFive: ImageView? = null
    private var mImgSix: ImageView? = null
    private var mImgSeven: ImageView? = null
    private var mImgEight: ImageView? = null
    private var mImgNine: ImageView? = null
    private var android_app_version: String? = null
    private val PERMISSION_CALLBACK_CONSTANT_CALENDAR = 1
    private val PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2
    private val PERMISSION_CALLBACK_CONSTANT_LOCATION = 3
    private val REQUEST_PERMISSION_CALENDAR = 101
    private val REQUEST_PERMISSION_EXTERNAL_STORAGE = 102
    private val REQUEST_PERMISSION_LOCATION = 103
    private val calendarToSettings = false
    private val externalStorageToSettings = false
    private var locationToSettings = false
    var permissionsRequiredCalendar = arrayOf(
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    )
    var permissionsRequiredExternalStorage = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var permissionsRequiredLocation = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private var calendarPermissionStatus: SharedPreferences? = null
    private var externalStoragePermissionStatus: SharedPreferences? = null
    private var locationPermissionStatus: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home_screen_registered_user, container, false)
        mContext = activity
        calendarPermissionStatus = activity!!.getSharedPreferences(
            "calendarPermissionStatus",
            Context.MODE_PRIVATE
        )
        externalStoragePermissionStatus = activity!!.getSharedPreferences(
            "externalStoragePermissionStatus",
            Context.MODE_PRIVATE
        )
        locationPermissionStatus = activity!!.getSharedPreferences(
            "locationPermissionStatus",
            Context.MODE_PRIVATE
        )
        initialiseUI()
        setListeners()
        getButtonBgAndTextImages()
        return rootView
    }

    private fun getButtonBgAndTextImages() {
        if (PreferenceManager.getButtonOneTextImage(mContext!!)!!.toInt() != 0) {
            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                mImgOne!!.background = mContext!!.resources.getDrawable(R.drawable.news)
            } else {
                mImgOne!!.background = mContext!!.resources.getDrawable(R.drawable.news)
            }
            mTxtOne!!.text = "NAIS MANILA TODAY"
            mRelOne!!.setBackgroundColor(PreferenceManager.getButtonOneBg(mContext!!))
        }
        if (PreferenceManager.getButtonTwoTextImage(mContext!!)!!.toInt() != 0) {
            mImgTwo!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonTwoTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray[PreferenceManager.getButtonTwoTextImage(mContext!!)!!
                    .toInt()].equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray[PreferenceManager.getButtonTwoTextImage(mContext!!)!!.toInt()].toUpperCase()
            }
            mTxtTwo!!.text = relTwoStr
            mRelTwo!!.setBackgroundColor(PreferenceManager.getButtonTwoBg(mContext!!))
        }
        if (PreferenceManager.getButtonThreeTextImage(mContext!!)!!.toInt() != 0) {
            mImgThree!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonThreeTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr: String = if (listItemArray.get(
                    PreferenceManager.getButtonThreeTextImage(
                        mContext!!
                    )!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray[PreferenceManager.getButtonThreeTextImage(mContext!!)!!.toInt()].toUpperCase(
                    Locale.ROOT
                )
            }
            mTxtThree!!.text = relTwoStr
            mRelThree!!.setBackgroundColor(PreferenceManager.getButtonThreeBg(mContext!!))
        }
        if (PreferenceManager.getButtonFourTextImage(mContext!!)!!.toInt() != 0) {
            mImgFour!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonFourTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr: String = if (listItemArray[PreferenceManager.getButtonFourTextImage(
                    mContext!!
                )!!.toInt()].equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray[PreferenceManager.getButtonFourTextImage(mContext!!)!!.toInt()].toUpperCase(
                    Locale.ROOT
                )
            }
            mTxtFour!!.text = relTwoStr
            mRelFour!!.setBackgroundColor(PreferenceManager.getButtonFourBg(mContext!!))
        }
        if (PreferenceManager.getButtonFiveTextImage(mContext!!)!!.toInt() != 0) {
            mImgFive!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager.getButtonFiveTextImage(
                        mContext!!
                    )!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager.getButtonFiveTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(PreferenceManager.getButtonFiveTextImage(mContext!!)!!.toInt()).toUpperCase()
            }
            mTxtFive!!.text = relTwoStr
            mRelFive!!.setBackgroundColor(
                PreferenceManager
                    .getButtonFiveBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonSixTextImage(mContext!!)!!.toInt() != 0) {
            mImgSix!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonSixTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonSixTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonSixTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtSix!!.text = relTwoStr
            mRelSix!!.setBackgroundColor(
                PreferenceManager
                    .getButtonSixBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonSevenTextImage(mContext!!)!!.toInt() != 0) {
            mImgSeven!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonSevenTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonSevenTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonSevenTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtSeven!!.text = relTwoStr
            mRelSeven!!.setBackgroundColor(
                PreferenceManager
                    .getButtonSevenBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonEightTextImage(mContext!!)!!.toInt() != 0) {
            mImgEight!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonEightTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonEightTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonEightTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtEight!!.text = relTwoStr
            mRelEight!!.setBackgroundColor(
                PreferenceManager
                    .getButtonEightBg(mContext!!)
            )
        }
        if (PreferenceManager
                .getButtonNineTextImage(mContext!!)!!.toInt() != 0) {
            mImgNine!!.setImageDrawable(
                listImageArray.getDrawable(
                    PreferenceManager
                        .getButtonNineTextImage(mContext!!)!!.toInt()
                )
            )
            var relTwoStr = ""
            relTwoStr = if (listItemArray.get(
                    PreferenceManager
                        .getButtonNineTextImage(mContext!!)!!.toInt()
                ).equals(NaisClassNameConstants.CCAS, ignoreCase = true)) {
                NaisClassNameConstants.CCAS
            } else {
                listItemArray.get(
                    PreferenceManager
                        .getButtonNineTextImage(mContext!!)!!.toInt()
                ).toUpperCase()
            }
            mTxtNine!!.text = relTwoStr
            mRelNine!!.setBackgroundColor(
                PreferenceManager
                    .getButtonNineBg(mContext!!)
            )
        }
    }

    private fun setListeners() {
        mRelOne!!.setOnClickListener{
            INTENT_TAB_ID = PreferenceManager.getButtonOneTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelTwo!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonTwoTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelThree!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonThreeTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelFour!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonFourTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelFive!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonFiveTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelSix!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonSixTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelSeven!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonSevenTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelEight!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonEightTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
        mRelNine!!.setOnClickListener {
            INTENT_TAB_ID = PreferenceManager.getButtonNineTabId(mContext!!)
            checkIntent(INTENT_TAB_ID)
        }
    }

    private fun checkIntent(intentTabId: String?) {
        var mFragment: Fragment? = null
        if (intentTabId.equals(NaisTabConstants.TAB_CALENDAR_REG, ignoreCase = true)) {
            mFragment = CalendarWebViewFragment(
                NaisClassNameConstants.CALENDAR,
                NaisTabConstants.TAB_CALENDAR_REG
            )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_NOTIFICATIONS_REG, ignoreCase = true)) {
            mFragment = NotificationsFragment(
                NaisClassNameConstants.NOTIFICATIONS,
                NaisTabConstants.TAB_NOTIFICATIONS_REG
            )
            fragmentIntent(mFragment)
        } else if ( intentTabId.equals(
                NaisTabConstants.TAB_PARENT_ESSENTIALS_REG,
                ignoreCase = true
            )) {
            mFragment = ParentEssentialsFragment(
                NaisClassNameConstants.PARENT_ESSENTIALS,
                NaisTabConstants.TAB_PARENT_ESSENTIALS_REG
            )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_PROGRAMMES_REG, ignoreCase = true)) {
            mFragment = CategoryMainFragment("Programmes", NaisTabConstants.TAB_PROGRAMMES_REG)
            fragmentIntent(mFragment!!)
        } else if (intentTabId.equals(NaisTabConstants.TAB_SOCIAL_MEDIA_REG, ignoreCase = true)) {
            mFragment = SocialMediaFragment(
                NaisClassNameConstants.SOCIAL_MEDIA,
                NaisTabConstants.TAB_SOCIAL_MEDIA_REG
            )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_ABOUT_US_REG, ignoreCase = true)) {
            mFragment = AboutUsFragment(
                NaisClassNameConstants.ABOUT_US,
                NaisTabConstants.TAB_ABOUT_US_REG
            )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_CONTACT_US_REG, ignoreCase = true)) {
            mFragment = ContactUsFragment(
                NaisClassNameConstants.CONTACT_US,
                NaisTabConstants.TAB_CONTACT_US_REG
            )
            if (Build.VERSION.SDK_INT < 23) {
                fragmentIntent(mFragment)
            } else {
                if (ActivityCompat.checkSelfPermission(activity!!, permissionsRequiredLocation[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        activity!!,
                        permissionsRequiredLocation[1]
                    ) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity!!,
                            permissionsRequiredLocation[0]
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                            activity!!,
                            permissionsRequiredLocation[1]
                        )) {
                        val builder = AlertDialog.Builder(activity!!)
                        builder.setTitle("Need Location Permission")
                        builder.setMessage("This module needs location permissions.")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            ActivityCompat.requestPermissions(
                                activity!!,
                                permissionsRequiredLocation,
                                PERMISSION_CALLBACK_CONSTANT_LOCATION
                            )
                        }
                        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                        builder.show()
                    } else if (locationPermissionStatus!!.getBoolean(
                            permissionsRequiredLocation[0],
                            false
                        )) {
                        val builder = AlertDialog.Builder(activity!!)
                        builder.setTitle("Need Location Permission")
                        builder.setMessage("This module needs location permissions.")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = true
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", activity!!.packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, REQUEST_PERMISSION_LOCATION)
                            Toast.makeText(
                                mContext,
                                "Go to settings and grant access to location",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        builder.setNegativeButton("Cancel") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = false
                        }
                        builder.show()
                    } else if (locationPermissionStatus!!.getBoolean(
                            permissionsRequiredLocation[1],
                            false
                        )) {
                        val builder = AlertDialog.Builder(activity!!)
                        builder.setTitle("Need Location Permission")
                        builder.setMessage("This module needs location permissions.")
                        builder.setPositiveButton("Grant") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = true
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", activity!!.packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, REQUEST_PERMISSION_LOCATION)
                            Toast.makeText(
                                mContext,
                                "Go to settings and grant access to location",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        builder.setNegativeButton("Cancel") { dialog, which ->
                            dialog.cancel()
                            locationToSettings = false
                        }
                        builder.show()
                    } else {
                        requestPermissions(
                            permissionsRequiredLocation,
                            PERMISSION_CALLBACK_CONSTANT_LOCATION
                        )
                    }
                    val editor = locationPermissionStatus!!.edit()
                    editor.putBoolean(permissionsRequiredLocation[0], true)
                    editor.apply()
                } else {
                    fragmentIntent(mFragment)
                }
            }
        } else if (intentTabId.equals(NaisTabConstants.TAB_NAS_TODAY, ignoreCase = true)) {
            mFragment = NasTodayFragment(
                NaisClassNameConstants.NAS_TODAY,
                NaisTabConstants.TAB_NAS_TODAY
            )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_PARENTS_MEETING_REG, ignoreCase = true)) {
            mFragment = ParentsMeetingFragment(
                NaisClassNameConstants.PARENT_EVENING,
                NaisTabConstants.TAB_PARENTS_MEETING_REG
            )
            fragmentIntent(mFragment)
        } else if (intentTabId.equals(NaisTabConstants.TAB_ABSENCES_REG, ignoreCase = true)) {
            mFragment = AbsenceFragment(
                NaisClassNameConstants.ABSENCE,
                NaisTabConstants.TAB_ABSENCES_REG
            )
            fragmentIntent(mFragment)
        } /*else if (intentTabId.equals(
                NaisTabConstants.TAB_PARENTS_ASSOCIATION_REG,
                ignoreCase = true
            )) {
            mFragment = ParentAssociationsFragment(
                NaisClassNameConstants.PARENTS_ASSOCIATION,
                NaisTabConstants.TAB_PARENTS_ASSOCIATION_REG
            )
            fragmentIntent(mFragment)
        }*/
    }

    private fun fragmentIntent(mFragment: Fragment?) {
        if (mFragment != null) {
            val fragmentManager = activity!!.supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.frame_container, mFragment, title)
                .addToBackStack(title).commitAllowingStateLoss()
        }
    }

    private fun initialiseUI() {
        bannerImagePager = rootView.findViewById<View>(R.id.bannerImagePager) as ViewPager
        mRelOne = rootView.findViewById<View>(R.id.relOne) as RelativeLayout?
        mRelTwo = rootView.findViewById<View>(R.id.relTwo) as RelativeLayout
        mRelThree = rootView.findViewById<View>(R.id.relThree) as RelativeLayout
        mRelFour = rootView.findViewById<View>(R.id.relFour) as RelativeLayout
        mRelFive = rootView.findViewById<View>(R.id.relFive) as RelativeLayout
        mRelSix = rootView.findViewById<View>(R.id.relSix) as RelativeLayout
        mRelSeven = rootView.findViewById<View>(R.id.relSeven) as RelativeLayout
        mRelEight = rootView.findViewById<View>(R.id.relEight) as RelativeLayout
        mRelNine = rootView.findViewById<View>(R.id.relNine) as RelativeLayout
        mTxtOne = rootView.findViewById<View>(R.id.relTxtOne) as TextView?
        mImgOne = rootView.findViewById<View>(R.id.relImgOne) as ImageView?
        mTxtTwo = rootView.findViewById<View>(R.id.relTxtTwo) as TextView
        mImgTwo = rootView.findViewById<View>(R.id.relImgTwo) as ImageView
        mTxtThree = rootView.findViewById<View>(R.id.relTxtThree) as TextView
        mImgThree = rootView.findViewById<View>(R.id.relImgThree) as ImageView
        mTxtFour = rootView.findViewById<View>(R.id.relTxtFour) as TextView
        mImgFour = rootView.findViewById<View>(R.id.relImgFour) as ImageView
        mTxtFive = rootView.findViewById<View>(R.id.relTxtFive) as TextView
        mImgFive = rootView.findViewById<View>(R.id.relImgFive) as ImageView
        mTxtSix = rootView.findViewById<View>(R.id.relTxtSix) as TextView
        mImgSix = rootView.findViewById<View>(R.id.relImgSix) as ImageView
        mTxtSeven = rootView.findViewById<View>(R.id.relTxtSeven) as TextView
        mImgSeven = rootView.findViewById<View>(R.id.relImgSeven) as ImageView
        mTxtEight = rootView.findViewById<View>(R.id.relTxtEight) as TextView
        mImgEight = rootView.findViewById<View>(R.id.relImgEight) as ImageView
        mTxtNine = rootView.findViewById<View>(R.id.relTxtNine) as TextView
        mImgNine = rootView.findViewById<View>(R.id.relImgNine) as ImageView

        progressBarDialog = ProgressBarDialog(context!!)
        homeBannerUrlImageArray = ArrayList<String>()
//        getVersionInfo()
        if (AppUtils.checkInternet(mContext!!)) {
            getBanner()
        } else {

            homeBannerUrlImageArray.add("")
            bannerImagePager!!.adapter = ImagePagerDrawableAdapter(
                mContext!!,
                homeBannerUrlImageArray
            )
            Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show()
        }

        if (homeBannerUrlImageArray != null) {
            val handler = Handler()
            val update = Runnable {
                if (currentPage == homeBannerUrlImageArray.size) {
                    currentPage = 0
                    bannerImagePager!!.setCurrentItem(
                        currentPage,
                        false
                    )
                } else {
                    bannerImagePager!!
                        .setCurrentItem(currentPage++, true)
                }
            }
            val swipeTimer = Timer()
            swipeTimer.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, 100, 3000)
        }
        mSectionText = arrayOfNulls(9)
    }

    private fun getBanner() {
        val call: Call<ResponseBody> = ApiClient.getApiService().getBannerImages()
        progressBarDialog!!.show()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressBarDialog!!.dismiss()
                val responseData = response.body()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData.string())
                    if (jsonObject.has("status")) {
                        val status = jsonObject.optInt("status")
                        if (status == 100) {
                            val responseArray: JSONObject = jsonObject.optJSONObject("responseArray")
                            val dataArray: JSONArray = responseArray.optJSONArray("banner_images")
                            if (dataArray.length() > 0) {
                                for (i in 0 until dataArray.length()) {
//												JSONObject dataObject = dataArray.getJSONObject(i);
                                    homeBannerUrlImageArray.add(dataArray.optString(i))
                                }
                                bannerImagePager!!.adapter =
                                    ImagePagerDrawableAdapter(mContext!!, homeBannerUrlImageArray)
                            } else {
                                homeBannerUrlImageArray.add("default_banner_home")
                                bannerImagePager!!.adapter = ImagePagerDrawableAdapter(
                                    mContext, homeBannerUrlImageArray
                                )
                            }
                        } else {
                            Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    AppUtils.getAccessToken(mContext!!)
                    getBanner()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressBarDialog!!.dismiss()            }
        })
    }
}