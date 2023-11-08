package com.mobatia.nasmanila.common.common_classes

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.mobatia.nasmanila.R
import java.io.Serializable

class HeaderManager: Serializable {
    private var context: Activity? = null

    private var inflator: LayoutInflater? = null

    var headerView: View? = null

    private var mHeading1: TextView? = null

    private var relativeParams: RelativeLayout.LayoutParams? = null

    private var heading1: String? = null

    private val isCancel = false

    private var edtText: EditText? = null

    private var isHome = false

    private var mLeftText: TextView? = null
    private  var mRightText: TextView? = null

    private var mLeftImage: ImageView? = null
    private  var mRightImage:android.widget.ImageView? = null
    private  var mRight:android.widget.ImageView? = null
    private  var mLeft:android.widget.ImageView? = null
    private  var logoClickImgView:android.widget.ImageView? = null
    var btn_info: ImageView? = null


    constructor(context: Activity?, heading1: String?) {
        setContext(context)
        inflator = LayoutInflater.from(context)
        this.heading1 = heading1
    }


    fun HeaderManager(home: Boolean, context: Activity?) {
        setContext(context)
        inflator = LayoutInflater.from(context)
        isHome = home
    }


    fun getLeftText(): TextView? {
        return mLeftText
    }

    fun setLeftText(mLeftText: TextView?) {
        this.mLeftText = mLeftText
    }


    fun getRightText(): TextView? {
        return mRightText
    }


    fun setRightText(mLeftText: TextView) {
        this.mRightText = mLeftText
    }


    fun getLeftImage(): ImageView? {
        return mLeftImage
    }


    fun setLeftImage(mLeftImage: ImageView?) {
        this.mLeftImage = mLeftImage
    }


    fun getRightImage(): ImageView? {
        return mLeftImage
    }

    fun getRightInfoImage(): ImageView? {
        return btn_info
    }


    fun setRightImage(mLeftImage: ImageView) {
        this.mRightImage = mLeftImage
    }


    fun setVisible(v: View?) {
        v!!.visibility = View.VISIBLE
    }


    fun setInvisible() {
        headerView!!.visibility = View.INVISIBLE
    }


    fun setInvisible(v: View) {
        v.visibility = View.INVISIBLE
    }


    fun getHeader(headerHolder: LinearLayout, type: Int): Int {
        initializeUI(type)
        relativeParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeParams!!.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        headerHolder.addView(headerView, relativeParams)
        return headerView!!.id
    }


    fun getHeader(
        headerHolder: RelativeLayout, getHeading: Boolean,
        type: Int
    ): Int {
        initializeUI(getHeading, type)
        relativeParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeParams!!.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        headerHolder.addView(headerView, relativeParams)
        return headerView!!.id
    }


    private fun initializeUI(type: Int) {
        inflator = LayoutInflater.from(getContext())
        headerView = inflator!!.inflate(R.layout.common_header_single, null)
        val logoHeader = headerView!!
            .findViewById<View>(R.id.relative_logo_header) as RelativeLayout
        btn_info = headerView!!
            .findViewById<View>(R.id.btn_info) as ImageView
        if (type == 0) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // two
            btn_info!!.visibility = View.GONE
            // buttons
        } else if (type == 1) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // left
            btn_info!!.visibility = View.GONE
            // button
        } else if (type == 3) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // left
            btn_info!!.visibility = View.VISIBLE
            // button
        }
        mHeading1 = headerView!!.findViewById<View>(R.id.heading) as TextView
        mHeading1!!.text = heading1
        mHeading1!!.setBackgroundColor(context!!.resources.getColor(R.color.split_bg))
        mRight = headerView!!.findViewById<View>(R.id.btn_right) as ImageView
        mLeft = headerView!!.findViewById<View>(R.id.btn_left) as ImageView
        logoClickImgView = headerView!!.findViewById<View>(R.id.logoClickImgView) as ImageView
    }


    private fun initializeUI(getHeading: Boolean, type: Int) {
        inflator = LayoutInflater.from(getContext())
        headerView = inflator!!.inflate(R.layout.common_header_single, null)
        val logoHeader = headerView!!
            .findViewById<View>(R.id.relative_logo_header) as RelativeLayout
        btn_info = headerView!!
            .findViewById<View>(R.id.btn_info) as ImageView
        if (type == 0) {
            logoHeader.setBackgroundResource(R.drawable.titlebar)
            btn_info!!.visibility = View.GONE
        } else if (type == 1) {
            logoHeader.setBackgroundResource(R.drawable.titlebar)
            mHeading1 = headerView!!.findViewById<View>(R.id.heading) as TextView
            mHeading1!!.visibility = View.GONE
            btn_info!!.visibility = View.GONE
        } else if (type == 3) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // left
            btn_info!!.visibility = View.VISIBLE
            // button
        } /*
		 * else if(type==2) {
		 * logoHeader.setBackgroundResource(R.drawable.titlebars); }
		 */


