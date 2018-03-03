package com.flyingsquirrels.starring

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


/**
 * Created by mlumeau on 26/02/2018.
 */

@Module
class NetworkModule(private val baseUrl: String, private val apiKey: String, private val lang:String, private val cacheDir:File) {

    companion object {
        const val CACHE_SIZE: Long = 10*1024*1024
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor({ chain -> chain.proceed(
                chain.request().newBuilder().url(
                        chain.request().url().newBuilder()
                                .addQueryParameter("api_key", apiKey)
                                .addQueryParameter("language", lang)
                                .build()
                ).build())
            }).cache(Cache(cacheDir, CACHE_SIZE))
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


    @Provides
    @Singleton
    fun provideTMDB(retrofit: Retrofit): TMDBRetrofitService{
        return retrofit.create(TMDBRetrofitService::class.java)
    }

}