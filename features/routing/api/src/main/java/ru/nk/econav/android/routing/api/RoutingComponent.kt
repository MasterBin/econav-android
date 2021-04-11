package ru.nk.econav.android.routing.api

import kotlinx.coroutines.flow.Flow
import ru.nk.econav.android.routing.api.model.Route
import ru.nk.econav.android.routing.api.model.RouteRequest
import ru.nk.econav.core.common.decopmose.AppComponentContext
import ru.nk.econav.core.common.util.OutEvent

interface RoutingComponent {

    fun requestRoute(request: RouteRequest)

    interface Dependencies {
        val route : OutEvent<Route>
    }

    fun interface Factory {
        fun invoke(componentContext: AppComponentContext, deps: Dependencies) : RoutingComponent
    }
}