package fr.flyingsquirrels.starring.utils

import android.annotation.TargetApi
import android.content.res.Resources
import android.os.Build
import android.support.design.widget.AppBarLayout


val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

class AppbarElevationOffsetListener : AppBarLayout.OnOffsetChangedListener {
    private var mTargetElevation: Float = 0f

    init {
        mTargetElevation = 8.dpToPx.toFloat()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val absOffset = Math.abs(offset)
        if (absOffset < appBarLayout.totalScrollRange) {
            appBarLayout.elevation = 0f
        } else {
            appBarLayout.elevation = mTargetElevation
        }

    }
}