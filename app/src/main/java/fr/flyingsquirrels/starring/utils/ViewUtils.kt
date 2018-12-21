package fr.flyingsquirrels.starring.utils

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import fr.flyingsquirrels.starring.R


fun ViewGroup.inflate(adapter_layout: Int, attachToRoot: Boolean = false): View? {
    return LayoutInflater.from(context).inflate(adapter_layout,this, attachToRoot)
}

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


fun View.rotate180(){
    val anim: Animation = if(this@rotate180.tag != "rotated") {
        this@rotate180.tag = "rotated"
        AnimationUtils.loadAnimation(this.context, R.anim.rotate_to_180)
    }
    else {
        this@rotate180.tag = null
        AnimationUtils.loadAnimation(this.context, R.anim.rotate_to_0)
    }


    this.startAnimation(anim)
}