package ru.nk.econav.extended_lifecycle

import android.os.Bundle

interface ExtendedLifecycle {

    fun subscribe(callbacks: Callbacks)
    fun unsubscribe(callbacks: Callbacks)

    interface Callbacks {
        fun onCreate(savedInstanceState : Bundle?)
        fun onStart()
        fun onResume()
        fun onPause()
        fun onStop()
        fun onDestroy()
        fun onLowMemory()
        fun onSaveInstanceState(outState : Bundle)
    }
}
