package com.sychev.kosnake

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View

class SnakeDrawer(context: Context?) : View(context), SnakeLogic.EventHandler {

    private var onPause: Boolean = false
    private var menuPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
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
    private var pauseRect: Rect = Rect()

    private var menuRects: MutableList<Rect> = mutableListOf()
    private var drawerHandler: DrawerHandler? = null

    interface DrawerHandler {
        fun exitGame()
    }

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

        menuPaint.style = Paint.Style.FILL
        menuPaint.color = Color.GREEN

        blinkHandler = Handler(Looper.myLooper()!!)
        snakeBlinker = Runnable {
            currentBlink++
            invalidate()

            if (currentBlink > 8 || blinkComplete) {
                blinkComplete = true
                currentBlink = 0
            } else {
                snakeBlinker?.let { blinkHandler?.postDelayed(it, 500) }
            }
        }
    }

    fun setDrawerHandler(drawerHandler: DrawerHandler) {
        this.drawerHandler = drawerHandler
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

        if (snakeAlive && !onPause) {
            drawSnake(canvas)
        } else if (snakeAlive && onPause) {
            drawMenu(canvas, true)
        } else if (!snakeAlive) {
            if (blinkComplete) {
                drawMenu(canvas, false)
            } else {
                if (currentBlink % 2 == 0) {
                    drawSnake(canvas)
                }
            }
        }
    }

    private fun drawScore(canvas: Canvas?) {
        val scoreText: String = snake?.score.toString()
        val scoreWidth = scorePaint.measureText(scoreText)
        canvas?.drawText(scoreText,
            (width - scoreWidth) / 2,
            (cubeSize * 2).toFloat(),
            scorePaint)

        canvas?.drawText("Pause", width - scorePaint.measureText("Pause Pause"),
            (cubeSize * 2).toFloat(), scorePaint)
        pauseRect.left = (width - scorePaint.measureText("Pause Pause")).toInt()
        pauseRect.right = width
        pauseRect.top = 0
        pauseRect.bottom = cubeSize * 3
    }

    private fun drawBorders(canvas: Canvas?) {
        canvas?.drawRect(1F,
            1F + bottomBarSize.toFloat(),
            (xMax * (cubeSize + 1) - thinkness).toFloat(),
            (yMax * (cubeSize + 1) - thinkness + bottomBarSize).toFloat(),
            borderPaint)
    }

    private fun drawMenu(canvas: Canvas?, pause: Boolean) {

        menuPaint.textSize = (height / 10).toFloat()
        val menuItems: List<String> = if (pause) arrayListOf("Continue", "Exit") else arrayListOf("Restart", "Exit")
        menuRects.clear()
        for (i in 0 until menuItems.count()) {
            var bound: Rect = Rect()
            menuPaint.getTextBounds(menuItems[i], 0, menuItems[i].length, bound)
            bound.offset(((width - menuPaint.measureText(menuItems[i])) / 2).toInt(),
                i * height / 6 + height / 2)
            menuRects.add(bound)
            canvas?.drawText(menuItems[i], bound.left.toFloat(), bound.bottom.toFloat(), menuPaint)
        }

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
        currentBlink = 0
        blinkComplete = false
        snakeBlinker?.let { blinkHandler!!.postDelayed(it, 500) }
//        snake!!.resetSnake()
    }

    override fun updateView() {
        invalidate()
    }

    fun click(x: Float, y: Float) {
        Log.d("Snake", "Tap $snakeAlive")

        if (snakeAlive && !onPause) {
            if (pauseRect.contains(x.toInt(), y.toInt())) {
                setPauseState()
            } else {
                snake?.makeStep()
            }
        } else if (snakeAlive && onPause) {
            if (menuRects[0].contains(x.toInt(), y.toInt())) {
                onPause = false
                snake?.setPause(false)
            } else if (menuRects[1].contains(x.toInt(), y.toInt())) {
                drawerHandler?.exitGame()
            }
        } else {
            if (blinkComplete) {
                if (menuRects[0].contains(x.toInt(), y.toInt())) {
                    snakeAlive = true
                    snake?.resetSnake()
                } else if (menuRects[1].contains(x.toInt(), y.toInt())) {
                    drawerHandler?.exitGame()
                }
            }
        }
    }

    fun setPauseState() {
        snake?.setPause(true)
        onPause = true
        invalidate()
    }
}