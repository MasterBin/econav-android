package ru.nk.econav.core.common.conductor

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.viewpager.RouterPagerAdapter

fun Controller.createSimplePagerAdapter(
    count : Int,
    pageFactory : (pos : Int) -> Controller,
    titleFactory : ((pos : Int) -> CharSequence)? = null
) : RouterPagerAdapter = object : RouterPagerAdapter(this) {
    override fun getCount(): Int = count

    override fun configureRouter(router: Router, pos: Int) {
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(pageFactory(pos)))
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleFactory?.invoke(position) ?: super.getPageTitle(position)
    }
}