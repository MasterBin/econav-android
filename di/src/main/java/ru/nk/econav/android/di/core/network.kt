package ru.nk.econav.android.di.core

import org.koin.dsl.module
import ru.nk.econav.android.core.network.api.Networking
import ru.nk.econav.android.core.network.impl.NetworkingImpl


val moduleNetwork = module {

    single<Networking> {
        NetworkingImpl()
    }
}