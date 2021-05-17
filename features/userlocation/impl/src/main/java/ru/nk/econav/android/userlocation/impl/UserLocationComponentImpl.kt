package ru.nk.econav.android.userlocation.impl

import android.Manifest
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.toBitmap
import com.arkivanov.decompose.lifecycle.subscribe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import ru.nk.econav.android.core.mapinterface.MyLocationInterface
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.activityResult
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.ui.components.UserLocationButton

class UserLocationComponentImpl(
    appComponentContext: AppComponentContext,
    dependencies: UserLocationComponent.Dependencies
) : AppComponentContext by appComponentContext, UserLocationComponent,
    UserLocationComponent.Dependencies by dependencies {

    override fun render(modifier: Modifier): Content = {
        UserLocation(modifier, this)
    }

    private val mapInterface = getMapInterface(lifecycle)
    private var locationInterface: MyLocationInterface? = null

    private val askLocationPermission = activityResult(
        key = "UserLocationComponentImpl",
        ActivityResultContracts.RequestPermission()
    ) { permitted ->
        if (permitted) {
            if (locationInterface == null) {
                getLocationInterface()
            } else {
                locationInterface?.let { initLocationInterface(it) }
            }
        }
    }

    init {
        requestPermissions()
    }

    private fun getLocationInterface() {
        componentScope.launch {
            mapInterface.getLocationInterface().collect {
                locationInterface = it
                initLocationInterface(it)
            }
        }
    }

    private fun initLocationInterface(locationInterface: MyLocationInterface) {
        val locationProvider = GpsMyLocationProvider(applicationContext)

        locationProvider.startLocationProvider { location, source ->
            userLocation.invoke(LatLon(location.latitude, location.longitude))
        }

        lifecycle.subscribe(
            onPause = {
                locationProvider.stopLocationProvider()
            },
            onResume = {
                locationProvider.startLocationProvider { location, source ->
                    userLocation.invoke(LatLon(location.latitude, location.longitude))
                }
            },
            onDestroy = {
                locationProvider.destroy()
            }
        )

        locationInterface.enableMyLocation(GpsMyLocationProvider(applicationContext))
        locationInterface.setUserIcon(
            applicationContext.getDrawable(R.drawable.ic_user_location)!!.toBitmap()
        )

        locationInterface.enableFollowLocation()
        locationInterface.getMyLocation()?.let{
            userLocation.invoke(LatLon(it.latitude, it.longitude))
        }
    }

    private fun requestPermissions() {
        askLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun onClick() {
        locationInterface?.getMyLocation()?.let {
            mapInterface.moveToPoint(it)
        }
    }

}

@Composable
fun UserLocation(
    modifier: Modifier = Modifier,
    component: UserLocationComponentImpl
) {
    UserLocationButton(modifier, onClick = { component.onClick() })
}