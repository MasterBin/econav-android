package ru.nk.econav.core.common.util

fun <T> T?.ifNotNull(f : (T) -> Unit) : Unit =
    if (this != null) {
        f(this)
    } else {
        Unit
    }