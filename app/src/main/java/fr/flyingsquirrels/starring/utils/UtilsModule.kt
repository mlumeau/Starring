package fr.flyingsquirrels.starring.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext


val UtilsModule : Module = applicationContext{

    bean {
        Gson()
    }

}

inline fun <reified T> Gson.fromJson(json: String): T = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

typealias AndroidPair<T,U> = androidx.core.util.Pair<T,U>