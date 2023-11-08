package com.mobatia.nasmanila.custom_views.custom_imageview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class RoundedImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {


    fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap? {
        var output: Bitmap? = null
        if (radius > 0) {
            val sbmp: Bitmap
            sbmp = if (bmp.width != radius || bmp.height != radius) Bitmap.createScaledBitmap(
                bmp,
                radius,
                radius,
                false
            ) else bmp
            output = Bitmap.createBitmap(
                sbmp.width, sbmp.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, sbmp.width, sbmp.height)
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.parseColor("#BAB399")
            canvas.drawCircle(
                sbmp.width / 2 + 0.7f,
                sbmp.height / 2 + 0.7f, sbmp.width / 2 + 0.1f,
                paint
            )
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(sbmp, rect, rect, paint)
        }
        return output
    }
}