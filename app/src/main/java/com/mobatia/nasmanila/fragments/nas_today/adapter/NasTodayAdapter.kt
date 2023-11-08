package com.mobatia.nasmanila.fragments.nas_today.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.activities.web_view.LoadUrlWebViewActivity
import com.mobatia.nasmanila.fragments.nas_today.model.NasTodayModel
import java.util.regex.Pattern

class NasTodayAdapter(activity: Activity?, mListViewArray: ArrayList<NasTodayModel>?) : BaseAdapter() {
    private val mContext: Context? = null

    private val mNasTodayList: ArrayList<NasTodayModel>? = null
    private var view: View? = null
    private val mTitleTxt: TextView? = null
    private val mImageView: ImageView? = null
    private val mTitle: String? = null
    private val mTabId: String? = null
    var mViewHolder: NasTodayAdapter.ViewHolder? = null

    class ViewHolder {
        var listTxtTitle: TextView? = null
        var listTxtDate: TextView? = null
        var listTxtDescription: TextView? = null
        var readMoreTextView: TextView? = null
    }

    override fun getCount(): Int {
        return mNasTodayList!!.size
    }

    override fun getItem(position: Int): Any {
        return mNasTodayList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (convertView == null) {
            val inflate = LayoutInflater.from(mContext)
            view = inflate.inflate(R.layout.custom_nastoday_list_adapter, null)
            mViewHolder = ViewHolder()
//            convertView = inflate.inflate(R.layout.custom_nastoday_list_adapter, null)
            mViewHolder!!.listTxtTitle =
                view!!.findViewById<View>(R.id.listTxtTitle) as TextView?
            mViewHolder!!.listTxtDate =
                view!!.findViewById<View>(R.id.listTxtDate) as TextView?
            mViewHolder!!.listTxtDescription =
                view!!.findViewById<View>(R.id.listTxtDescription) as TextView?
            mViewHolder!!.readMoreTextView =
                view!!.findViewById<View>(R.id.readMoreTextView) as TextView?
            view!!.tag = mViewHolder
        } else {
            mViewHolder = convertView.tag as ViewHolder
        }
        mViewHolder!!.listTxtTitle!!.text = mNasTodayList!![position].title!!.trim { it <= ' ' }
        mViewHolder!!.listTxtDescription!!.text =
            mNasTodayList[position].description!!.trim { it <= ' ' }
        mViewHolder!!.listTxtDate!!.text = mNasTodayList[position].day+ "-" + mNasTodayList[position].month + "-" + mNasTodayList[position].year + " " + mNasTodayList[position].time
        mViewHolder!!.readMoreTextView!!.setOnClickListener {
            var webViewComingUpDetail: String = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head><meta name=\"viewport\" content=\"user-scalable=no, \n" +
                    "initial-scale=1.0, maximum-scale=1.0, width=device-width\">" +
                    "<style>\n" +
                    "\n" +
                    "@font-face {\n" +
                    "font-family: SourceSansPro-Semibold;" +
                    "src: url(SourceSansPro-Semibold.ttf);" +

                    "font-family: SourceSansPro-Regular;" +
                    "src: url(SourceSansPro-Regular.ttf);" +
                    "}" +
                    ".title {" +
                    "font-family: SourceSansPro-Regular;" +
                    "font-size:16px;" +
                    "text-align:left;" +
                    "color:	#46C1D0;" +
                    "}" +
                    "a {" +
                    "font-family: SourceSansPro-Regular;" +
                    "color:	#46C1D0;"+
                    "}" +
                    ".date {" +
                    "font-family: SourceSansPro-Regular;" +
                    "font-size:12px;" +
                    "text-align:left;" +
                    "color:	#46C1D0;" +
                    "}" +
                    ".description {"+
                    "font-family: SourceSansPro-Light;"+
                    "src: url(SourceSansPro-Light.otf);"+
                    "font-size:14px;" +
                    "word-wrap:break-word;"+
                    "color: #808080;" +//black removed
                    "}" +
                    "</style>\n" + "</head>" +
                    "<body>" +
                    "<p class='title'>" + mNasTodayList[position].title + "</p>" +
                    "<p class='date'>" + mNasTodayList[position].day + "-" + mNasTodayList[position].month + "-" + mNasTodayList[position].year +
                    " " + mNasTodayList[position].time + "</p>"
            if (!mNasTodayList[position].image.equals("", ignoreCase = true)) {
                webViewComingUpDetail =
                    webViewComingUpDetail + "<center><img src='" + mNasTodayList[position].image + "'width='100%', height='auto'></center>"
            }
            var desc = ""
            desc = mNasTodayList[position].description!!.replace(" ".toRegex(), "&nbsp;")
            var pullLinks: List<String> = ArrayList()
            pullLinks = pullLinks(mNasTodayList[position].description)
            if (pullLinks.isNotEmpty()) {
                for (i in pullLinks.indices) {
                    desc = desc.replace(
                        pullLinks[i].toRegex(),
                        "<a href=\"" + pullLinks[i] + "\">" + pullLinks[i] + " </a>"
                    )
                }
            } else {
                desc = mNasTodayList[position].description!!.replace(" ".toRegex(), "&nbsp;")
            }
            println("decription::$desc")
            webViewComingUpDetail = """
                $webViewComingUpDetail<p class='description'>${
                desc.replace(
                    "\n".toRegex(),
                    "<br>"
                )
            }</p></body>
                </html>
                """.trimIndent()
            val mIntent = Intent(mContext, LoadUrlWebViewActivity::class.java)
            println("result is::" + mNasTodayList[position].image)
            mIntent.putExtra("webViewComingDetail", webViewComingUpDetail)
            Log.v("detail:: ", webViewComingUpDetail)
            Log.v("desc:: ", mNasTodayList[position].description!!)
            mIntent.putExtra("pdf", mNasTodayList[position].pdf)
            mContext!!.startActivity(mIntent)
        }

        return convertView!!
    }

    private fun pullLinks(text: String?): List<String> {

        val links: MutableList<String> = ArrayList()
        val regex =
            "\\(?\\b(https?://|http?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
        val p = Pattern.compile(regex)
        val m = p.matcher(text)

        while (m.find()) {
            var urlStr = m.group()
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length - 1)
            }

//            links=urlStr;
            if (!links.contains(urlStr)) links.add(urlStr)
        }

        return links
    }
}