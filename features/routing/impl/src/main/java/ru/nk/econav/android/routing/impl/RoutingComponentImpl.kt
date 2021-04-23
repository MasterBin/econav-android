package ru.nk.econav.android.routing.impl

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nk.econav.android.core.network.api.Networking
import ru.nk.econav.android.routing.api.RoutingComponent
import ru.nk.econav.android.routing.api.model.Route
import ru.nk.econav.android.routing.api.model.RouteRequest
import ru.nk.econav.android.routing.impl.data.Api
import ru.nk.econav.android.routing.impl.data.PathRepositoryImpl
import ru.nk.econav.core.common.decopmose.AppComponentContext
import ru.nk.econav.core.common.util.convert

class RoutingComponentImpl(
    private val componentContext: AppComponentContext,
    private val deps: RoutingComponent.Dependencies,
    private val networking: Networking
) : RoutingComponent, RoutingComponent.Dependencies by deps,
    AppComponentContext by componentContext {

    private val executor = instanceKeeper.getOrCreate { Executor(networking) }

    init {
        componentScope.launch {
            executor.routeFlow.collect {
                route.invoke(it)
            }
        }
    }

    override fun requestRoute(request: RouteRequest) = executor.requestRoute(request)
}

//FIXME: add supertype, deal with flows
private class Executor(
    private val networking: Networking
) : InstanceKeeper.Instance {
    private val coroutineScope = MainScope()

    private val routeReceived = MutableSharedFlow<Route>()
    val routeFlow: Flow<Route> = routeReceived

    private val api = networking.createApi(Api::class.java)
    private val pathRepository = PathRepositoryImpl(api)

    fun requestRoute(request: RouteRequest) {
        coroutineScope.launch {
            val route = pathRepository.getPath(
                start = request.from,
                end = request.to,
                ecoParam = request.parameterRange.convert(request.ecoParameter, 0f..1f)
            )
            routeReceived.emit(route)
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
    }
}