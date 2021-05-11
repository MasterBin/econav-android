package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.userlocation.UserLocationComponent
import ru.nk.econav.android.userlocation.impl.UserLocationComponentImpl

val moduleUserLocation = module {

    single<UserLocationComponent.Factory> {
        UserLocationComponent.Factory { appComponentContext, deps ->
            UserLocationComponentImpl(
                appComponentContext = appComponentContext,
                deps
            )
        }
    }

}