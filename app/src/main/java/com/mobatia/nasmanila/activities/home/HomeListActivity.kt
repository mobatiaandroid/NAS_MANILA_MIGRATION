package com.mobatia.nasmanila.activities.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.TypedArray
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.GestureDetector
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.adapter.HomeListAdapter
import com.mobatia.nasmanila.common.common_classes.PreferenceManager
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.common.constants.NaisTabConstants
import com.mobatia.nasmanila.fragments.about_us.AboutUsFragment
import com.mobatia.nasmanila.fragments.absence.AbsenceFragment
import com.mobatia.nasmanila.fragments.calendar.CalendarWebViewFragment
import com.mobatia.nasmanila.fragments.category_main.CategoryMainFragment
import com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment
import com.mobatia.nasmanila.fragments.home.HomeScreenGuestUserFragment
import com.mobatia.nasmanila.fragments.notifications.NotificationsFragment
import com.mobatia.nasmanila.fragments.parent_essentials.ParentEssentialsFragment
import com.mobatia.nasmanila.fragments.parents_meeting.ParentsMeetingFragment
import com.mobatia.nasmanila.fragments.settings.SettingsFragment
import com.mobatia.nasmanila.fragments.social_media.SocialMediaFragment

class HomeListActivity : AppCompatActivity() {
    lateinit var linearLayout: LinearLayout
    lateinit var homeListView: ListView
    //    lateinit var studentsArrayList: ArrayList<StudentModel>
    lateinit var listAdapter: HomeListAdapter
    lateinit var context: Context
    lateinit var activity: Activity
    lateinit var drawerLayout: DrawerLayout
    lateinit var listItemArray: Array<String>
    lateinit var listImgArray: TypedArray
    lateinit var detector: GestureDetector
    lateinit var fragment: Fragment
    var sPosition = 0
    lateinit var downArrow: ImageView
    var preLast = 0
    var notificationRecieved = 0
    lateinit var extras: Bundle
    lateinit var drawerButton: ImageView
    lateinit var settingsButton: ImageView
    var drawerToggle: ActionBarDrawerToggle? = null

    private val PERMISSION_CALLBACK_CONSTANT_LOCATION = 3
    private val REQUEST_PERMISSION_LOCATION = 103
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
    private var calendarToSettings = false
    private val externalStorageToSettings = false
    private var locationToSettings = false

    var tabPositionProceed = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_list)
        context = this
        activity = this
        calendarPermissionStatus = getSharedPreferences("calendarPermissionStatus", MODE_PRIVATE)
        externalStoragePermissionStatus = getSharedPreferences(
            "externalStoragePermissionStatus",
            MODE_PRIVATE
        )
        locationPermissionStatus = getSharedPreferences("locationPermissionStatus", MODE_PRIVATE)
//        extras = intent.extras!!
//        notificationRecieved = extras.getInt("Notification_Recieved", 0)
        initialiseUI()
        initialSettings()
