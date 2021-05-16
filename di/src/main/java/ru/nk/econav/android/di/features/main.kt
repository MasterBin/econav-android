package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.main.impl.MainComponentImpl
import ru.nk.econav.android.features.place_details.api.PlaceDetailsComponent
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.userlocation.UserLocationComponent

val moduleMain = module {

    single<MainComponent.Factory> {
        MainComponent.Factory { appComponentContext, dependencies ->
            MainComponentImpl(
                appComponentContext = appComponentContext,
                deps = dependencies,
                children = object : MainComponent.Children {
                    override val general: GeneralComponent.Factory = get()
                    override val placeDetails: PlaceDetailsComponent.Factory = get()
                })
        }
    }

}