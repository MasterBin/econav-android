package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.features.place_details.api.PlaceDetailsComponent
import ru.nk.econav.android.features.place_details.impl.PlaceDetailsComponentImpl
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.userlocation.UserLocationComponent

val modulePlaceDetails = module {

    single<PlaceDetailsComponent.Factory> {
        PlaceDetailsComponent.Factory { appComponentContext, deps ->
            PlaceDetailsComponentImpl(
                appComponentContext = appComponentContext,
                deps = deps,
                children = object : PlaceDetailsComponent.Children {
                    override val userLocation: UserLocationComponent.Factory = get()
                    override val ecoParamElector: EcoParamElector.Factory = get()
                    override val routing: RoutingComponent.Factory = get()
                }
            )
        }
    }

}