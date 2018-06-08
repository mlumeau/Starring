package fr.flyingsquirrels.starring.utils

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NavViewPager : ViewPager {

        constructor(context : Context) : super(context)

        constructor(context: Context, attrs : AttributeSet) : super(context, attrs)

        override fun onInterceptTouchEvent(event : MotionEvent) = false

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(ev: MotionEvent?) = false
}