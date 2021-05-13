package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.android.general.impl.GeneralComponentImpl
import ru.nk.econav.android.userlocation.UserLocationComponent

val moduleGeneral = module {

    single<GeneralComponent.Factory> {
        GeneralComponent.Factory { appComponentContext, deps ->
            GeneralComponentImpl(
                appComponentContext = appComponentContext,
                deps = deps,
                children = object : GeneralComponent.Children {
                    override val userLocation: UserLocationComponent.Factory = get()
                    override val searchPlaces: SearchPlacesComponent.Factory = get()
                }
            )
        }
    }

}