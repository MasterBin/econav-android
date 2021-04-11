package ru.nk.econav.android

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.backpressed.BackPressedDispatcher
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.arkivanov.decompose.statekeeper.StateKeeper
import org.koin.android.ext.android.getKoin
import ru.nk.econav.android.eco_param_elector.api.EcoParamElector
import ru.nk.econav.android.features.map.api.MapComponent
import ru.nk.econav.core.common.decopmose.Content
import ru.nk.econav.core.common.decopmose.DefaultAppComponentContext
import ru.nk.econav.core.common.util.OutEvent

class MainActivity : AppCompatActivity() {

//    private val lifecycleExtensionImpl: LifecycleExtensionImpl by inject(named("impl"))

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
//        lifecycleExtensionImpl.onCreate(savedInstanceState)

        val componentContext = DefaultComponentContext(
            lifecycle.asDecomposeLifecycle(),
            StateKeeper(savedStateRegistry),
            InstanceKeeper(viewModelStore),
            BackPressedDispatcher(onBackPressedDispatcher)
        )

        val appComponentContext = DefaultAppComponentContext(componentContext, applicationContext)

        val component = getKoin().get<MapComponent.Factory>().invoke(
            appComponentContext
        )

        setContent {
            MaterialTheme {
                component.render(Modifier.fillMaxSize())()
            }
        }
    }

    @Composable
    fun dsasd() {
        Text(text = "asd")
    }

//    override fun onLowMemory() {
//        super.onLowMemory()
//        lifecycleExtensionImpl.onLowMemory()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        lifecycleExtensionImpl.onSaveInstanceState(outState)
//    }
}