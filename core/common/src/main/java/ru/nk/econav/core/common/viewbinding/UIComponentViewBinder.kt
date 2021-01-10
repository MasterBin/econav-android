package ru.nk.econav.core.common.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bluelinelabs.conductor.Controller

class UIComponentViewBinder<T : ViewBinding>(
        private val viewBindingClass : Class<T>
) {

    private val inflateViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.getMethod("inflate", View::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    fun inflate(parentView : ViewGroup, attachToRoot : Boolean): T {
        return inflateViewMethod(null, LayoutInflater.from(parentView.context), parentView, attachToRoot) as T
    }
}