package ru.nk.econav.core.common.viewbinding

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.viewbinding.ViewBinding
import com.bluelinelabs.conductor.Controller
import ru.nk.econav.core.common.view.UIComponent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ControllerViewBindingProperty<C : Controller, T : ViewBinding>(
    private val viewBinder: (C) -> T
) : ReadOnlyProperty<C, T> {

    var viewBinding: T? = null

    @MainThread
    override fun getValue(thisRef: C, property: KProperty<*>): T {
        checkIsMainThread()
        viewBinding?.let { return it }

        thisRef.addLifecycleListener(object : Controller.LifecycleListener() {
            override fun postDestroyView(controller: Controller) {
                controller.removeLifecycleListener(this)
                viewBinding = null
            }
        })
        return viewBinder(thisRef).also { viewBinding = it }
    }

    private fun checkIsMainThread() {
        check(Looper.myLooper() == Looper.getMainLooper())
    }
}

class UIComponentViewBindingProperty<UC : UIComponent, VB : ViewBinding>(
        private val inflateMethod : (LayoutInflater, ViewGroup, Boolean) -> VB,
        private val parentView : ViewGroup,
        private val attachToRoot : Boolean = true
) : ReadOnlyProperty<UC, VB> {

    override fun getValue(thisRef: UC, property: KProperty<*>): VB {
        return inflateMethod(LayoutInflater.from(parentView.context), parentView, attachToRoot)
    }
}

fun <C : UIComponent, VB : ViewBinding> C.viewBinding(
    parentView: ViewGroup,
    inflateMethod: (LayoutInflater, ViewGroup, Boolean) -> VB,
    attachToRoot: Boolean = true
): UIComponentViewBindingProperty<C, VB> {
    return UIComponentViewBindingProperty(inflateMethod, parentView, attachToRoot)
}

fun <C : Controller, T : ViewBinding> C.viewBinding(viewBinder: (C) -> T) : ControllerViewBindingProperty<C, T> {
    return ControllerViewBindingProperty(viewBinder)
}

inline fun <C : Controller, reified T : ViewBinding> C.viewBinding() : ControllerViewBindingProperty<C, T> {
    return viewBinding(ControllerViewBinder(T::class.java)::bind)
}

