package com.flyingsquirrels.starring

import android.app.Application
import org.koin.android.ext.android.startKoin
/**
 * Created by mlumeau on 26/02/2018.
 */

class StarringApp : Application(){


    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(NetworkModule))

    }
}