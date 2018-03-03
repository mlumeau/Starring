package com.flyingsquirrels.starring

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

/**
 * Created by mlumeau on 26/02/2018.
 */

class StarringApp : Application(){

    lateinit var network: NetworkComponent

    private val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)

        network = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule("https://api.themoviedb.org/3/",
                        BuildConfig.api_key,
                        "${Locale.getDefault().language}-${Locale.getDefault().country}",
                        cacheDir))
                .build()

    }
}

@Module
class AppModule(val app: StarringApp) {
    @Provides
    @Singleton
    fun provideApp() = app

}

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: StarringApp)
}