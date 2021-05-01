package com.sychev.kosnake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import android.view.View

class SnakeDrawer(context: Context?) : View(context) {

    private var applePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var snakePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var scorePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var cubeSize: Int = 0
    private val maxCubeNumber = 30
    private var snake: SnakeLogic? = null;
    private var thinkness: Float = 10F
    var xMax: Int = maxCubeNumber
    var yMax: Int = maxCubeNumber
    private var bottomBarSize: Int = 0

    init {
        if (context != null) {
            if (context.resources.displayMetrics.widthPixels > context.resources.displayMetrics.heightPixels) {
                cubeSize = context.resources.displayMetrics.heightPixels / maxCubeNumber - 1
                xMax = context.resources.displayMetrics.widthPixels / cubeSize
            } else {
                cubeSize = context.resources.displayMetrics.widthPixels / maxCubeNumber - 1
                yMax = context.resources.displayMetrics.heightPixels / cubeSize
            }
            thinkness = (cubeSize / 10).toFloat()

            scorePaint.textSize = cubeSize.toFloat()
            bottomBarSize = cubeSize * 3// I don't know why
        }

        snakePaint.style = Paint.Style.STROKE
        snakePaint.color = Color.GREEN
        snakePaint.strokeWidth = thinkness
        applePaint.style = Paint.Style.STROKE
        applePaint.strokeWidth = thinkness
        applePaint.color = Color.RED

        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = thinkness
        borderPaint.color = Color.BLUE

        scorePaint.style = Paint.Style.FILL
        scorePaint.color = Color.YELLOW
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > h) {
            cubeSize = h / maxCubeNumber - 1
            xMax = w / cubeSize
        } else {
            cubeSize = w / maxCubeNumber - 1
            yMax = h / cubeSize
        }
        thinkness = (cubeSize / 10).toFloat()
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
        canvas?.drawRect(1F,
            1F + bottomBarSize.toFloat(),
            (xMax * (cubeSize + 1) - thinkness).toFloat(),
            (yMax * (cubeSize + 1) - thinkness * 20).toFloat(),
            borderPaint)

        val scoreText: String = snake!!.score.toString()
        val scoreWidth = scorePaint.measureText(scoreText)
        canvas?.drawText(scoreText, (width - scoreWidth) / 2, (cubeSize * 2).toFloat(), scorePaint)
        Log.d("Painter", "Size $width, $height " + (xMax * (cubeSize + 1) - thinkness).toString()
                + "  " + (yMax * (cubeSize + 1) - thinkness).toString())

    }

    private fun drawAppleCube(canvas: Canvas?, applePoint: Point) {
        drawCube(canvas, applePoint, applePaint)
    }

    private fun drawSnakeCube(canvas: Canvas?, pos: Point) {
        drawCube(canvas, pos, snakePaint)
    }

    private fun drawCube(canvas: Canvas?, pos: Point, paint: Paint) {
        paint.style = Paint.Style.STROKE;
        canvas?.drawRect((pos.x * cubeSize).toFloat(),
            (pos.y * cubeSize + bottomBarSize).toFloat(),
            ((pos.x + 1) * cubeSize).toFloat() - thinkness * 2,
            ((pos.y + 1) * cubeSize + bottomBarSize).toFloat() - thinkness * 2,
            paint)
        paint.style = Paint.Style.FILL
        canvas?.drawRect((pos.x * cubeSize + thinkness * 2).toFloat(),
            (pos.y * cubeSize + thinkness * 2 + bottomBarSize).toFloat(),
            ((pos.x + 1) * cubeSize).toFloat() - thinkness * 4,
            ((pos.y + 1) * cubeSize + bottomBarSize).toFloat() - thinkness * 4,
            paint)
    }
}