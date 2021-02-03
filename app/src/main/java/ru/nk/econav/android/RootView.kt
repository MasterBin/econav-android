package ru.nk.econav.android

import android.view.View
import android.widget.FrameLayout
import com.arkivanov.decompose.extensions.android.RouterView
import com.arkivanov.decompose.extensions.android.ViewContext
import com.arkivanov.decompose.extensions.android.context
import ru.nk.econav.android.map.MainMapView

fun ViewContext.rootView(model: RootContainer): View {

    val root = RouterView(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    root.children(model.routerState, lifecycle) { parent, child, configuration ->
        parent.removeAllViews()
        parent.addView(
            when (child) {
                is RootContainer.Child.Map -> MainMapView(
                    child.component.model,
                    child.component.lifecycleExtension
                )
            }
        )
    }

    return root
}