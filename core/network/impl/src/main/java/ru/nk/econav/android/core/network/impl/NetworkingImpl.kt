package ru.nk.econav.android.core.network.impl

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.nk.econav.android.core.network.api.Networking

class NetworkingImpl : Networking {

    private val retrofit : Retrofit = createRetrofit()

    override fun <T> createApi(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun createRetrofit() : Retrofit =
    Retrofit.Builder()
        .client(createOkhttpClient())
        .baseUrl("http://192.168.2.82:8080/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

fun createOkhttpClient() : OkHttpClient =
    OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT).apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }
        .build()
