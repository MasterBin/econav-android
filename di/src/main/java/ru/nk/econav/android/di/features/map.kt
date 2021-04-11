package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.map.api.MapComponent
import ru.nk.econav.android.features.map.impl.MapComponentImpl

val moduleMap = module {

    single<MapComponent.Factory> {
        MapComponent.Factory { appComponentContext ->
            MapComponentImpl(
                componentContext = appComponentContext,
                children = object : MapComponent.Children {
                    override val mainComponent: MainComponent.Factory = get()
                }
            )
        }
    }

}