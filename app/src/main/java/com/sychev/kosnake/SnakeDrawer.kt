package com.sychev.kosnake

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.graphics.withTranslation
import kotlin.math.roundToInt

class SnakeDrawer(context: Context?, initialCubeNumber: Int) : View(context),
    SnakeLogic.EventHandler {

    private var onPause: Boolean = false
    private var menuPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var snakeBlinker: Runnable? = null
    private var blinkHandler: Handler? = null
    private var currentBlink: Int = 0
    private var blinkComplete: Boolean = false
    private var applePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var snakePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var scorePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var cubeSize: Float = 0f
    private var snake: SnakeLogic? = null
    private var maxCubeNumber: Int = 20
    private var thinkness: Float = 0.0f
    private val borderThinkness = 8f
    var xMax: Int = maxCubeNumber
    var yMax: Int = maxCubeNumber
    private var topBarSize: Int = 0
    private var snakeAlive: Boolean = true
    private var pauseRect: Rect = Rect()
    private var borderRect: Rect = Rect()
    private var menuRects: MutableList<Rect> = mutableListOf()
    private var drawerHandler: DrawerHandler? = null
    private var bottomSeek: Int = 0

    interface DrawerHandler {
        fun exitGame()
        fun newRecord(score: Int)
    }

    init {
        maxCubeNumber = initialCubeNumber

        val w = context!!.resources.displayMetrics.widthPixels
        val h = context.resources.displayMetrics.heightPixels
        onSizeChanged(w, h, 0, 0)
        setupPainers()

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

    override fun newRecord(score: Int) {
        drawerHandler!!.newRecord(score)
    }

    private fun setupPainers() {
        snakePaint.style = Paint.Style.STROKE
        snakePaint.color = Color.GREEN
        applePaint.style = Paint.Style.STROKE
        applePaint.color = Color.RED
        borderPaint.style = Paint.Style.STROKE
        borderPaint.color = Color.BLUE
        scorePaint.style = Paint.Style.FILL
        scorePaint.color = Color.YELLOW
        menuPaint.style = Paint.Style.FILL
        menuPaint.color = Color.GREEN
        updatePainterWidth()
    }

    fun setDrawerHandler(drawerHandler: DrawerHandler) {
        this.drawerHandler = drawerHandler
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        xMax = maxCubeNumber
        yMax = maxCubeNumber
        topBarSize = h / 10

        if (w > h) {
            cubeSize = (((h - topBarSize - 2 * borderThinkness) / maxCubeNumber) * 0.75).toFloat()
            thinkness = (cubeSize / 7)
            xMax = ((w - getBottomBarSize()) / (cubeSize)).roundToInt()
        } else {
            cubeSize = (w - 2 * borderThinkness) * 8 / 10 / maxCubeNumber
            thinkness = (cubeSize / 8)
            yMax =
                ((h - topBarSize - 2 * borderThinkness - thinkness) / (cubeSize + 2 * thinkness)).roundToInt()
        }
        Log.d("Painter", "Set $xMax and $yMax cubesize if $cubeSize, h is $h $topBarSize")
//        thinkness = (cubeSize / 10).toFloat()
        if (snake != null)
            snake?.setMaxSize(xMax, yMax)
        borderRect = Rect((thinkness / 2).toInt(),
            (thinkness / 2 + topBarSize).toInt(),
            (w - thinkness / 2).toInt(),
            (h - thinkness / 2).toInt())

        bottomSeek = (h - yMax * (cubeSize + thinkness * 2) - topBarSize - borderThinkness).toInt()

        updatePainterWidth()
    }

    private fun updatePainterWidth() {
        borderPaint.strokeWidth = borderThinkness
        applePaint.strokeWidth = thinkness
        snakePaint.strokeWidth = thinkness
    }

    fun setSnake(snake: SnakeLogic) {
        this.snake = snake
        snake.snakeHandler = this
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val xposDev = width - (xMax) * (cubeSize + thinkness * 2) - borderThinkness * 2
        canvas?.translate(xposDev / 2, bottomSeek.toFloat())
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

    private fun getBottomBarSize(): Int {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    private fun drawScore(canvas: Canvas?) {
        val scoreText: String = snake?.score.toString()
        val scoreWidth = scorePaint.measureText(scoreText)
        scorePaint.textSize = (topBarSize / 3).toFloat()
        canvas?.drawText(scoreText,
            (width - scoreWidth) / 2,
            (topBarSize / 3).toFloat(),
            scorePaint)

        canvas?.drawText("Pause", width - scorePaint.measureText("Pause Pause"),
            (topBarSize / 3).toFloat(), scorePaint)
        pauseRect.left = (width - scorePaint.measureText("Pause Pause")).toInt()
        pauseRect.right = width
        pauseRect.top = 0
        pauseRect.bottom = (cubeSize * 3).toInt()
    }

    private fun drawBorders(canvas: Canvas?) {
        canvas?.drawRect(borderThinkness / 2,
            topBarSize.toFloat() + borderThinkness / 2,
            xMax * (cubeSize + thinkness * 2) + borderThinkness,
            yMax * (cubeSize + thinkness * 2) + topBarSize + borderThinkness,
            borderPaint)
    }

    private fun drawMenu(canvas: Canvas?, pause: Boolean) {

//        menuPaint.textSize = (height / 10).toFloat()
        val lastRecord: Int = snake!!.getHighscore()
        val currentScore: Int = snake!!.score
        var scoreString: String = if (lastRecord < currentScore) "New record!!!\n" else ""
        scoreString += "You score is " + snake?.score.toString() + "!"


        val menuItems: List<String> =
            if (pause) arrayListOf("Continue", "Exit") else arrayListOf(scoreString,
                "Restart",
                "Exit")
        menuRects.clear()

        val heightSplit = (height / 20).toFloat()
        val vertBorder =
            ((height - (menuItems.size - 1) * heightSplit) / (menuItems.size + 2))
        menuPaint.textSize = vertBorder / 4
        for (i in 0 until menuItems.count()) {
            val y = vertBorder * (i + 1.5f) + i * heightSplit
            val rectangle = Rect(((width - menuPaint.measureText(menuItems[i])) / 2).toInt(),
                (y - vertBorder).toInt(),
                ((width + menuPaint.measureText(menuItems[i])) / 2).toInt(),
                y.toInt())

            menuRects.add(rectangle)
            canvas?.drawText(menuItems[i], menuRects[i].left.toFloat(),
                menuRects[i].top.toFloat() + vertBorder, menuPaint)

        }
    }

    private fun drawSnake(canvas: Canvas?) {
        for (i in 0 until snake!!.getLength()) {
            drawSnakeCube(canvas, snake!!.getSnakePos(i))
        }
        drawAppleCube(canvas, snake!!.getApplePoint())
    }

    private fun drawAppleCube(canvas: Canvas?, applePoint: Point) {
        drawCube(canvas, applePoint, applePaint)
    }

    private fun drawSnakeCube(canvas: Canvas?, pos: Point) {
        drawCube(canvas, pos, snakePaint)
    }

    private fun drawCube(canvas: Canvas?, pos: Point, paint: Paint) {
        paint.style = Paint.Style.STROKE

        canvas?.withTranslation(borderThinkness, borderThinkness + topBarSize) {

            canvas.drawRect((pos.x * (cubeSize + thinkness * 2) + thinkness / 2),
                (pos.y * (cubeSize + thinkness * 2) + thinkness / 2),
                ((pos.x + 1) * (cubeSize + thinkness * 2) - 2 * thinkness),
                ((pos.y + 1) * (cubeSize + thinkness * 2) - 2 * thinkness),
                paint)
            paint.style = Paint.Style.FILL
            canvas.drawRect((pos.x * (cubeSize + thinkness * 2) + thinkness * 5 / 2),
                (pos.y * (cubeSize + thinkness * 2) + thinkness * 5 / 2),
                ((pos.x + 1) * (cubeSize + thinkness * 2) - 4 * thinkness),
                ((pos.y + 1) * (cubeSize + thinkness * 2) - 4 * thinkness),
                paint)
        }
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
                if (menuRects[1].contains(x.toInt(), y.toInt())) {
                    snakeAlive = true
                    snake?.resetSnake()
                } else if (menuRects[2].contains(x.toInt(), y.toInt())) {
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