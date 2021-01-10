package ru.nk.econav.core.common.view

import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.launch

interface UIComponent {
    fun init() {}
    fun onDestroy() {}
    fun onPause() {}
    fun onResume() {}
}
