package ru.nk.econav.core.common.conductor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.bluelinelabs.conductor.Controller
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
abstract class BaseController : Controller, KoinComponent {

    constructor(): super() { contentLayoutId = 0 }

    constructor(contentLayoutId: Int) : super() {
        this.contentLayoutId = contentLayoutId
    }

    constructor(bundle: Bundle) : super(bundle) {
        this.contentLayoutId = 0
    }

    constructor(contentLayoutId: Int, bundle: Bundle) : super(bundle) {
        this.contentLayoutId = contentLayoutId
    }

    init {
        addLifecycleListener(object : LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                prepareUi()
            }

            override fun postDetach(controller: Controller, view: View) {
                //TODO
            }

            override fun postDestroyView(controller: Controller) {
                //TODO
            }

            override fun postDestroy(controller: Controller) {
                //TODO
            }
        })
    }

    @LayoutRes
    private val contentLayoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View =
        if (contentLayoutId != 0)
            inflater.inflate(contentLayoutId, container, false)
        else
            View(container.context)

    open fun prepareUi() {}
}