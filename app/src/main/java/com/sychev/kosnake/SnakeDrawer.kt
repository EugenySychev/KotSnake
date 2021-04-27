package com.sychev.kosnake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class SnakeDrawer(context: Context) : View(context){

    var snakePaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var cubeSize: Int = 0
    private val maxCubeNumber = 20
    private var snake : SnakeLogic? = null;

    init {
        cubeSize = if (context.resources.displayMetrics.widthPixels > context.resources.displayMetrics.heightPixels) {
            context.resources.displayMetrics.heightPixels / maxCubeNumber
        } else {
            context.resources.displayMetrics.widthPixels / maxCubeNumber
        }
    }

    fun setSnake(snake: SnakeLogic) {
        this.snake = snake
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null && snake != null) {
            for(i in 0 until snake!!.getLength()) {
                drawSnakeCube(canvas, snake!!.getX()[i].toFloat(), snake!!.getY()[i].toFloat() )
            }
        }
    }

    fun drawSnakeCube(canvas: Canvas?, x: Float, y: Float){
        canvas?.drawRect(x, y, (x + cubeSize).toFloat(), (y + cubeSize).toFloat(), snakePaint)
    }
}