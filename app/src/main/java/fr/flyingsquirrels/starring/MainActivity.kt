package fr.flyingsquirrels.starring

import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        nav.setOnNavigationItemSelectedListener {
            tabs_container.currentItem = when(it.itemId){
                R.id.movies -> 0
                R.id.tv_shows -> 1
                else -> 0
            }
            true
        }

        tabs_container.adapter = BottomNavAdapter()

    }

    inner class BottomNavAdapter: FragmentStatePagerAdapter(supportFragmentManager){
        override fun getCount() = 2

        override fun getItem(position: Int) = when(position){
            0 -> MovieTabsFragment()
            1 -> TVTabsFragment()
            else -> null
        }

    }

}

