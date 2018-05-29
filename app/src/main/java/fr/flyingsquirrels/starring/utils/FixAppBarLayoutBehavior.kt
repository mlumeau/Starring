/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.flyingsquirrels.starring.utils


import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Workaround AppBarLayout.Behavior for https://issuetracker.google.com/66996774
 *
 * See https://gist.github.com/chrisbanes/8391b5adb9ee42180893300850ed02f2 for
 * example usage.
 *
 * Change the package name as you wish.
 */
class FixAppBarLayoutBehavior : AppBarLayout.Behavior {
    private var isPositive: Boolean = false

    constructor() : super() {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}


    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        stopNestedScrollIfNeeded(dyUnconsumed, child, target, type)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        stopNestedScrollIfNeeded(dy, child, target, type)
    }

    private fun stopNestedScrollIfNeeded(dy: Int, child: AppBarLayout, target: View?, type: Int) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            val currOffset = topAndBottomOffset
            if (dy < 0 && currOffset == 0 || dy > 0 && currOffset == -child.totalScrollRange) {
                ViewCompat.stopNestedScroll(target!!, ViewCompat.TYPE_NON_TOUCH)
            }
        }
    }


    override fun onNestedFling(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        var newVelocityY = velocityY
        var newConsumed = consumed
        if (newVelocityY > 0 && !isPositive || newVelocityY < 0 && isPositive) {
            newVelocityY *= -1
        }
        if (target is RecyclerView && newVelocityY < 0) {
            val firstChild = target.getChildAt(0)
            val childAdapterPosition = target.getChildAdapterPosition(firstChild)
            newConsumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, newVelocityY, newConsumed)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed)
        isPositive = dy > 0
    }

    companion object {

        private const val TOP_CHILD_FLING_THRESHOLD = 3
    }
}