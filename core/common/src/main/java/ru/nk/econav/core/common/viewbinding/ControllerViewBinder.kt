package ru.nk.econav.core.common.viewbinding

import android.view.View
import androidx.viewbinding.ViewBinding
import com.bluelinelabs.conductor.Controller

class ControllerViewBinder<T : ViewBinding>(
    private val viewBindingClass : Class<T>
) {

    /**
     * Cache static method `ViewBinding.bind(View)`
     */
    private val bindViewMethod by lazy(LazyThreadSafetyMode.NONE) {
        viewBindingClass.getMethod("bind", View::class.java)
    }

    /**
     * Create new [ViewBinding] instance
     */
    @Suppress("UNCHECKED_CAST")
    fun bind(controller : Controller): T {
        return bindViewMethod(null, controller.view) as T
    }
}