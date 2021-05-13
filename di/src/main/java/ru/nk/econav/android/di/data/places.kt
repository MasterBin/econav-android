package ru.nk.econav.android.di.data

import org.koin.dsl.module
import ru.nk.econav.android.data.places.api.PlacesRepository
import ru.nk.econav.android.data.places.impl.PlacesRepositoryImpl


val moduleDataPlaces = module {

    single<PlacesRepository> {
        PlacesRepositoryImpl(get())
    }

}