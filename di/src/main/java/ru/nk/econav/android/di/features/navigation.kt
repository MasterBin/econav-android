package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.features.navigation.api.NavigationComponent
import ru.nk.econav.android.features.navigation.impl.NavigationComponentImpl
import ru.nk.econav.android.userlocation.UserLocationComponent

val moduleNavigation = module {

    single {
        NavigationComponent.Factory { appComponentContext, deps ->
            NavigationComponentImpl(
                appComponentContext = appComponentContext,
                deps = deps,
                children = object : NavigationComponent.Children {
                    override val userLocationComponent: UserLocationComponent.Factory = get()
                }
            )
        }
    }

}