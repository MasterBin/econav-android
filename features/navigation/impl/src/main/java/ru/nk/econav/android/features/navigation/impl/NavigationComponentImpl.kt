package ru.nk.econav.android.features.navigation.impl

import androidx.compose.ui.Modifier
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import ru.nk.econav.android.data.routing.models.Instruction
import ru.nk.econav.android.features.navigation.api.NavigationComponent
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.oneChild
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.OutEvent
import ru.nk.econav.core.common.util.invoke

class NavigationComponentImpl(
    val appComponentContext: AppComponentContext,
    val deps: NavigationComponent.Dependencies,
    val children: NavigationComponent.Children
) : NavigationComponent, AppComponentContext by appComponentContext,
    NavigationComponent.Dependencies by deps {

    val userLocationComponent = oneChild("userLocation2") {
        children.userLocationComponent.invoke(
            it,
            object : UserLocationComponent.Dependencies, NavigationComponent.Dependencies by this {
                override val userLocation: OutEvent<LatLon> = OutEvent {
                    onUserLocationChanged(it)
                }
                override val permissionNotGranted: OutEvent<Unit> = OutEvent {
                    //TODO:
                }
            }
        )
    }

    private val mapInterface = getMapInterface(lifecycle)

    private val polyline = Polyline()
    private val overlayPoints = ItemizedIconOverlay<OverlayItem>(
        applicationContext,
        mutableListOf(),
        object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                return false
            }

            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                return false
            }
        }
    )

    init {
        if (route.instructions.isEmpty())
            noRouteInstructions.invoke()

        mapInterface.add(polyline)
        mapInterface.add(overlayPoints)
        drawPolyline()
        initEndLocation()
        mapInterface.invalidateMap()
        moveToUserLocation()
    }

    private val _model = MutableValue(Model(route.instructions.first()))
    val model: Value<Model> = _model

    private fun onUserLocationChanged(location: LatLon) {
        val point = GeoPoint(location.lat, location.lon)

        val polylinePoint =
            polyline.actualPoints.minByOrNull { it.distanceToAsDouble(point) } ?: return
        val distance = polylinePoint.distanceToAsDouble(point)

        if (distance < 20) {
            val index = polyline.actualPoints.indexOf<GeoPoint>(polylinePoint)
            polyline.setPoints(
                listOf(point) + polyline.actualPoints.minus(
                    polyline.actualPoints.subList(
                        0,
                        index + 1
                    )
                )
            )

            val nextInstruction = route.instructions.minByOrNull {
                val s = it.points.minByOrNull { GeoPoint(it.lat, it.lon).distanceToAsDouble(polylinePoint) < 20 } ?: error("")
                GeoPoint(s.lat, s.lon).distanceToAsDouble(polylinePoint)
            }

            _model.reduce {
                it.copy(
                    instruction = nextInstruction!!
                )
            }
//
//            if (instructionPoint != null) {
//                val index = route.instructions.indexOf(_model.value.instruction)
//                val instr = route.instructions.getOrNull(index + 1)
//
//                if (instr == null) {
//                    arrivedAtDestination()
//                    return
//                }
//
//            }
        }

        mapInterface.invalidateMap()
    }

    private fun arrivedAtDestination() {

    }

    private fun drawPolyline() {
        polyline.setPoints(route.polyline.map { GeoPoint(it.lat, it.lon) })
    }

    private fun moveToUserLocation() {
        mapInterface.moveToPoint(GeoPoint(route.from.lat, route.from.lon), 17.0)
    }

    private fun initEndLocation() {
        overlayPoints.addItem(
            OverlayItem(
                null,
                null,
                GeoPoint(route.to.lat, route.to.lon)
            ).apply {
                setMarker(applicationContext.getDrawable(R.drawable.ic_point_end))
            })
    }


    override fun render(modifier: Modifier): Content = {
        Navigation(modifier, this)
    }

    data class Model(
        val instruction: Instruction
    )
}