//		edtText = (EditText) headerView.findViewById(R.id.edtTxt_Header);
        mHeading1 = headerView!!.findViewById<View>(R.id.heading) as TextView
        mHeading1!!.text = heading1
        mHeading1!!.setBackgroundColor(context!!.resources.getColor(R.color.split_bg))
        mRight = headerView!!.findViewById<View>(R.id.btn_right) as ImageView
        mLeft = headerView!!.findViewById<View>(R.id.btn_left) as ImageView
        logoClickImgView = headerView!!.findViewById<View>(R.id.logoClickImgView) as ImageView
    }


    fun setTitleBar(titleBar: Int) {
        headerView!!.setBackgroundResource(titleBar)
    }

    fun setTitle(title: String?) {
        mHeading1!!.text = title
    }


    fun getLeftButton(): ImageView? {
        return mLeft
    }

    fun getLogoButton(): ImageView? {
        return logoClickImgView
    }


    fun setLeftButton(right: ImageView) {
        this.mLeft = right
    }


    fun getRightButton(): ImageView? {
        return mRight
    }

    fun getEditText(): EditText? {
        mHeading1!!.visibility = View.GONE
        edtText!!.visibility = View.VISIBLE
        return edtText
    }


    fun setEditText(editText: EditText?) {
        edtText = editText
        setVisible(edtText)
    }


    fun setRightButton(right: ImageView) {
        this.mRight = right
    }


    fun setButtonRightSelector(
        normalStateResID: Int,
        pressedStateResID: Int
    ) {
        if (normalStateResID == R.drawable.settings) {
            mRight!!.setBackgroundResource(R.drawable.settings)
        }

        mRight!!.setImageDrawable(
            getButtonDrawableByScreenCathegory(
                normalStateResID, pressedStateResID
            )
        )
        setVisible(mRight)
    }


    fun setButtonLeftSelector(
        normalStateResID: Int,
        pressedStateResID: Int
    ) {
        mLeft!!.setImageDrawable(
            getButtonDrawableByScreenCathegory(
                normalStateResID, pressedStateResID
            )
        )
        setVisible(mLeft)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    fun getButtonDrawableByScreenCathegory(
        normalStateResID: Int,
        pressedStateResID: Int
    ): Drawable? {
        val stateNormal = context!!.resources
            .getDrawable(normalStateResID).mutate()
        val statePressed = context!!.resources
            .getDrawable(pressedStateResID).mutate()
        val drawable = StateListDrawable()
        drawable.addState(
            intArrayOf(R.attr.state_pressed),
            statePressed
        )
        drawable.addState(
            intArrayOf(R.attr.state_enabled),
            stateNormal
        )
        return drawable
    }


    private fun setContext(context: Activity?) {
        this.context = context
    }


    private fun getContext(): Activity? {
        return context
    }
}