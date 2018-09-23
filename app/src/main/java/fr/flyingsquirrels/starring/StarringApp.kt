package fr.flyingsquirrels.starring

import android.app.Application
import fr.flyingsquirrels.starring.db.DatabaseModule
import fr.flyingsquirrels.starring.network.NetworkModule
import fr.flyingsquirrels.starring.utils.UtilsModule
import org.koin.android.ext.android.startKoin
/**
 * Created by mlumeau on 26/02/2018.
 */

class StarringApp : Application(){


    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(NetworkModule, DatabaseModule, UtilsModule))

    }
}