package ru.nk.econav.core.common.decompose

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetpack.asState
import com.arkivanov.decompose.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import kotlinx.parcelize.Parcelize

interface OneChild<T : Any> {
    fun show()
    fun hide()

    val state: Value<ChildOneChild<T>>

    val showed: Boolean
        get() = state.value is ChildOneChild.Showed

    val hidden: Boolean
        get() = state.value is ChildOneChild.Hidden
}

sealed class ChildOneChild<out T : Any> {
    data class Showed<out S : Any>(val instance: S) : ChildOneChild<S>()
    object Hidden : ChildOneChild<Nothing>()
}

private sealed class ChildConf : Parcelable {
    @Parcelize
    object Showed : ChildConf()

    @Parcelize
    object Hidden : ChildConf()
}

fun <T : Any> AppComponentContext.oneChild(
    key : String = "Default",
    showed: Boolean = true,
    create: (AppComponentContext) -> T
): OneChild<T> = OneChildImpl<T>(key,this, showed, create)

private class OneChildImpl <T: Any>(
    key : String,
    appComponentContext: AppComponentContext,
    showed: Boolean = true,
    create: (AppComponentContext) -> T
) : OneChild<T> {
    val router = appComponentContext.appRouter<ChildConf, Any>(
        initialConfiguration = if (showed) ChildConf.Showed else ChildConf.Hidden,
        key = key,
        childFactory = { c, ctx ->
            when (c) {
                ChildConf.Hidden -> {
                    Unit
                }
                ChildConf.Showed -> create(ctx)
            }
        }
    )

    override fun show() {
        router.replaceCurrent(ChildConf.Showed)
    }

    override fun hide() {
        router.replaceCurrent(ChildConf.Hidden)
    }

    @Suppress("UNCHECKED_CAST")
    override val state: Value<ChildOneChild<T>>
        get() = router.state.map {
            if (it.activeChild.configuration is ChildConf.Showed) {
                ChildOneChild.Showed(it.activeChild.instance as T)
            } else {
                ChildOneChild.Hidden
            }
        }
}

typealias ChildContent<T> = @Composable (child: ChildOneChild.Showed<T>) -> Unit
typealias ChildAnimation<T> = @Composable (ChildContent<T>) -> Unit

@Composable
fun <T : Any> OneChild(
    state: Value<ChildOneChild<T>>,
    content: ChildContent<T>
) {
    val s by state.asState()
    if (s is ChildOneChild.Showed<*>) {
        content(s as ChildOneChild.Showed<T>)
    }
}