//        getStudentList()
        if (notificationRecieved == 1) {
            displayView(0)
            displayView(2)
        } else
            displayView(0)
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        drawerToggle!!.syncState()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle!!.onConfigurationChanged(newConfig)

    }

    private fun displayView(position: Int) {
        tabPositionProceed = position
        if (PreferenceManager.getUserCode(context) != "") {
            settingsButton.visibility = View.VISIBLE
        } else {
            settingsButton.visibility = View.INVISIBLE
        }
        if(PreferenceManager.getUserCode(context) == "") {
            when (position) {
                0 -> {
                    settingsButton.visibility = View.GONE
                    fragment = HomeScreenGuestUserFragment(
                        listItemArray[position], drawerLayout, homeListView, linearLayout,
                        listItemArray, listImgArray
                    )
                    replaceFragmentsSelected(position)
                }
                1 -> {
                    settingsButton.visibility = View.GONE
                    fragment = NotificationsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_NOTIFICATIONS_GUEST
                    )
                    replaceFragmentsSelected(position)
                }
                2 -> {
                    settingsButton.visibility = View.GONE
                    fragment = ParentEssentialsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_PARENT_ESSENTIALS_GUEST
                    )
                    replaceFragmentsSelected(position)
                }
                3 -> {
                    settingsButton.visibility = View.GONE
                    fragment = CategoryMainFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_PROGRAMMES_GUEST
                    )
                    replaceFragmentsSelected(position)
                }
                4 -> {
                    settingsButton.visibility = View.GONE
                    fragment = SocialMediaFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_SOCIAL_MEDIA
                    )
                    replaceFragmentsSelected(position)
                }
                5 -> {
                    // about us
                    settingsButton.visibility = View.GONE
                    fragment = AboutUsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_ABOUT_US_GUEST
                    )
                    replaceFragmentsSelected(position)
                }
                6 -> {
                    settingsButton.visibility = View.GONE
                    fragment = ContactUsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_CONTACT_US_GUEST
                    )
                    if (Build.VERSION.SDK_INT < 23) {
                        replaceFragmentsSelected(position)
                    } else {
                        if (ActivityCompat.checkSelfPermission(
                                activity,
                                permissionsRequiredLocation[0]
                            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                                activity,
                                permissionsRequiredLocation[1]
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity,
                                    permissionsRequiredLocation[0]
                                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity,
                                    permissionsRequiredLocation[1]
                                )
                            ) {
                                val builder = AlertDialog.Builder(activity)
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton("Grant") { dialog, which ->
                                    dialog.cancel()
                                    ActivityCompat.requestPermissions(
                                        activity,
                                        permissionsRequiredLocation,
                                        PERMISSION_CALLBACK_CONSTANT_LOCATION
                                    )
                                }
                                builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                                builder.show()
                            } else if (locationPermissionStatus!!.getBoolean(
                                    permissionsRequiredLocation[0],
                                    false
                                )
                            ) {
                                val builder = AlertDialog.Builder(activity)
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton("Grant") { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true
                                    Toast.makeText(
                                        context,
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
                                )
                            ) {
                                val builder = AlertDialog.Builder(activity)
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton("Grant") { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts("package", activity.packageName, null)
                                    intent.data = uri
                                    startActivityForResult(intent, REQUEST_PERMISSION_LOCATION)
                                    Toast.makeText(
                                        context,
                                        "Go to settings and grant access to location",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
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
                            replaceFragmentsSelected(position)
                        }
                    }
                }
            }
        } else {
            when (position) {
                0 -> {
                    fragment = HomeScreenGuestUserFragment(
                        listItemArray[position],
                        drawerLayout,
                        homeListView,
                        linearLayout,
                        listItemArray,
                        listImgArray
                    )
                    replaceFragmentsSelected(position)
                }
                1 -> {
                    fragment = CalendarWebViewFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_CALENDAR_REG
                    )
                    replaceFragmentsSelected(position)
                }
                2 -> {
                    fragment = NotificationsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_NOTIFICATIONS_REG
                    )
                    replaceFragmentsSelected(position)
                }
                3 -> {
                    fragment = AbsenceFragment(
                        listItemArray[position], NaisTabConstants.TAB_ABSENCES_REG
                    )
                    replaceFragmentsSelected(position)
                }
                4 -> {
                    fragment = ParentEssentialsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_PARENT_ESSENTIALS_REG
                    )
                    replaceFragmentsSelected(position)
                }
                5 -> {
                    fragment = CategoryMainFragment(
                        listItemArray[position], NaisTabConstants.TAB_PROGRAMMES_REG
                    )
                    replaceFragmentsSelected(position)
                }
                6 -> {
                    fragment = ParentsMeetingFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_PARENTS_MEETING_REG
                    )
                    replaceFragmentsSelected(position)
                }
                7 -> {
                    fragment = SocialMediaFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_SOCIAL_MEDIA_REG
                    )
                    replaceFragmentsSelected(position)
                }
                8 -> {
                    fragment = AboutUsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_ABOUT_US_REG
                    )
                    replaceFragmentsSelected(position)
                }
                9 -> {
                    fragment = ContactUsFragment(
                        listItemArray[position],
                        NaisTabConstants.TAB_CONTACT_US_REG
                    )
                    if (Build.VERSION.SDK_INT < 23) {
                        replaceFragmentsSelected(position)
                    } else {
                        if (ActivityCompat.checkSelfPermission(
                                activity,
                                permissionsRequiredLocation[0]
                            ) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(
                                activity,
                                permissionsRequiredLocation[1]
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity,
                                    permissionsRequiredLocation[0]
                                )
                                || ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity,
                                    permissionsRequiredLocation[1]
                                )
                            ) {
                                val builder = AlertDialog.Builder(
                                    activity
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    ActivityCompat.requestPermissions(
                                        activity,
                                        permissionsRequiredLocation,
                                        PERMISSION_CALLBACK_CONSTANT_LOCATION
                                    )
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which -> dialog.cancel() }
                                builder.show()
                            } else if (locationPermissionStatus!!.getBoolean(
                                    permissionsRequiredLocation[0],
                                    false
                                )
                            ) {
                                val builder = AlertDialog.Builder(
                                    activity
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts("package", activity.packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        REQUEST_PERMISSION_LOCATION
                                    )
                                    Toast.makeText(
                                        context,
                                        "Go to settings and grant access to location",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = false
                                }
                                builder.show()
                            } else if (locationPermissionStatus!!.getBoolean(
                                    permissionsRequiredLocation[1],
                                    false
                                )
                            ) {
                                val builder = AlertDialog.Builder(
                                    activity
                                )
                                builder.setTitle("Need Location Permission")
                                builder.setMessage("This module needs location permissions.")
                                builder.setPositiveButton(
                                    "Grant"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = true
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri =
                                        Uri.fromParts("package", activity.packageName, null)
                                    intent.data = uri
                                    startActivityForResult(
                                        intent,
                                        REQUEST_PERMISSION_LOCATION
                                    )
                                    Toast.makeText(
                                        context,
                                        "Go to settings and grant access to location",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                builder.setNegativeButton(
                                    "Cancel"
                                ) { dialog, which ->
                                    dialog.cancel()
                                    locationToSettings = false
                                }
                                builder.show()
                            } else {
                                ActivityCompat.requestPermissions(
                                    activity,
                                    permissionsRequiredLocation,
                                    PERMISSION_CALLBACK_CONSTANT_LOCATION
                                )
                            }
                            val editor = locationPermissionStatus!!.edit()
                            editor.putBoolean(permissionsRequiredLocation[0], true)
                            editor.apply()
                        } else {
                            replaceFragmentsSelected(position)
                        }
                    }
                }
            }
        }
    }

    private fun replaceFragmentsSelected(position: Int) {
        settingsButton.visibility = View.VISIBLE
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.frame_container, fragment!!,
                    listItemArray[position]
                )
                .addToBackStack(listItemArray[position]).commitAllowingStateLoss()
            homeListView.setItemChecked(position, true)
            homeListView.setSelection(position)
            supportActionBar?.setTitle(R.string.null_value)
            if (drawerLayout.isDrawerOpen(linearLayout!!)) {
                drawerLayout.closeDrawer(linearLayout!!)
            }
        }
    }

    private fun initialiseUI() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_action_view_home)
        supportActionBar!!.elevation = 0f

        val view = supportActionBar!!.customView
        val toolbar = view.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)

        drawerButton = view.findViewById<View>(R.id.action_bar_back) as ImageView
        drawerButton.setOnClickListener {
            val fm = supportFragmentManager
            fm.findFragmentById(R.id.frame_container)
            if (drawerLayout.isDrawerOpen(linearLayout)) {
                drawerLayout.closeDrawer(linearLayout)
            } else {
                drawerLayout.openDrawer(linearLayout)
            }
        }
        settingsButton = view.findViewById<View>(R.id.action_bar_forward) as ImageView
        val logoClickImgView = view.findViewById<View>(R.id.logoClickImgView) as ImageView
        logoClickImgView.setOnClickListener {
            val fm = supportFragmentManager
            val currentFragment = fm.findFragmentById(R.id.frame_container)
            if (currentFragment != null) {
                if (currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.about_us.AboutUsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.absence.AbsenceFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.calendar.CalendarFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.calendar.CalendarWebViewFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.category1.CategoryMainFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.category2.CategoryFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.parentassociation.ParentAssociationsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.cca.CcaFragment", ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.communications.CommunicationsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.gallery.GalleryFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.early_years.EarlyYearsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.social_media.SocialMediaFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.ib_programme.IbProgrammeFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.nae_programmes.NaeProgrammesFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.notifications.NotificationsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.parent_essentials.ParentEssentialsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.performing_arts.PerformingArtsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.primary.PrimaryFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.secondary.SecondaryFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.sports.SportsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.contact_us.ContactUsFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.nas_today.NasTodayFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.parents_evening.ParentsEveningFragment",
                        ignoreCase = true
                    )
                    || currentFragment.javaClass.toString().equals(
                        "class com.mobatia.nasmanila.fragments.settings.SettingsFragment",
                        ignoreCase = true
                    )
                ) {
                    displayView(0)
                    if (PreferenceManager.getUserCode(context) == "") {
                        settingsButton.visibility = View.GONE
                    } else {
                        settingsButton.visibility = View.VISIBLE
                    }
                }
            }
        }
        if (PreferenceManager.getUserCode(context) == "") {
            settingsButton.visibility = View.GONE
        } else {
            settingsButton.visibility = View.VISIBLE
        }
        settingsButton.setOnClickListener {
            val fm = supportFragmentManager
            val currentFragment = fm.findFragmentById(R.id.frame_container)
            println(
                "nas current fragment "
                        + currentFragment!!.javaClass.toString()
            )
            if (!currentFragment.javaClass.toString().equals(
                    "class com.mobatia.nasmanila.fragments.settings.SettingsFragment",
                    ignoreCase = true
                )) {
                fragment = SettingsFragment(
                    NaisClassNameConstants.SETTINGS,
                    NaisTabConstants.TAB_SETTINGS
                )
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                    .add(R.id.frame_container, fragment, NaisClassNameConstants.SETTINGS)
                    .addToBackStack(NaisClassNameConstants.SETTINGS).commit()
                drawerLayout.closeDrawer(linearLayout)
                supportActionBar!!.setTitle(R.string.null_value)
                if (PreferenceManager.getUserCode(context) == "") {
                    settingsButton.visibility = View.GONE
                } else {
                    settingsButton.visibility = View.VISIBLE
                }
            }
        }
