package ru.nk.econav.extended_lifecycle

import android.os.Bundle
import com.arkivanov.decompose.lifecycle.Lifecycle

inline fun ExtendedLifecycle.subscribe(
    crossinline onCreate: (savedInstanceState: Bundle?) -> Unit = {},
    crossinline onStart: () -> Unit = {},
    crossinline onResume: () -> Unit = {},
    crossinline onPause: () -> Unit = {},
    crossinline onStop: () -> Unit = {},
    crossinline onLowMemory: () -> Unit = {},
    crossinline onSaveInstanceState: (outState: Bundle) -> Unit = {},
    crossinline onDestroy: (DefaultExtendedLifecycleCallbacks) -> Unit = {},
) {
    val callbacks = object : DefaultExtendedLifecycleCallbacks {
        override fun onCreate(savedInstanceState: Bundle?) {
            onCreate.invoke(savedInstanceState)
        }

        override fun onStart() {
            onStart.invoke()
        }

        override fun onResume() {
            onResume.invoke()
        }

        override fun onPause() {
            onPause.invoke()
        }

        override fun onStop() {
            onStop.invoke()
        }

        override fun onLowMemory() {
            onLowMemory.invoke()
        }

        override fun onSaveInstanceState(outState: Bundle) {
            onSaveInstanceState.invoke(outState)
        }

        override fun onDestroy() {
            onDestroy.invoke(this)
        }
    }

    subscribe(callbacks)
}

fun Lifecycle.extended(extension: LifecycleExtension): ExtendedLifecycle = extension.create(this)