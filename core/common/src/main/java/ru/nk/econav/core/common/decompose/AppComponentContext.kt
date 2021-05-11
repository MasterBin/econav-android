package ru.nk.econav.core.common.decompose

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import com.arkivanov.decompose.*
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.lifecycle.subscribe
import com.arkivanov.decompose.statekeeper.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.reflect.KClass
import kotlin.reflect.KFunction2

interface AppComponentContext : ComponentContext {
    val applicationContext : Context
    val componentScope : CoroutineScope
    val activityResultRegistry : ActivityResultRegistry
}

class DefaultAppComponentContext(
    componentContext: ComponentContext,
    override val applicationContext: Context,
    override val componentScope: CoroutineScope = MainScope(),
    override val activityResultRegistry: ActivityResultRegistry,
    ) : AppComponentContext, ComponentContext by componentContext {

    init {
        lifecycle.subscribe(onDestroy = {
            componentScope.cancel()
        })
    }
}

fun <C : Parcelable, T : Any> AppComponentContext.appRouterInternal(
    initialConfiguration: () -> C,
    initialBackStack: () -> List<C> = ::emptyList,
    configurationClass: KClass<out C>,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    childFactory: (configuration: C, AppComponentContext) -> T
): Router<C, T> =
    router(
        initialConfiguration = initialConfiguration,
        initialBackStack = initialBackStack,
        configurationClass = configurationClass,
        key = key,
        handleBackButton = handleBackButton
    ) { configuration, componentContext ->
        childFactory(
            configuration,
            DefaultAppComponentContext(
                componentContext = componentContext,
                applicationContext = applicationContext,
                activityResultRegistry = activityResultRegistry
            )
        )
    }

inline fun <reified C : Parcelable, T : Any> AppComponentContext.appRouter(
    initialConfiguration: C,
    initialBackStack: List<C> = emptyList(),
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    noinline childFactory: (C, AppComponentContext) -> T
) : Router<C,T> =
    appRouterInternal(
        initialConfiguration = { initialConfiguration },
        initialBackStack = { initialBackStack },
        configurationClass = C::class,
        key = key,
        handleBackButton = handleBackButton,
        childFactory = childFactory
    )

fun AppComponentContext.childContext(key: String): AppComponentContext =
    DefaultAppComponentContext(
        this.childContext(key),
        applicationContext = applicationContext,
        activityResultRegistry = activityResultRegistry
    )