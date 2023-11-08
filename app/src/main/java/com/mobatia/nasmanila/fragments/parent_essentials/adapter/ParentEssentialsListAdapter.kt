package com.mobatia.nasmanila.fragments.parent_essentials.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.nasmanila.R
import com.mobatia.nasmanila.fragments.parent_essentials.model.ParentEssentialsModel

class ParentEssentialsListAdapter(
    mContext: Context?,
    newsLetterModelArrayList: ArrayList<ParentEssentialsModel>
) : RecyclerView.Adapter<ParentEssentialsListAdapter.MyViewHolder>() {
    private val mContext: Context? = null
    private val mnNewsLetterModelArrayList: ArrayList<ParentEssentialsModel>? = null
    var dept: String? = null
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var mTitleTxt: TextView? = null
        init {
            mTitleTxt = view.findViewById<View>(R.id.listTxtTitle) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_about_us_list_adapter, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitleTxt!!.text = mnNewsLetterModelArrayList!![position].title
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