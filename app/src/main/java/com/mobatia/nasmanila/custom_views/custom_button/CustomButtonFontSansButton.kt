package com.mobatia.nasmanila.custom_views.custom_button

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class CustomButtonFontSansButton(context: Context, attributeSet: AttributeSet) :
    androidx.appcompat.widget.AppCompatButton(context, attributeSet) {
    var FONT_NAME: Typeface? = null

    init {
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.assets, "fonts/SourceSansPro-Regular.otf")
        this.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        this.typeface = FONT_NAME
    }
}