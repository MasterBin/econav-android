package ru.nk.econav.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.backpressed.toBackPressedDispatcher
import com.arkivanov.decompose.instancekeeper.toInstanceKeeper
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.arkivanov.decompose.statekeeper.toStateKeeper
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import ru.nk.econav.extended_lifecycle_impl.LifecycleExtensionImpl
import ru.nk.econav.mapscreen.MainMapComponent
import ru.nk.econav.mapscreen.Map
import ru.nk.econav.mapscreen.data.PathRepository
import ru.nk.econav.mapscreen.data.PathRepositoryImpl

class MainActivity : AppCompatActivity() {

    private val lifecycleExtensionImpl: LifecycleExtensionImpl by inject(named("impl"))

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        lifecycleExtensionImpl.onCreate(savedInstanceState)
//
//        setContentView(R.layout.activity_main)
//
//        val lifecycle = lifecycle.asDecomposeLifecycle()
//
//        val componentContext = DefaultComponentContext(
//            lifecycle = lifecycle,
//            stateKeeper = savedStateRegistry.toStateKeeper(),
//            instanceKeeper = viewModelStore.toInstanceKeeper(),
//            backPressedDispatcher = onBackPressedDispatcher.toBackPressedDispatcher()
//        )
//
//        val root = createRoot(componentContext)
//
//        val viewContext = DefaultViewContext(
//            parent = findViewById(R.id.root),
//            lifecycle = lifecycle,
//        )
//
//        viewContext.apply {
//            child(parent) {
//                rootView(root)
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleExtensionImpl.onCreate(savedInstanceState)

        val componentContext = DefaultComponentContext(
            lifecycle.asDecomposeLifecycle(),
            savedStateRegistry.toStateKeeper(),
            viewModelStore.toInstanceKeeper(),
            onBackPressedDispatcher.toBackPressedDispatcher()
        )

        val mapComponent = MainMapComponent(
            componentContext = componentContext,
            deps = object : MainMapComponent.Dependencies {
                override val pathRepository: PathRepository = PathRepositoryImpl(getKoin().get())
            }
        )

        setContent {
            MaterialTheme {
                Box(Modifier.fillMaxSize()){
                    var state by rememberSaveable { mutableStateOf(false) }
                    if (state) {
                        Map(
                            modifier = Modifier.fillMaxSize(),
                            lifecycleExtension = lifecycleExtensionImpl,
                            mainMap = mapComponent
                        )
                    }
                    Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = { state = !state }) {
                        Text(text = "Switch")
                    }
                }
            }
        }
    }

//    private fun createRoot(componentContext: ComponentContext) =
//        RootContainer(
//            componentContext,
//            dependencies = object : RootContainer.Dependencies {
//                override val pathRepository: PathRepository = getKoin().get()
//                override val lifecycleExtension: LifecycleExtension = getKoin().get()
//            }
//        )

    override fun onLowMemory() {
        super.onLowMemory()
        lifecycleExtensionImpl.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lifecycleExtensionImpl.onSaveInstanceState(outState)
    }
}