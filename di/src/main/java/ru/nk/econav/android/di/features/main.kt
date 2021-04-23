package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.ecoplaces.api.EcoPlacesComponent
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.main.impl.MainComponentImpl
import ru.nk.econav.android.routing.api.RoutingComponent

val moduleMain = module {

    single<MainComponent.Factory> {
        MainComponent.Factory { appComponentContext, dependencies ->
            MainComponentImpl(
                appComponentContext = appComponentContext,
                deps = dependencies,
                children = object : MainComponent.Children {
                    override val routing: RoutingComponent.Factory = get()
                    override val ecoPlaces: EcoPlacesComponent.Factory = get()
                    override val ecoParamElector: EcoParamElector.Factory = get()
                })
        }
    }

}