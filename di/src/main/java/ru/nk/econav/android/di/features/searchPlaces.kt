package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.features.searchplaces.impl.SearchPlacesComponentImpl

val moduleSearchPlaces = module {

    single<SearchPlacesComponent.Factory> {
        SearchPlacesComponent.Factory { appComponentContext, deps ->
            SearchPlacesComponentImpl(
                appComponentContext = appComponentContext,
                deps = deps,
                repository = get()
            )
        }
    }
}