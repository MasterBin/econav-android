package ru.nk.econav.android.di.features

import org.koin.dsl.module
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.eco_param_elector.impl.EcoParamElectorImpl

val moduleEcoParamElector = module {

    single<EcoParamElector.Factory> {
        EcoParamElector.Factory { componentContext, deps ->
            EcoParamElectorImpl(componentContext, deps)
        }
    }

}