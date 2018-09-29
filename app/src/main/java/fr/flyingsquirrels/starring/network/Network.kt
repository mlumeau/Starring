package fr.flyingsquirrels.starring.network

import fr.flyingsquirrels.starring.BuildConfig
import fr.flyingsquirrels.starring.network.Parameters.CACHE_SIZE
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


/**
 * Created by mlumeau on 26/02/2018.
 */

object Parameters{
    const val CACHE_SIZE: Long = 10*1024*1024
}

val NetworkModule : Module = applicationContext{


    bean{


        val builder = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(
                            chain.request().newBuilder().url(
                                    chain.request().url().newBuilder()
                                            .addQueryParameter("api_key", BuildConfig.api_key)
                                            .addQueryParameter("language", "${Locale.getDefault().language}-${Locale.getDefault().country}")
                                            .addQueryParameter("include_image_language", "${Locale.getDefault().language},null")
                                            .addQueryParameter("include_adult", "false")
                                            .build()
                            ).build())
                }
                .cache(Cache(this.androidApplication().cacheDir, CACHE_SIZE))

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }

        builder.build()
    }

    bean{
        Retrofit.Builder()
                .baseUrl(BuildConfig.base_url)
                .client(get())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    bean{
        val retrofit: Retrofit = get()
        retrofit
                .create(TMDBRetrofitService::class.java)
    }

}
