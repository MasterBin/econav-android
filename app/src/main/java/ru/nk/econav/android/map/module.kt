package ru.nk.econav.android.map

import com.arkivanov.decompose.ComponentContext
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.nk.econav.android.BuildConfig
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
val testModule = module {

    single<PathRepository> { PathRepositoryImpl(get()) }

    single {
        OkHttpClient.Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor()
                        .also { it.setLevel(HttpLoggingInterceptor.Level.BODY) })
            }
        }
            .readTimeout(1, TimeUnit.HOURS)
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("http://192.168.1.50:8080/")
            .build()
    }

    factory<MainMap> { (componentContext: ComponentContext) ->
        MainMapImpl(componentContext, get())
    }

    factory {
        get<Retrofit>().create(Api::class.java)
    }
}