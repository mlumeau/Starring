package com.flyingsquirrels.starring

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by mlumeau on 02/03/2018.
 */

class DetailActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setActionBar(toolbar)

        toolbar.title = "Hello world"
    }
}
