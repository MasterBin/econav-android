package ru.nk.econav.android.core.network.api

interface Networking {
    fun <T> createApi(clazz: Class<T>) : T
}