package ru.nk.econav.core.common.util

sealed class Upshot<out OK, out ERROR> {

    internal abstract val isOk: Boolean
    internal abstract val isError: Boolean

    fun isOk() = isOk
    fun isError() = isError

    data class Ok<out OK> internal constructor(val okValue: OK) : Upshot<OK, Nothing>() {
        override val isOk: Boolean
            get() = true
        override val isError: Boolean
            get() = false
    }

    data class Error<out ERR> internal constructor(val errValue: ERR) : Upshot<Nothing, ERR>() {
        override val isOk: Boolean
            get() = false
        override val isError: Boolean
            get() = true
    }
}

fun <A, B, E> Upshot<A,E>.combine(b : Upshot<B,E>) : Upshot<Pair<A,B>, E> {
    return when(this){
        is Upshot.Ok -> {
            when (b) {
                is Upshot.Ok -> (this.okValue to b.okValue).asOk()
                is Upshot.Error -> b
            }
        }
        is Upshot.Error -> this
    }
}

fun <T> T.asOk(): Upshot<T, Nothing> = Upshot.Ok(this)
fun <T> T.asError(): Upshot<Nothing, T> = Upshot.Error(this)

fun <OK, ERR> Upshot<OK, ERR>.getOrNull(): OK? = when (this) {
    is Upshot.Ok -> okValue
    is Upshot.Error -> null
}

inline fun <OK, ERR> Upshot<OK, ERR>.getOr(or: () -> OK): OK = when (this) {
    is Upshot.Ok -> okValue
    is Upshot.Error -> or()
}

inline fun <OK, ERR, OUT> Upshot<OK, ERR>.map(f: (OK) -> OUT): Upshot<OUT, ERR> =
    when (this) {
        is Upshot.Ok -> f(this.okValue).asOk()
        is Upshot.Error -> this
    }

inline fun <OK, ERR> Upshot<OK, ERR>.open(
    ifOk: (OK) -> Unit,
    ifError: (ERR) -> Unit
): Unit =
    when (this) {
        is Upshot.Ok -> ifOk(this.okValue)
        is Upshot.Error -> ifError(this.errValue)
    }

inline fun <OK, ERR, OUT> Upshot<OK, ERR>.fold(
    ifOk: (OK) -> OUT,
    ifError: (ERR) -> OUT
): OUT = when (this) {
    is Upshot.Ok -> ifOk(this.okValue)
    is Upshot.Error -> ifError(this.errValue)
}

inline fun <OK, ERR> Upshot<OK, ERR>.ifOkDo(
    ok: (OK) -> Unit,
): Unit =
    when (this) {
        is Upshot.Ok -> ok(this.okValue)
        else -> Unit
    }

fun <OK, ERR> Upshot<OK, ERR>.ifErrorDo(
    error: (ERR) -> Unit,
): Unit =
    when (this) {
        is Upshot.Error -> error(this.errValue)
        else -> Unit
    }
