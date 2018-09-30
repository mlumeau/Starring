package fr.flyingsquirrels.starring

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_TAB = "selected"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        nav.setupWithNavController(navController)

        //setupActionBarWithNavController(navController)

        ViewCompat.setTransitionName(nav, DetailActivity.EXTRA_SHARED_NAV)
        intent?.extras?.getInt(SELECTED_TAB)?.let {
            nav.selectedItemId = it
            intent.extras?.clear()


        }
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.nav_host_fragment).navigateUp()



}

