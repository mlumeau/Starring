package fr.flyingsquirrels.starring.utils

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.res.Resources
import android.os.Build
import android.support.design.widget.AppBarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun ViewGroup.inflate(adapter_layout: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(adapter_layout,this, attachToRoot)
}

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

class AppbarElevationOffsetListener : AppBarLayout.OnOffsetChangedListener {
    private var isElevated = false
    private var mTargetElevation: Float = 0f

    init {
        mTargetElevation = 8.dpToPx.toFloat()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val absOffset = Math.abs(offset)
        if (isElevated && absOffset < appBarLayout.totalScrollRange) {

            val tx = ValueAnimator.ofFloat(mTargetElevation, 0f)
            val mDuration = 300 //in millis
            tx.duration = mDuration.toLong()
            tx.addUpdateListener { animation -> appBarLayout.elevation = animation.animatedValue as Float }
            tx.start()

            isElevated = false
        } else if(!isElevated && absOffset == appBarLayout.totalScrollRange){
            appBarLayout.elevation = mTargetElevation
            isElevated = true
        }

    }
}