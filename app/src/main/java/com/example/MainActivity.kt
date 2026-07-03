package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.StoryRepository
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.screens.CharactersScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StoryPreviewScreen
import com.example.ui.screens.StoryScreen
import com.example.ui.screens.TriviaScreen
import com.example.ui.screens.FinalCreditsScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Init SoundManager
        val ctx = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            this
        } else {
            this
        }
        com.example.utils.SoundManager.init(ctx)
        
        // Edge-to-edge design for immersive fullscreen experience
        enableEdgeToEdge()

        // Initialize Local Database and Repository (optimized offline execution)
        val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = StoryRepository(database.adventureDao())
        val factory = MainViewModelFactory(application, repository)

        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel(factory = factory)
                val currentScreen by viewModel.currentScreen.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = androidx.compose.foundation.layout.WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Beautiful clean animations when switching between the pop-up book pages
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                // Pop-up book style transition: Scale in with a bounce, fade out scaling up
                                (scaleIn(
                                    initialScale = 0.7f,
                                    animationSpec = androidx.compose.animation.core.spring(
                                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                                        stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                                    )
                                ) + fadeIn(
                                    animationSpec = androidx.compose.animation.core.tween(400)
                                )) togetherWith (
                                    scaleOut(
                                        targetScale = 1.2f,
                                        animationSpec = androidx.compose.animation.core.tween(400)
                                    ) + fadeOut(
                                        animationSpec = androidx.compose.animation.core.tween(400)
                                    )
                                )
                            },
                            label = "BookPageTurnTransition"
                        ) { screen ->
                            val screenContent = @Composable {
                                when (screen) {
                                    Screen.Credits -> com.example.ui.screens.CreditsScreen(viewModel = viewModel)
                                    Screen.FinalCredits -> FinalCreditsScreen(viewModel = viewModel)
                                    Screen.Home -> HomeScreen(viewModel = viewModel)
                                    Screen.StoryPreview -> StoryPreviewScreen(viewModel = viewModel)
                                    Screen.Story -> StoryScreen(viewModel = viewModel)
                                    Screen.Trivia -> TriviaScreen(viewModel = viewModel)
                                    Screen.Characters -> CharactersScreen(viewModel = viewModel)
                                }
                            }
                            
                            if (screen == Screen.Credits || screen == Screen.FinalCredits) {
                                screenContent()
                            } else {
                                com.example.ui.components.DioramaAppWrapper {
                                    screenContent()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        com.example.utils.SoundManager.release()
    }
}

/**
 * Custom ViewModelFactory to inject Application and StoryRepository.
 */
class MainViewModelFactory(
    private val application: Application,
    private val repository: StoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
