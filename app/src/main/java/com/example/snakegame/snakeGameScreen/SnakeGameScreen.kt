package com.example.snakegame.snakeGameScreen

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snakegame.snakeGameScreen.component.SnakeBoard
import com.example.snakegame.R
import com.example.snakegame.snakeGameScreen.component.GameState
import com.example.snakegame.snakeGameScreen.component.SnakeGameEvent
import com.example.snakegame.snakeGameScreen.component.SnakeGameState

@Composable
fun SnackGameScreen(
    state: SnakeGameState,
    onEvent: (SnakeGameEvent) -> Unit
) {


    val context = LocalContext.current
    val foodSound = remember {
        MediaPlayer.create(context, R.raw.food)
    }
    val gameOverSound = remember {
        MediaPlayer.create(context, R.raw.gameover)
    }

    LaunchedEffect(key1 = state.snake.size){
        if (state.snake.size != 1){
            foodSound?.start()
        }
    }

    LaunchedEffect(key1 = state.isGameOver){
        if (state.isGameOver){
            gameOverSound?.start()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Text(
                text = "Score: ${state.snake.size -1}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
        }

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
        ){
            SnakeBoard(
                state, onEvent
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .weight(1f),
                onClick = {
                    onEvent(SnakeGameEvent.ResetGame)
                },
                enabled = state.gameState == GameState.PAUSE || state.isGameOver
            ) {
                Text(
                    text = if (state.isGameOver) "Reset"
                           else "New Game",
                    fontSize = 18.sp
                    )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .weight(1f),
                onClick = {
                    when(state.gameState){
                        GameState.IDLE, GameState.PAUSE -> {
                            onEvent(SnakeGameEvent.StartGame)
                        }
                        GameState.Start -> {
                            onEvent(SnakeGameEvent.PauseGame)
                        }
                    }
                },
                enabled = !state.isGameOver
            ) {
                Text(
                    text = when(
                        state.gameState
                    ){
                        GameState.IDLE -> "Start"
                        GameState.PAUSE -> "Resume"
                        GameState.Start -> "Pause"
                    },
                    fontSize = 18.sp
                    )
            }

        }

    }



}


