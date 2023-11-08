package com.mobatia.nasmanila.fragments.about_us.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.about_us.model.AboutUsModel

class FacilityRecyclerAdapter(mContext: Context, mAboutUsListArray: ArrayList<AboutUsModel>?) : RecyclerView.Adapter<FacilityRecyclerAdapter.MyViewHolder>() {
    private val mContext: Context? = null
    private val mnNewsLetterModelArrayList: ArrayList<AboutUsModel>? = null
    var dept: String? = null
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageIcon: ImageView? = null
        var pdfTitle: TextView? = null
        init {
            imageIcon = itemView.findViewById<View?>(R.id.imageIcon) as android.widget.ImageView?
            pdfTitle = itemView.findViewById<View?>(R.id.pdfTitle) as TextView?
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_pdf_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.pdfTitle!!.text = mnNewsLetterModelArrayList!![position].itemTitle

        if (mnNewsLetterModelArrayList!![position].itemPdfUrl!!.endsWith(".pdf")) {
            holder.imageIcon!!.setBackgroundResource(R.drawable.pdfdownloadbutton)
        } else {
            holder.imageIcon!!.setBackgroundResource(R.drawable.webcontentviewbutton)
        }
    }

    override fun getItemCount(): Int {
        return mnNewsLetterModelArrayList!!.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
