package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.routing.impl.RoutingComponentImpl

val moduleRouting = module {

    single {
        RoutingComponent.Factory { componentContext, deps ->
            RoutingComponentImpl(componentContext, deps, get())
        }
    }
}