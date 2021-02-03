package ru.nk.econav.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.backpressed.toBackPressedDispatcher
import com.arkivanov.decompose.extensions.android.DefaultViewContext
import com.arkivanov.decompose.extensions.android.child
import com.arkivanov.decompose.instancekeeper.toInstanceKeeper
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.arkivanov.decompose.statekeeper.toStateKeeper
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import ru.nk.econav.android.map.PathRepository
import ru.nk.econav.android.map.PathRepositoryImpl
import ru.nk.econav.extended_lifecycle.LifecycleExtension
import ru.nk.econav.extended_lifecycle.LifecycleExtensionImpl

class MainActivity : AppCompatActivity() {

    private val lifecycleExtensionImpl : LifecycleExtensionImpl by inject(named("impl"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleExtensionImpl.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val lifecycle = lifecycle.asDecomposeLifecycle()

        val componentContext = DefaultComponentContext(
            lifecycle = lifecycle,
            stateKeeper = savedStateRegistry.toStateKeeper(),
            instanceKeeper = viewModelStore.toInstanceKeeper(),
            backPressedDispatcher = onBackPressedDispatcher.toBackPressedDispatcher()
        )

        val root = createRoot(componentContext)

        val viewContext = DefaultViewContext(
            parent = findViewById(R.id.root),
            lifecycle = lifecycle,
        )

        viewContext.apply {
            child(parent) {
                rootView(root)
            }
        }
    }

    private fun createRoot(componentContext: ComponentContext) =
        RootContainer(
            componentContext,
            dependencies = object : RootContainer.Dependencies {
                override val pathRepository: PathRepository = getKoin().get()
                override val lifecycleExtension: LifecycleExtension = getKoin().get()
            }
        )

    override fun onLowMemory() {
        super.onLowMemory()
        lifecycleExtensionImpl.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleExtensionImpl.onSaveInstanceState(outState)
    }
}