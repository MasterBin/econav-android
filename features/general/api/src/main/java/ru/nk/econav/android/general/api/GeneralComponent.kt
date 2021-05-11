package ru.nk.econav.android.general.api

import ru.nk.econav.core.common.decompose.AppComponentContext

interface GeneralComponent {

    interface Children {
    }

    interface Dependencies {

    }

    fun interface Factory {
        fun invoke(appComponentContext: AppComponentContext, deps : Dependencies) : GeneralComponent
    }

}