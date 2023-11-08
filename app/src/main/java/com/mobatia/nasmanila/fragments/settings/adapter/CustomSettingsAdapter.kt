package com.mobatia.nasmanila.fragments.settings.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.common.common_classes.PreferenceManager

class CustomSettingsAdapter(activity: Activity, arrayList: java.util.ArrayList<String?>) : BaseAdapter() {

    private val mContext: Context = activity
    private val mSettingsList: java.util.ArrayList<String?> = arrayList
    private var view: View? = null
    private var mTitleTxt: TextView? = null
    private var mTxtUser: TextView? = null
    private val mImageView: ImageView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null

    override fun getCount(): Int {
        return mSettingsList!!.size
    }

    override fun getItem(position: Int): String? {


        return mSettingsList!![position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        view = if (convertView == null) {
            val inflate = LayoutInflater.from(mContext)
            inflate.inflate(R.layout.custom_settings_list_adapter, null)
        } else {
            convertView
        }
        try {
            mTitleTxt = view!!.findViewById<View>(R.id.listTxtTitle) as TextView
            mTxtUser = view!!.findViewById<View>(R.id.txtUser) as TextView
            mTitleTxt!!.text = mSettingsList[position].toString()
            if (PreferenceManager.getUserCode(mContext!!).equals("", ignoreCase = true)) {
                if (position == 4) {
                    mTxtUser!!.visibility = View.VISIBLE
                    mTitleTxt!!.text = mSettingsList[position].toString()
                    mTxtUser!!.text = "(" + "Guest" + ")"
                } else {
//                    mTitleTxt.setGravity(Gravity.CENTER_VERTICAL);
                    val lp = mTitleTxt!!.layoutParams as RelativeLayout.LayoutParams
                    lp.addRule(RelativeLayout.CENTER_VERTICAL)
                    mTitleTxt!!.layoutParams = lp
                }
            } else {
                if (position == 5) {
                    mTxtUser!!.visibility = View.VISIBLE
                    mTitleTxt!!.text = mSettingsList[position].toString()
                    mTxtUser!!.text = "(" + PreferenceManager.getUserEmail(mContext!!) + ")"
                } else {
                    val lp = mTitleTxt!!.layoutParams as RelativeLayout.LayoutParams
                    lp.addRule(RelativeLayout.CENTER_VERTICAL)
                    mTitleTxt!!.layoutParams = lp
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
//        if (convertView == null) {
//            val inflate = LayoutInflater.from(mContext)
//            view = inflate.inflate(R.layout.custom_settings_list_adapter, null)
//        } else {
//            view = convertView
//        }
//        mTitleTxt = view!!.findViewById<View>(R.id.listTxtTitle) as TextView
//        mTxtUser = view!!.findViewById<View>(R.id.txtUser) as TextView
//        mTitleTxt!!.text = mSettingsList!![position]
//        if (preferenceManager.getUserId(mContext!!).equals("", ignoreCase = true)) {
//            if (position == 4) {
//                mTxtUser!!.visibility = View.VISIBLE
//                mTitleTxt!!.text = mSettingsList[position]
//                mTxtUser!!.text = "(" + "Guest" + ")"
//            } else {
////                    mTitleTxt.setGravity(Gravity.CENTER_VERTICAL);
//                val lp = mTitleTxt!!.layoutParams as RelativeLayout.LayoutParams
//                lp.addRule(RelativeLayout.CENTER_VERTICAL)
//                mTitleTxt!!.layoutParams = lp
//            }
//        } else {
//            if (position == 5) {
//                mTxtUser!!.visibility = View.VISIBLE
//                mTitleTxt!!.text = mSettingsList[position]
//                mTxtUser!!.text = "(" + preferenceManager.getUserEmail(mContext).toString() + ")"
//            } else {
//                val lp = mTitleTxt!!.layoutParams as RelativeLayout.LayoutParams
//                lp.addRule(RelativeLayout.CENTER_VERTICAL)
//                mTitleTxt!!.layoutParams = lp
//            }
//        }
//        return convertView

    }
}