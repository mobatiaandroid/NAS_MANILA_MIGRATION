package com.mobatia.nasmanila.custom_views.custom_text

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.mobatia.nasmanila.R

class CustomFontSansProTextBlack : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {
        val type = Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Regular.otf")
        this.typeface = type
        this.setTextColor(context.resources.getColor(R.color.black))
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val type = Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Regular.otf")
        this.typeface = type
        this.setTextColor(context.resources.getColor(R.color.black))
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        val type = Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Regular.otf")
        this.typeface = type
        this.setTextColor(context.resources.getColor(R.color.black))
    }
}