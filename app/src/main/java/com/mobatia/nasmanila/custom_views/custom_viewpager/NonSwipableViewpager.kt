package com.mobatia.nasmanila.custom_views.custom_viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager

class NonSwipableViewpager: ViewPager {
    constructor(context: Context) : super(context) {
        setMyScroller()
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setMyScroller()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun setMyScroller() {
        val viewpager: Class<*> = ViewPager::class.java
        val scroller = viewpager.getDeclaredField("mScroller")
        scroller.isAccessible = true
        scroller[this] = MyScroller(context)
    }

    class MyScroller(context: Context?): Scroller(context) {
        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/)
        }
    }
}