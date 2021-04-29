package com.sychev.kosnake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.view.View

class SnakeDrawer(context: Context?) : View(context) {

    private var applePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var snakePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var cubeSize: Int = 0
    private val maxCubeNumber = 20
    private var snake: SnakeLogic? = null;
    private var thinkness: Float = 10F

    init {
        if (context != null) {
            cubeSize =
                if (context.resources.displayMetrics.widthPixels > context.resources.displayMetrics.heightPixels) {
                    context.resources.displayMetrics.heightPixels / maxCubeNumber
                } else {
                    context.resources.displayMetrics.widthPixels / maxCubeNumber
                } - 1
            thinkness = (cubeSize/10).toFloat()
        }

        snakePaint.style = Paint.Style.STROKE
        snakePaint.color = Color.GREEN
        snakePaint.strokeWidth = thinkness
        applePaint.style = Paint.Style.STROKE
        applePaint.strokeWidth = thinkness
        applePaint.color = Color.RED
    }

    fun setSnake(snake: SnakeLogic) {
        this.snake = snake
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null && snake != null) {
            for (i in 0 until snake!!.getLength()) {
                drawSnakeCube(canvas, snake!!.getSnakePos(i))
            }
            drawAppleCube(canvas, snake!!.getApplePoint());
        }
    }

    private fun drawAppleCube(canvas: Canvas?, applePoint: Point) {
        drawCube(canvas, applePoint, applePaint)
    }

    fun drawSnakeCube(canvas: Canvas?, pos: Point) {
        drawCube(canvas, pos, snakePaint)
    }

    fun drawCube(canvas: Canvas?, pos: Point, paint: Paint) {
        paint.style = Paint.Style.STROKE;
        canvas?.drawRect((pos.x * cubeSize).toFloat(),
            (pos.y * cubeSize).toFloat(),
            ((pos.x + 1) * cubeSize).toFloat() - thinkness * 2,
            ((pos.y + 1) * cubeSize).toFloat() - thinkness * 2,
            paint)
        paint.style = Paint.Style.FILL
        canvas?.drawRect((pos.x * cubeSize + thinkness * 2).toFloat(),
            (pos.y * cubeSize + thinkness * 2).toFloat(),
            ((pos.x + 1) * cubeSize).toFloat() - thinkness * 4,
            ((pos.y + 1) * cubeSize).toFloat() - thinkness * 4,
            paint)
    }
}