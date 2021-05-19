package ru.nk.econav.android.features.searchplaces.impl

import android.widget.Toast
import androidx.compose.ui.Modifier
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nk.econav.android.data.places.api.PlacesRepository
import ru.nk.econav.android.data.places.models.GeoFeature
import ru.nk.econav.android.features.searchplaces.api.SearchPlacesComponent
import ru.nk.econav.android.features.searchplaces.impl.store.SearchPlacesStore
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
        componentScope.launch {
            clearFlow.collect {
                store.accept(Intent.SearchTextChanged(""))
            }
        }
        componentScope.launch {
            store.labels.collect {
                when (it) {
                    SearchPlacesStore.Label.NetworkError ->
                        toast(applicationContext.getString(R.string.no_server_connection))
                    is SearchPlacesStore.Label.TextError ->
                        toast(it.text)
                }
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    fun onSearchTextChanged(text : String) {
        textChanged.invoke(text)
        store.accept(Intent.SearchTextChanged(text))
    }

    fun onSearchDoneClicked() {
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
