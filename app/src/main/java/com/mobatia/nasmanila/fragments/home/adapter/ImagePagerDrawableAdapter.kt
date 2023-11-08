package com.mobatia.nasmanila.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.common.common_classes.AppUtils

class ImagePagerDrawableAdapter(var context: Context?, var imagesArrayList: ArrayList<String>): PagerAdapter() {
    var iContext: Context? = context
    var iImagesArrayList: ArrayList<String> = imagesArrayList
//    private var mInflaters: LayoutInflater? = null

    override fun getCount(): Int {
        return iImagesArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pageView: View
        val mInflaters: LayoutInflater = LayoutInflater.from(iContext)
        pageView = mInflaters.inflate(R.layout.layout_imagepager_adapter, null)
        val imageView = pageView.findViewById<View>(R.id.adImg) as ImageView
        if (iImagesArrayList[position] != "") {
            if (AppUtils.replace(iImagesArrayList[position].toString()).startsWith("http") || AppUtils.replace(
                    iImagesArrayList[position].toString()
                ).startsWith("http")) {
                Glide.with(iContext!!).load(AppUtils.replace(iImagesArrayList[position].toString())).placeholder(
                    R.drawable.default_bannerr
                ).error(R.drawable.default_bannerr).centerCrop().into(imageView)
            } else {
                Glide.with(iContext!!).load(getImage(iImagesArrayList[position])).into(imageView)
            }
        } else {
            imageView.setImageResource(R.drawable.default_bannerr)
        }
        (container as ViewPager).addView(pageView, 0)

        return pageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    private fun getImage(imageName: String): Int {

        return iContext!!.resources.getIdentifier(imageName, "drawable", iContext!!.packageName)
    }
}