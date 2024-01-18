package com.example.snakegame.snakeGameScreen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snakegame.snakeGameScreen.component.Coordinate
import com.example.snakegame.snakeGameScreen.component.Direction
import com.example.snakegame.snakeGameScreen.component.GameState
import com.example.snakegame.snakeGameScreen.component.SnakeGameEvent
import com.example.snakegame.snakeGameScreen.component.SnakeGameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnakeGameViewModel: ViewModel() {

//    private val _state = MutableStateFlow(SnakeGameState())
//    val state = _state.asStateFlow()

    var state by mutableStateOf(SnakeGameState())
        private set


    fun onEvent(event: SnakeGameEvent){
        when(event){
            is SnakeGameEvent.PauseGame -> {
                state = state.copy(gameState = GameState.PAUSE)
                }


            is SnakeGameEvent.ResetGame -> {
                state = SnakeGameState()
            }

            is SnakeGameEvent.StartGame -> {
                state = state.copy(gameState = GameState.Start)
                viewModelScope.launch {
                    while (state.gameState == GameState.Start) {
                        val delayMillis = when (state.snake.size) {
                            in 1..5 -> 300L
                            in 6..10 -> 280L
                            in 11..15 -> 250L
                            in 16..20 -> 210L
                            in 21..25 -> 150L
                            in 26..30 -> 100L
                            else -> 50L
                        }
                        delay(delayMillis)
                        state = updateGame(state)
                    }
                }
            }

            is SnakeGameEvent.UpdateDirection -> {
                updateDirection(event.offset, event.canvasWidth)
            }
        }
    }

    private fun updateDirection(offset: Offset, canvasWidth: Int) {
        if (!state.isGameOver) {
            val cellSize = canvasWidth / state.xAxisGridSize
            val tapX = (offset.x / cellSize).toInt()
            val tapY = (offset.y / cellSize).toInt()
            val head = state.snake.first()

            state =
                state.copy(
                    direction = when (state.direction) {
                        Direction.UP, Direction.DOWN -> {
                            if (tapX < head.x) Direction.LEFT else Direction.RIGHT
                        }

                        Direction.LEFT, Direction.RIGHT -> {
                            if (tapY < head.y) Direction.UP else Direction.DOWN
                        }
                    }
                )
        }
    }

    private fun updateGame(currentGame: SnakeGameState): SnakeGameState {
        if (currentGame.isGameOver) {
            return currentGame
        }

        val head = currentGame.snake.first()
        val xAxisGridSize = currentGame.xAxisGridSize
        val yAxisGridSize = currentGame.yAxisGridSize

        //Update movement of snake
        val newHead = when (currentGame.direction) {
            Direction.UP -> Coordinate(x = head.x, y = (head.y - 1))
            Direction.DOWN -> Coordinate(x = head.x, y = (head.y + 1))
            Direction.LEFT -> Coordinate(x = head.x - 1, y = (head.y))
            Direction.RIGHT -> Coordinate(x = head.x + 1, y = (head.y))
        }

        //Check if the snake collides with itself or goes out of bounds
        if (
            currentGame.snake.contains(newHead) ||
            !isWithinBounds(newHead, xAxisGridSize, yAxisGridSize)
        ) {
            return currentGame.copy(isGameOver = true)
        }

        //Check if the snake eats the food
        var newSnake = mutableListOf(newHead) + currentGame.snake
        val newFood = if (newHead == currentGame.food) SnakeGameState.generateRandomFoodCoordinate()
        else currentGame.food

        //Update snake length
        if (newHead != currentGame.food) {
            newSnake = newSnake.toMutableList()
            newSnake.removeAt(newSnake.size - 1)
        }

        return currentGame.copy(snake = newSnake, food = newFood)
    }

    private fun isWithinBounds(
        coordinate: Coordinate,
        xAxisGridSize: Int,
        yAxisGridSize: Int
    ): Boolean {
        return coordinate.x in 1 until xAxisGridSize - 1
                && coordinate.y in 1 until yAxisGridSize - 1
    }
}

