package ru.nk.econav.android

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.arkivanov.decompose.extensions.android.RouterView
import com.arkivanov.decompose.extensions.android.ViewContext
import com.arkivanov.decompose.extensions.android.context
import ru.nk.econav.android.map.MainMap
import ru.nk.econav.android.map.MainMapView
import ru.nk.econav.extended_lifecycle.ExtendedLifecycle

fun ViewContext.rootView(model : RootContainer.Model) : View {

    val root = RouterView(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    root.children(model.child, lifecycle) { parent, child, configuration ->
        parent.removeAllViews()
        when(configuration.screen) {
            RootContainer.Screens.INTRO -> TODO()
            RootContainer.Screens.MAINMAP -> parent.addView(MainMapView((child as MainMap).model))
        }
    }

    return root
}