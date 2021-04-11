package ru.nk.econav.extended_lifecycle

import android.os.Bundle

interface DefaultExtendedLifecycleCallbacks : ExtendedLifecycle.Callbacks {
    override fun onCreate(savedInstanceState : Bundle?) {}
    override fun onStart() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun onStop() {}
    override fun onDestroy() {}
    override fun onLowMemory() {}
    override fun onSaveInstanceState(outState : Bundle) {}
}