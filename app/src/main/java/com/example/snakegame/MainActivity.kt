package com.example.snakegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snakegame.snakeGameScreen.SnackGameScreen
import com.example.snakegame.snakeGameScreen.SnakeGameViewModel
import com.example.snakegame.ui.theme.SnakeGameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakeGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val viewModel = viewModel<SnakeGameViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    SnackGameScreen(
                        state = state,
                        onEvent = viewModel::onEvent
                    )

                }
            }
        }
    }
}

