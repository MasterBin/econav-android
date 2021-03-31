package ru.nk.econav.extended_lifecycle_impl

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nk.econav.extended_lifecycle.LifecycleExtension
import ru.nk.econav.extended_lifecycle_impl.LifecycleExtensionImpl

val extendedLifecycleModule = module {

    val lifecycleExtension = LifecycleExtensionImpl()

    single<LifecycleExtension>(createdAtStart = true) { lifecycleExtension }
    single(qualifier = named("impl"), createdAtStart = true) { lifecycleExtension }
}