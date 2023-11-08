package com.mobatia.nasmanila.fragments.about_us

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.home.HomeListActivity
import com.mobatia.nasmanila.common.common_classes.AppUtils
import com.mobatia.nasmanila.common.common_classes.HeaderManager
import com.mobatia.nasmanila.common.constants.NaisClassNameConstants
import com.mobatia.nasmanila.fragments.about_us.adapter.AccreditationsRecyclerViewAdapter
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsModel

class AccreditationsActivity : AppCompatActivity() {

    private var mContext: Context? = null
    var extras: Bundle? = null
    var tab_type: String? = null
    var bannner_img:kotlin.String? = null
    var relativeHeader: LinearLayout? = null
    var headermanager: HeaderManager? = null
    var back: ImageView? = null
    var home: ImageView? = null
    var mAboutUsListArray: ArrayList<AboutUsModel>? = null
    var bannerImagePager: ImageView? = null
    var bannerUrlImageArray = ArrayList<String>()
    private var mTermsCalendarListView: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accreditations)
        mContext = this
        initialiseUI()
    }
    private fun initialiseUI() {
        extras = intent.extras
        if (extras != null) {
            mAboutUsListArray = extras!!
                .getSerializable("array") as ArrayList<AboutUsModel>?
            bannner_img = extras!!.getString("banner_image")
            println("Image url--$bannner_img")
            if (bannner_img != "") {
                bannerUrlImageArray.add(bannner_img!!)
            }
        }
        relativeHeader = findViewById<View>(R.id.relativeHeader) as LinearLayout
        mTermsCalendarListView = findViewById<View>(R.id.mTermsCalendarListView) as ListView
        bannerImagePager = findViewById<View>(R.id.bannerImageViewPager) as ImageView

        bannerImagePager!!.visibility = View.GONE

        headermanager = HeaderManager(this@AccreditationsActivity, NaisClassNameConstants.ABOUT_US)
        headermanager!!.getHeader(relativeHeader!!, 1 )
        back = headermanager!!.getLeftButton()
        headermanager!!.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
//        mTermsCalendarListView!!.onItemClickListener = AdapterView.OnItemClickListener()
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
        Glide.with(mContext!!).load(AppUtils.replace(bannner_img!!)).centerCrop()
            .into(bannerImagePager!!)

        val recyclerViewAdapter = AccreditationsRecyclerViewAdapter(mContext!!, mAboutUsListArray!!)
//        mTermsCalendarListView!!.adapter = recyclerViewAdapter
    }
}