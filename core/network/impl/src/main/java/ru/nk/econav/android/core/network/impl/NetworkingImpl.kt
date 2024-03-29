package ru.nk.econav.android.core.network.impl

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.nk.econav.android.core.network.api.Networking
import java.util.concurrent.*

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
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType())
        )
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
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
        .readTimeout(1, TimeUnit.HOURS)
        .retryOnConnectionFailure(false)
        .build()
