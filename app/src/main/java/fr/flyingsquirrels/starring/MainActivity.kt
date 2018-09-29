package fr.flyingsquirrels.starring

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        nav.setupWithNavController(navController)

        //setupActionBarWithNavController(navController)

    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.nav_host_fragment).navigateUp()



}

