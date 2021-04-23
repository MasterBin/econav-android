package ru.nk.econav.core.common.util

fun <T> T?.ifNotNull(f : (T) -> Unit) : Unit =
    if (this != null) {
        f(this)
    } else {
        Unit
    }


fun ClosedFloatingPointRange<Float>.convert(number: Float, target: ClosedFloatingPointRange<Float>): Float {
    val ratio = number / (endInclusive - start)
    return (ratio * (target.endInclusive - target.start))
}