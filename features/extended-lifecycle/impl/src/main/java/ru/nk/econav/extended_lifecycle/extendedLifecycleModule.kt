package ru.nk.econav.extended_lifecycle

import org.koin.core.qualifier.named
import org.koin.dsl.module

val extendedLifecycleModule = module {

    val lifecycleExtension = LifecycleExtensionImpl()

    single<LifecycleExtension>(createdAtStart = true) { lifecycleExtension }
    single(qualifier = named("impl"), createdAtStart = true) { lifecycleExtension }
}