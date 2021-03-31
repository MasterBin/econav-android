package ru.nk.econav.core.common.util

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.instancekeeper.getOrCreate
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.ValueObserver
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

fun <T : Store<*, *, *>> InstanceKeeper.getStore(key: Any, factory: () -> T): T =
    getOrCreate(key) { StoreHolder(factory()) }
        .store

inline fun <reified T : Store<*, *, *>> InstanceKeeper.getStore(noinline factory: () -> T): T =
    getStore(T::class, factory)

private class StoreHolder<T : Store<*, *, *>>(
    val store: T
) : InstanceKeeper.Instance {
    override fun onDestroy() {
        store.dispose()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
    object : Value<T>() {
        override val value: T get() = state
//        private val scope = CoroutineScope(Dispatchers.Main)
        private var jobs = emptyMap<ValueObserver<T>, CoroutineScope>()

        override fun subscribe(observer: ValueObserver<T>) {
            val scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                states.collect {
                    observer.invoke(it)
                }
            }
            jobs = jobs + (observer to scope)
        }

        override fun unsubscribe(observer: ValueObserver<T>) {
            val job = jobs[observer] ?: return
            jobs = jobs - observer
            job.cancel()
        }
    }