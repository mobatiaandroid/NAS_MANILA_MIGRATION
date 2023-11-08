package com.mobatia.nasmanila.manager.recyclermanager

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(context: Context, offset: Int): RecyclerView.ItemDecoration() {
    var mContext = context
    var mOffset = offset
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[mOffset, mOffset, mOffset] = mOffset
    }
}