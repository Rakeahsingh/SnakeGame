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

    var state by mutableStateOf(SnakeGameState())
        private set


    fun onEvent(event: SnakeGameEvent){
        when(event){
            is SnakeGameEvent.PauseGame -> {
                state = state.copy(
                    gameState = GameState.PAUSE
                )
            }

            is SnakeGameEvent.ResetGame -> {
                state = SnakeGameState()
            }

            is SnakeGameEvent.StartGame -> {
                state = state.copy(
                    gameState = GameState.Start
                )

                viewModelScope.launch {
                    while (state.gameState == GameState.Start){
                        val delayMillis = when(state.snake.size){
                            in 1..5 -> 120L
                            in 6..10 -> 100L
                            else -> 80L
                        }
                      delay(delayMillis)
                      state =  updateGame(state)
                    }
                }
            }

            is SnakeGameEvent.UpdateDirection -> {
                updateDirection(event.offset, event.canvasWidth)
            }
        }
    }

    private fun updateDirection(
        offset: Offset,
        canvasWidth: Int
    ) {
        if (!state.isGameOver){
            val cellSize = canvasWidth / state.xAxisGridSize
            val tapX = (offset.x - cellSize).toInt()
            val tapY = (offset.y - cellSize).toInt()
            val head = state.snake.first()

            state = state.copy(
                direction = when (state.direction){
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

    private fun updateGame(currentGame: SnakeGameState): SnakeGameState{
        if (currentGame.isGameOver){
            return currentGame
        }

        val head = currentGame.snake.first()
        val xAxisGridSize = currentGame.xAxisGridSize
        val yAxisGridSize = currentGame.yAxisGridSize

        // update snake movement
        val newHead = when(currentGame.direction){
            Direction.UP -> {
                Coordinate(x = head.x, y = head.y -1)
            }
            Direction.DOWN -> {
                Coordinate(x = head.x, y = head.y +1)
            }
            Direction.LEFT -> {
                Coordinate(x = head.x -1, y = head.y)
            }
            Direction.RIGHT -> {
                Coordinate(x = head.x +1, y = head.y)
            }
        }

        // check if snake collides itself or goes out of bounds
        if (
            currentGame.snake.contains(newHead) ||
            isWithinBounds(coordinate = newHead, xAxisGridSize = xAxisGridSize, yAxisGridSize = yAxisGridSize)
            ){
            return currentGame.copy(isGameOver = true)
        }

        // check if snake Eat the food
        var newSnake = mutableListOf(newHead) + currentGame.snake
        val newFood = if (newHead == currentGame.food) SnakeGameState.generateRandomFoodCoordinate()
                      else currentGame.food

        // update snake length
        if (newHead != currentGame.food){
            newSnake = newSnake.toMutableList()
            newSnake.removeAt(newSnake.size -1)
        }

        return currentGame.copy(snake = newSnake, food = newFood)
    }


    private fun isWithinBounds(
        coordinate: Coordinate,
        xAxisGridSize: Int,
        yAxisGridSize: Int
    ): Boolean{
        return coordinate.x in 1 until xAxisGridSize -1 &&
                coordinate.y in 1 until yAxisGridSize -1
    }

}