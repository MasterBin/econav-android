package ru.nk.econav.core.common.util

import androidx.compose.runtime.Composable

fun interface OutEvent <T> {
    fun invoke(value : T)
}

fun OutEvent<Unit>.invoke() {
    invoke(Unit)
}
