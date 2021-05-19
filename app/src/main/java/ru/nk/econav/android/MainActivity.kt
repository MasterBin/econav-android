package ru.nk.econav.android

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.backpressed.BackPressedDispatcher
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.arkivanov.decompose.statekeeper.StateKeeper
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets
import ru.nk.econav.android.root.Root
import ru.nk.econav.android.root.RootComponent
import ru.nk.econav.core.common.decompose.DefaultAppComponentContext
import ru.nk.econav.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalAnimatedInsets::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val componentContext = DefaultComponentContext(
            lifecycle.asDecomposeLifecycle(),
            StateKeeper(savedStateRegistry),
            InstanceKeeper(viewModelStore),
            BackPressedDispatcher(onBackPressedDispatcher)
        )


        setContent {
            AppTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    val root = remember {
                        val appComponentContext = DefaultAppComponentContext(
                            componentContext,
                            applicationContext,
                            activityResultRegistry = activityResultRegistry
                        )

                        RootComponent(appComponentContext)
                    }

                    Root(
                        Modifier.fillMaxSize(),
                        root
                    )
                }
            }
        }
    }

}