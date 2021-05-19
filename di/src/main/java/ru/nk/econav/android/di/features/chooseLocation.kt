package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.features.choose_location.api.ChooseLocationComponent
import ru.nk.econav.android.features.choose_location.impl.ChooseLocationComponentImpl
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent

val moduleChooseLocation = module {

    single {
        ChooseLocationComponent.Factory { appComponentContext, deps ->
            ChooseLocationComponentImpl(
                appComponentContext = appComponentContext,
                deps = deps,
                children = object : ChooseLocationComponent.Children {
                    override val searchPlaces: SearchPlacesComponent.Factory = get()
                }
            )
        }
    }

}