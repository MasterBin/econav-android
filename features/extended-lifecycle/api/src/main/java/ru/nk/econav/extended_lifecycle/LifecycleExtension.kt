package ru.nk.econav.extended_lifecycle

import com.arkivanov.decompose.lifecycle.Lifecycle

interface LifecycleExtension {
    fun create(lifecycle: Lifecycle) : ExtendedLifecycle
}