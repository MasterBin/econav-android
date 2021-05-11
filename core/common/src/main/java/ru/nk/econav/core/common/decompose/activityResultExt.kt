package ru.nk.econav.core.common.decompose

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.arkivanov.decompose.lifecycle.subscribe

fun <I, O> AppComponentContext.activityResult(
    key : String,
    contract : ActivityResultContract<I, O>,
    callback : ActivityResultCallback<O>
) : ActivityResultLauncher<I> {
    val activityResultLauncher = activityResultRegistry.register(key, contract, callback)

    lifecycle.subscribe(onDestroy = {
        activityResultLauncher.unregister()
    })

    return activityResultLauncher
}