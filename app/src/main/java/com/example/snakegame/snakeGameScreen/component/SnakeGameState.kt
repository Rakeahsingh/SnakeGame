package com.example.snakegame.snakeGameScreen.component

import kotlin.random.Random

data class SnakeGameState(
    val xAxisGridSize: Int = 20,
    val yAxisGridSize: Int = 30,
    val direction: Direction = Direction.RIGHT,
    val snake: List<Coordinate> = listOf(Coordinate(5,5)),
    val food: Coordinate = generateRandomFoodCoordinate(),
    val isGameOver: Boolean = false,
    val gameState: GameState = GameState.IDLE
){
    companion object{
        fun generateRandomFoodCoordinate(): Coordinate{
            return Coordinate(
                x = Random.nextInt(from = 1 , until = 19),
                y = Random.nextInt(from = 1 , until = 29)
            )
        }
    }
}



enum class Direction{
    UP,
    DOWN,
    LEFT,
    RIGHT
}

data class Coordinate(
        val x: Int,
        val y: Int
)

enum class GameState{
    IDLE,
    PAUSE,
    Start
}

