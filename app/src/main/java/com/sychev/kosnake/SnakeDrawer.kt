package com.sychev.kosnake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View

class SnakeDrawer(context: Context?) : View(context), SnakeLogic.EventHandler {

    private var snakeBlinker: Runnable? = null
    private var blinkHandler: Handler? = null
    private var currentBlink: Int = 0
    private var blinkComplete: Boolean = false
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
    var snakeAlive: Boolean = true

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

        blinkHandler = Handler(Looper.myLooper()!!)
        snakeBlinker = Runnable {
            currentBlink++
            invalidate()

            if (currentBlink > 8 || blinkComplete) {
                blinkComplete = true
                currentBlink = 0
            } else {
                snakeBlinker?.let { blinkHandler!!.postDelayed(it, 500) }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > h) {
            cubeSize = (h - bottomBarSize) / maxCubeNumber - 1
            xMax = w / (cubeSize + 1)
        } else {
            cubeSize = w / maxCubeNumber - 1
            yMax = (h - bottomBarSize) / (cubeSize + 1)
        }
        thinkness = (cubeSize / 10).toFloat()
        snake!!.setMaxSize(xMax, yMax)
    }

    fun setSnake(snake: SnakeLogic) {
        this.snake = snake
        snake.snakeHandler = this
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBorders(canvas)
        drawScore(canvas)

        if (snakeAlive) {
            drawSnake(canvas)
        } else {
            if (blinkComplete) {
                drawMenu(canvas)
            } else {
                if (currentBlink % 2 == 0) {
                    drawSnake(canvas)
                }
            }
        }
    }

    private fun drawScore(canvas: Canvas?) {
        val scoreText: String = snake!!.score.toString()
        val scoreWidth = scorePaint.measureText(scoreText)
        canvas?.drawText(scoreText,
            (width - scoreWidth) / 2,
            (cubeSize * 2).toFloat(),
            scorePaint)
    }

    private fun drawBorders(canvas: Canvas?) {
        canvas?.drawRect(1F,
            1F + bottomBarSize.toFloat(),
            (xMax * (cubeSize + 1) - thinkness).toFloat(),
            (yMax * (cubeSize + 1) - thinkness + bottomBarSize).toFloat(),
            borderPaint)
    }

    private fun drawMenu(canvas: Canvas?) {
        canvas!!.drawText("RESTART", 300f, 300f, snakePaint)
    }

    private fun drawSnake(canvas: Canvas?) {
        for (i in 0 until snake!!.getLength()) {
            drawSnakeCube(canvas, snake!!.getSnakePos(i))
        }
        drawAppleCube(canvas, snake!!.getApplePoint());
    }

    private fun drawAppleCube(canvas: Canvas?, applePoint: Point) {
        drawCube(canvas, applePoint, applePaint)
    }

    private fun drawSnakeCube(canvas: Canvas?, pos: Point) {
        drawCube(canvas, pos, snakePaint)
    }

    private fun drawCube(canvas: Canvas?, pos: Point, paint: Paint) {
        paint.style = Paint.Style.STROKE;
        canvas?.drawRect((pos.x * cubeSize + thinkness).toFloat(),
            (pos.y * cubeSize + bottomBarSize + thinkness).toFloat(),
            ((pos.x + 1) * cubeSize).toFloat() - thinkness,
            ((pos.y + 1) * cubeSize + bottomBarSize + thinkness).toFloat() - thinkness * 2,
            paint)
        paint.style = Paint.Style.FILL
        canvas?.drawRect((pos.x * cubeSize + thinkness * 3).toFloat(),
            (pos.y * cubeSize + thinkness * 3 + bottomBarSize).toFloat(),
            ((pos.x + 1) * cubeSize).toFloat() - thinkness * 3,
            ((pos.y + 1) * cubeSize + bottomBarSize).toFloat() - thinkness * 3,
            paint)
    }

    override fun snakeDie() {
        snakeAlive = false
        snakeBlinker?.let { blinkHandler!!.postDelayed(it, 500) }
//        snake!!.resetSnake()
    }

    override fun updateView() {
        invalidate()
    }

    fun click(x: Float, y: Float) {
        Log.d("Snake", "Tap $snakeAlive")

        if (snakeAlive)
            snake?.makeStep()
        else {
            blinkComplete = true
            snakeAlive = true
            snake?.resetSnake()
        }
    }
}