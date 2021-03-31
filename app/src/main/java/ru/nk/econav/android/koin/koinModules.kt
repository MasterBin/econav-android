package ru.nk.econav.android.koin

import org.koin.core.module.Module
import ru.nk.econav.android.map.testModule
import ru.nk.econav.extended_lifecycle_impl.extendedLifecycleModule

val koinModules = listOf<Module>(
    extendedLifecycleModule,
    testModule
)