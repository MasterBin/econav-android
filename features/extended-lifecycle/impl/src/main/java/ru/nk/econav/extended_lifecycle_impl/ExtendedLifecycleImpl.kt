package ru.nk.econav.extended_lifecycle_impl

import android.os.Bundle
import com.arkivanov.decompose.lifecycle.Lifecycle
import com.arkivanov.decompose.lifecycle.doOnDestroy
import ru.nk.econav.extended_lifecycle.ExtendedLifecycle
import ru.nk.econav.extended_lifecycle.ExtendedLifecycle.Callbacks
import ru.nk.econav.extended_lifecycle.LifecycleExtension
import ru.nk.econav.extended_lifecycle.subscribe

class LifecycleExtensionImpl : LifecycleExtension {

    private var callbacks = setOf<ExtendedLifecycleImpl>()
    private var onCreateBundle: Bundle? = null

    override fun create(lifecycle: Lifecycle): ExtendedLifecycle = ExtendedLifecycleImpl(lifecycle, onCreateBundle) {
        callbacks = callbacks - it
    }.also {
        callbacks = callbacks + it
    }

    fun onCreate(savedInstanceState: Bundle?) {
        onCreateBundle = savedInstanceState
    }

    fun onLowMemory() {
        callbacks.forEach(ExtendedLifecycleImpl::onLowMemory)
    }

    fun onSaveInstanceState(outState: Bundle) {
        callbacks.forEach {
            it.onSaveInstanceState(outState)
        }
    }
}

private class ExtendedLifecycleImpl(
    private val lifecycle: Lifecycle,
    private val onCreateBundle: Bundle?,
    private val onDispose : (ExtendedLifecycleImpl) -> Unit
) : ExtendedLifecycle {

    private var callbacks = HashMap<Callbacks, Lifecycle.Callbacks>()

    override fun subscribe(callbacks: Callbacks) {
        check(callbacks !in this.callbacks) { "Already subscribed" }

        val origin = object : Lifecycle.Callbacks {
            override fun onCreate() { callbacks.onCreate(onCreateBundle) }
            override fun onDestroy() { callbacks.onDestroy() }
            override fun onPause() { callbacks.onPause() }
            override fun onResume() { callbacks.onResume() }
            override fun onStart() { callbacks.onStart() }
            override fun onStop() { callbacks.onStop() }
        }

        this.callbacks[callbacks] = origin
        lifecycle.subscribe(origin)
    }

    override fun unsubscribe(callbacks: Callbacks) {
        val res = this.callbacks.remove(callbacks)
        check(res != null) { "Not subscribed" }
        lifecycle.unsubscribe(res)
    }

    override fun dispose() {
        val c = callbacks
        callbacks = HashMap()
        c.forEach {
            lifecycle.unsubscribe(it.value)
        }
        onDispose(this)
    }

    fun onLowMemory() {
        callbacks.keys.forEach(Callbacks::onLowMemory)
    }

    fun onSaveInstanceState(outState: Bundle) {
        callbacks.keys.forEach {
            it.onSaveInstanceState(outState)
        }
    }
}

