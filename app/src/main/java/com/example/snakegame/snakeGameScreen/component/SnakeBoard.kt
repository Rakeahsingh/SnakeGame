package com.example.snakegame.snakeGameScreen.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.snakegame.snakeGameScreen.SnakeGameViewModel
import com.example.snakegame.ui.theme.Custard
import com.example.snakegame.ui.theme.RoyalBlue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snakegame.R
import com.example.snakegame.ui.theme.Crin

@Composable
fun SnakeBoard(
    viewModel: SnakeGameViewModel = viewModel()
) {

    val state = viewModel.state

    val foodImage = ImageBitmap.imageResource(id = R.drawable.img_apple)
    val snakeHeadImage = when(state.direction){
        Direction.UP -> ImageBitmap.imageResource(id = R.drawable.img_snake_head3)
        Direction.DOWN -> ImageBitmap.imageResource(id = R.drawable.img_snake_head4)
        Direction.LEFT -> ImageBitmap.imageResource(id = R.drawable.img_snake_head2)
        Direction.RIGHT -> ImageBitmap.imageResource(id = R.drawable.img_snake_head)
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2 / 3f)
            .pointerInput(state.gameState){
                if (state.gameState != GameState.Start){
                    return@pointerInput
                }
                detectTapGestures { offset ->
                    viewModel.onEvent(
                        SnakeGameEvent.UpdateDirection(offset, size.width)
                    )
                }
            }
    ){
        val cellSize = size.width / 20

        drawGameBoard(
            cellSize = cellSize,
            cellColor = Custard,
            boardCellColor = RoyalBlue,
            gridHeight = state.yAxisGridSize,
            gridWidth = state.xAxisGridSize,
        )

        drawFood(
            foodImage = foodImage,
            cellSize = cellSize.toInt(),
            coordinate = state.food
        )

        drawSnake(
            snakeHeadImage = snakeHeadImage,
            cellSize = cellSize,
            snake = state.snake
        )

    }

}



private fun DrawScope.drawGameBoard(
    cellSize: Float,
    cellColor: Color,
    boardCellColor: Color,
    gridWidth: Int,
    gridHeight: Int
    ){

    for (i in 0 until gridWidth){
        for (j in 0 until gridHeight){

            val isBorderCell = i == 0 || j == 0 || i == gridWidth -1 || j == gridHeight -1
            drawRect(
                color = if (isBorderCell) boardCellColor
                        else if((i + j) % 2 == 0) cellColor
                        else cellColor.copy(alpha = 0.5f),
                topLeft = Offset(
                    x = i * cellSize,
                    y = j * cellSize
                ),
                size = Size(width = cellSize, height = cellSize)
            )


        }
    }

}


private fun DrawScope.drawFood(
    foodImage: ImageBitmap,
    cellSize: Int,
    coordinate: Coordinate
){

    drawImage(
        image = foodImage,
        dstSize = IntSize(width = cellSize, height = cellSize),
        dstOffset = IntOffset(
            x = (coordinate.x * cellSize),
            y = (coordinate.y * cellSize)
        )
    )

}

private fun DrawScope.drawSnake(
    snakeHeadImage: ImageBitmap,
    cellSize: Float,
    snake: List<Coordinate>
){

    snake.forEachIndexed { index, coordinate ->
        val cellSizeInt = cellSize.toInt()
        val radius = if (index == snake.lastIndex) cellSize / 2.5f else cellSize / 2
        if (index == 0){
            drawImage(
                image = snakeHeadImage,
                dstSize = IntSize(width = cellSizeInt, height = cellSizeInt),
                dstOffset = IntOffset(
                    x = (coordinate.x * cellSizeInt),
                    y = (coordinate.y * cellSizeInt)
                )
            )
        }else{
            drawCircle(
                color = Crin,
                radius = radius,
                center = Offset(
                    x = (coordinate.x * cellSize) + radius,
                    y = (coordinate.y * cellSize) + radius
                )
            )
        }
    }

}


@Preview
@Composable
fun Preview() {

    SnakeBoard()

}