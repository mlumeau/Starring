package fr.flyingsquirrels.starring

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val FRAG_MOVIES = "movies_frag"
        const val FRAG_TV = "tv_frag"
        const val FRAG_PEOPLE = "people_frag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.movies -> loadFragByTag(FRAG_MOVIES)
                R.id.tv_shows -> loadFragByTag(FRAG_TV)
            }
            true
        }

        loadFragByTag(FRAG_MOVIES)

    }

    private fun loadFragByTag(tag : String) {
        var frag = supportFragmentManager.findFragmentByTag(tag)
        if (frag == null) {
            when (tag) {
                FRAG_MOVIES -> {
                    frag = MovieTabsFragment()
                }
                FRAG_TV -> {
                    frag = TVTabsFragment()
                }
            }

        }

        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.tabs_container, frag, tag)
                .commit()
    }

}

