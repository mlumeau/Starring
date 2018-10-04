package fr.flyingsquirrels.starring

import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import fr.flyingsquirrels.starring.utils.dpToPx
import kotlinx.android.synthetic.main.fragment_tabs.*

abstract class BaseTabsFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    protected fun init(adapter: FragmentStatePagerAdapter) {
        pager.adapter = adapter
        pager.currentItem = 1

        tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(pager){

            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0 && context!=null) {
                    val ta = context!!.obtainStyledAttributes(kotlin.intArrayOf(R.attr.tabSelectedTextColor))
                    val selectedColor = ta.getResourceId(0, android.R.color.black)
                    ta.recycle()
                    tab.icon?.setColorFilter(ContextCompat.getColor(context!!, selectedColor), PorterDuff.Mode.SRC_IN)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    tab.icon?.setColorFilter(ContextCompat.getColor(context!!, R.color.grey600), PorterDuff.Mode.SRC_IN)
                }
            }
        })

        tabs.setupWithViewPager(pager)
        tabs.getTabAt(0)?.icon = ContextCompat.getDrawable(context!!,R.drawable.ic_star_black_24dp)
        tabs.getTabAt(0)?.icon?.setColorFilter(ContextCompat.getColor(context!!, R.color.grey600), PorterDuff.Mode.SRC_IN)

    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {

        var isElevated = false

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val offset = recyclerView.computeVerticalScrollOffset()
            if (tabs != null && !isElevated && offset > 0) {
                val tx = ValueAnimator.ofFloat(0f, 8.dpToPx.toFloat())
                val mDuration = 200 //in millis
                tx.duration = mDuration.toLong()
                tx.addUpdateListener { animation -> tabs.elevation = animation.animatedValue as Float }
                tx.start()
                isElevated = true
            } else if (isElevated && offset <= 0) {
                val tx = ValueAnimator.ofFloat(8.dpToPx.toFloat(), 0f)
                val mDuration = 200 //in millis
                tx.duration = mDuration.toLong()
                tx.addUpdateListener { animation -> tabs.elevation = animation.animatedValue as Float }
                tx.start()
                isElevated = false
            }
        }
    }
}