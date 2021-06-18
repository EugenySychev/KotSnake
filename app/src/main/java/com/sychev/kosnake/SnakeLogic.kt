package com.sychev.kosnake

import android.graphics.Point
import java.lang.Exception
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SnakeLogic(
    private var xMax: Int,
    private var yMax: Int,
    private var length: Int
) {

    private val initialTimer = 500L
    private var timePeriod: Long = initialTimer
    private var snakeAlive: Boolean = false
    private lateinit var applePoint: Point
    private var snakePos: MutableList<Point> = mutableListOf()
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var onPause: Boolean = false
    private var highscore: Int = 0
    var snakeHandler: EventHandler? = null
        set(value) {
            field = value
        }
    private lateinit var soundHandler: SoundHandler
    var score: Int = 0

    private var direction: MoveDirection = MoveDirection.UP

    enum class MoveDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    interface EventHandler {
        fun snakeDie()
        fun updateView()
        fun newRecord(score: Int)
    }

    interface SoundHandler {
        fun onStepSound()
        fun onEatSound()
        fun onDieSound()
    }


    init {
        if (xMax < length + 2) {
            throw Exception("Very big length of snake, unable to locate it")
        } else {
            resetSnake()
        }
        handler = Handler(Looper.myLooper()!!)
        runnable = Runnable {
            makeStep()
            runnable?.let { handler!!.postDelayed(it, timePeriod) }
        }
        runnable?.let { handler!!.postDelayed(it, timePeriod) }

        generateNewApple()
    }

    fun resetSnake() {
        snakePos.clear()
        snakePos.add(Point((xMax - length) / 2, yMax / 2))

        for (i in 1 until length) {
            snakePos.add(Point(snakePos[i - 1].x - 1, snakePos[i - 1].y))
        }
        score = 0
        direction = MoveDirection.RIGHT
        snakeAlive = true
        timePeriod = initialTimer
    }

    fun generateNewApple() {
        applePoint = Point(Random.nextInt(xMax), Random.nextInt((yMax)))

        if (snakePos.count() > 0) {
            while (snakePos.indexOf(applePoint) != -1)
                applePoint = Point(Random.nextInt(xMax), Random.nextInt((yMax)))
        }
    }

    fun changeDirection(newDirection: MoveDirection) {
        if ((direction == MoveDirection.UP && newDirection != MoveDirection.DOWN) ||
            (direction == MoveDirection.DOWN && newDirection != MoveDirection.UP) ||
            (direction == MoveDirection.LEFT && newDirection != MoveDirection.RIGHT) ||
            (direction == MoveDirection.RIGHT && newDirection != MoveDirection.LEFT)
        ) {
            direction = newDirection
        }
    }

    fun getSnakePos(index: Int) =
        if (index < snakePos.count())
            snakePos[index]
        else
            Point(0, 0)


    fun makeStep() {

        if (snakeAlive && !onPause) {

            var newPoint: Point = Point()
            val lastPoint = Point(snakePos.last())

            newPoint.x = snakePos[0].x
            newPoint.y = snakePos[0].y
            when (direction) {
                MoveDirection.UP -> newPoint.y -= 1
                MoveDirection.DOWN -> newPoint.y += 1
                MoveDirection.LEFT -> newPoint.x -= 1
                MoveDirection.RIGHT -> newPoint.x += 1
            }

            if (newPoint.x >= xMax || newPoint.x < 0 ||
                newPoint.y >= yMax || newPoint.y < 0
            )
                snakeBeginDie()
            else {
                for (i in snakePos.count() - 1 downTo 1) {
                    snakePos[i].x = snakePos[i - 1].x
                    snakePos[i].y = snakePos[i - 1].y
                }
                snakePos[0].x = newPoint.x
                snakePos[0].y = newPoint.y
            }

            if (snakePos[0] == applePoint) {
                snakePos.add(lastPoint)
                generateNewApple()
                increaseScore()
                soundHandler.onEatSound()
            } else {
                soundHandler.onStepSound()
            }


            for (i in 1 until snakePos.count()) {
                if (snakePos[0] == snakePos[i]) {
                    snakeBeginDie()
                }
            }
            if (snakeHandler != null)
                snakeHandler!!.updateView()
        }
    }

    private fun increaseScore() {
        score++
        timePeriod = (timePeriod / 1.125f).toLong()
    }

    private fun snakeBeginDie() {
        snakeAlive = false
        if (snakeHandler != null && soundHandler != null)
        {
            snakeHandler!!.snakeDie()
            soundHandler!!.onDieSound()
            if (score > highscore)
                snakeHandler!!.newRecord(score)
        }
    }


    fun getLength(): Int {
        return snakePos.count()
    }

    fun getApplePoint(): Point {
        return applePoint
    }

    fun setMaxSize(xMax: Int, yMax: Int) {
        this.xMax = xMax
        this.yMax = yMax
        Log.d("SNAKE", "Set size $xMax, $yMax")
        generateNewApple()
    }

    fun setPause(b: Boolean) {
        onPause = b
    }

    fun setSoundHandler(handler: SoundHandler) {
        soundHandler = handler
    }

    fun setHighscore(highsc: Int) {
        highscore = highsc
    }

    fun getHighscore(): Int {
        return highscore
    }
}