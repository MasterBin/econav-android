package ru.nk.econav.android.features.searchplaces.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import ru.nk.econav.ui.components.Search
import ru.nk.econav.ui.components.SearchItem

@Composable
fun SearchPlaces(
    modifier: Modifier,
    component: SearchPlacesComponentImpl
) {
    val model by component.model.asState()

    Column(
        modifier = modifier
    ) {
        Search(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = model.searchText,
            onTextChanged = { component.onSearchTextChanged(it) }
        )

        Spacer(Modifier.height(16.dp))

        model.foundFeatures?.let { lst ->
            LazyColumn {
                items(
                    items = lst,
                    key = { it.hashCode() },
                ) {
                    SearchItem(
                        title = it.name,
                        subtitle = it.address,
                        distanceTo = it.distanceTo ?: "",
                        onClick = {
                            component.onItemClicked(it)
                        }
                    )
                }
            }
        }

    }


}