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

@Parcelize
data class ChildConf(val show : Boolean) : Parcelable

fun <T : Any> AppComponentContext.oneChild(
    key : String,
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
        initialConfiguration = ChildConf(showed),
        key = key,
        childFactory = { c, ctx ->
            if (c.show) {
                create(ctx)
            } else {
                Unit
            }
        }
    )

    override fun show() {
        router.replaceCurrent(ChildConf(true))
    }

    override fun hide() {
        router.replaceCurrent(ChildConf(true))
    }

    @Suppress("UNCHECKED_CAST")
    override val state: Value<ChildOneChild<T>>
        get() = router.state.map {
            if (it.activeChild.configuration.show) {
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

