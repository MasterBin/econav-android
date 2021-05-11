package ru.nk.econav.android.di.data

import org.koin.dsl.module
import ru.nk.econav.android.data.routing.api.RoutingRepository
import ru.nk.econav.android.data.routing.impl.RoutingRepositoryImpl

val moduleDataRouting = module {

    single<RoutingRepository> {
        RoutingRepositoryImpl(get())
    }

}