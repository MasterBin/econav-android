package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.ecoplaces.api.EcoPlacesComponent
import ru.nk.econav.android.ecoplaces.impl.EcoPlacesComponentImpl


val moduleEcoPlaces = module {

    single {
        EcoPlacesComponent.Factory { componentContext, deps ->
            EcoPlacesComponentImpl(
                componentContext = componentContext,
                deps = deps,
                networking = get()
            )
        }
    }


}