//        drawerToggle!!.syncState()
    }

    @SuppressLint("Recycle")
    private fun initialSettings() {
        homeListView = findViewById(R.id.homeList)
        downArrow = findViewById(R.id.downarrow)
        linearLayout = findViewById(R.id.linearLayout)

        if (PreferenceManager.getUserCode(context) != "") {
            listItemArray = context.resources.getStringArray(
                R.array.home_list_content_reg_items
            )
            listImgArray = context.resources.obtainTypedArray(
                R.array.home_list_reg_icons
            )
        } else {
            listItemArray = context.resources.getStringArray(
                R.array.home_list_content_guest_items
            )
            listImgArray = context.resources.obtainTypedArray(
                R.array.home_list_guest_icons
            )
        }
        listAdapter = HomeListAdapter(
            context, listItemArray,
            listImgArray, R.layout.custom_list_adapter, true
        )
        homeListView.adapter = listAdapter
        homeListView.setBackgroundColor(
            resources.getColor(
                R.color.split_bg
            )
        )
        homeListView.setOnItemClickListener { parent, view, position, id ->
            if (PreferenceManager.getIfHomeItemClickEnabled(context)) {
                if (PreferenceManager.getUserCode(context) == "") {
                    settingsButton.visibility = View.GONE
                } else {
                    settingsButton.visibility = View.VISIBLE
                }
                displayView(position)
            }
        }
        drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
//                mHomeListView.setOnItemLongClickListener(this);
//        mDetector = GestureDetector(this, GestureDetector.OnGestureListener)
//        drawerToggle = object : ActionBarDrawerToggle(
//            context as Activity?,
//            drawerLayout,
//            R.drawable.hamburgerbtn,
//            R.string.null_value,
//            R.string.null_value
//        )

        {


        }
        drawerLayout.setDrawerListener(drawerToggle)
//        mDrawerLayout!!.setOnTouchListener { v, event -> mDetector!!.onTouchEvent(event) }
        homeListView.setOnScrollListener(object : AbsListView.OnScrollListener {
            var mLastFirstVisibleItem = 0
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (view.id == homeListView.id) {
                    val currentFirstVisibleItem = homeListView.lastVisiblePosition
                    if (currentFirstVisibleItem == totalItemCount - 1) {
                        downArrow.visibility = View.INVISIBLE
                    } else {
                        downArrow.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}
