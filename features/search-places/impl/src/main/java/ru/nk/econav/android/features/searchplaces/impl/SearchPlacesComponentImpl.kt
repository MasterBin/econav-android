package ru.nk.econav.android.features.searchplaces.impl

import androidx.compose.ui.Modifier
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nk.econav.android.data.places.api.PlacesRepository
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.features.searchplaces.impl.store.SearchPlacesStore.Intent
import ru.nk.econav.android.features.searchplaces.impl.store.SearchPlacesStoreProvider
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.models.LatLon
import ru.nk.econav.core.common.util.asValue
import ru.nk.econav.core.common.util.getStore

class SearchPlacesComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps : SearchPlacesComponent.Dependencies,
    private val repository : PlacesRepository
) : AppComponentContext by appComponentContext, SearchPlacesComponent, SearchPlacesComponent.Dependencies by deps{

    private val store = instanceKeeper.getStore {
        SearchPlacesStoreProvider(
            LoggingStoreFactory(DefaultStoreFactory),
            repository
        ).create()
    }

    val model = store.asValue()

    init {
        componentScope.launch {
            userLocationFlow.collect {
                userLocationChanged(it)
            }
        }
    }

    fun onSearchTextChanged(text : String) {
        store.accept(Intent.SearchTextChanged(text))
    }

    fun onSearchButtonClicked() {
        store.accept(Intent.SearchClicked)
    }

    fun onItemClicked(item : GeoFeature) {
        deps.placeSelected.invoke(item)
    }

    private fun userLocationChanged(location : LatLon) {
        store.accept(Intent.UserLocationChanged(location))
    }

    override fun render(modifier: Modifier): Content = {
        SearchPlaces(modifier, this)
    }
}
