package ru.nk.econav.android.features.main.impl

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.parcelize.Parcelize
import ru.nk.econav.android.features.main.api.MainComponent
import ru.nk.econav.android.features.main.impl.store.MainStoreProvider
import ru.nk.econav.android.general.api.GeneralComponent
import ru.nk.econav.core.common.decompose.AppComponentContext
import ru.nk.econav.core.common.decompose.Content
import ru.nk.econav.core.common.decompose.appRouter
import ru.nk.econav.core.common.util.getStore

@OptIn(InternalCoroutinesApi::class)
class MainComponentImpl(
    private val appComponentContext: AppComponentContext,
    private val deps: MainComponent.Dependencies,
    private val children: MainComponent.Children
) : MainComponent, AppComponentContext by appComponentContext, MainComponent.Dependencies by deps {

    private val store = instanceKeeper.getStore {
        MainStoreProvider(LoggingStoreFactory(DefaultStoreFactory))
            .create()
    }

    sealed class Child : Parcelable {

        @Parcelize
        object General : Child()
    }

    private val router = appRouter<Child, (Modifier) -> Content>(
        initialConfiguration = Child.General,
        handleBackButton = true,
        childFactory = ::createChildren
    )

    val routerState = router.state

    private fun createChildren(
        config: Child,
        appComponentContext: AppComponentContext
    ): (Modifier) -> Content =
        when (config) {
            Child.General -> {
                {
                    children.general.invoke(
                        appComponentContext,
                        deps = object : MainComponent.Dependencies by deps,
                            GeneralComponent.Dependencies {
                        }
                    ).render(it)
                }
            }
        }

    override fun render(modifier: Modifier): Content = {
        MainComponentView(modifier = modifier, component = this)
    }
}

@Composable
fun MainComponentView(
    modifier: Modifier = Modifier,
    component: MainComponentImpl
) {
    Box(Modifier.fillMaxSize()) {
        Children(routerState = component.routerState) {
            it.instance.invoke(modifier).invoke()
        }
    }
}