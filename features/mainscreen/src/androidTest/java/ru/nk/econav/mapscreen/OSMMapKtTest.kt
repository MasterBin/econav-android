package ru.nk.econav.mapscreen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.backpressed.BackPressedDispatcher
import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.arkivanov.decompose.lifecycle.asDecomposeLifecycle
import com.arkivanov.decompose.statekeeper.StateKeeper
import org.junit.Rule
import org.junit.Test
import ru.nk.econav.mapscreen.map.MapComponent
import ru.nk.econav.mapscreen.map.OsmMap

class OSMMapKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `smoke`() {

        val componentContext = composeTestRule.activity.let {
            DefaultComponentContext(
                it.lifecycle.asDecomposeLifecycle(),
                StateKeeper(it.savedStateRegistry),
                InstanceKeeper(it.viewModelStore),
                BackPressedDispatcher(it.onBackPressedDispatcher)
            )
        }

        composeTestRule.setContent {
            OsmMap(
                Modifier.fillMaxSize(),
                mainMap = MapComponent(componentContext)
            )
        }
    }

}