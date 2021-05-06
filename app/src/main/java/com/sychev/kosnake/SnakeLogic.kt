package com.sychev.kosnake

import android.graphics.Point
import java.lang.Exception
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private var onPause : Boolean = false
    var snakeHandler: EventHandler? = null
        set(value) {
            field = value
        }

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

            val lastPoint = Point(snakePos.last())

            for (i in snakePos.count() - 1 downTo 1) {
                snakePos[i].x = snakePos[i - 1].x
                snakePos[i].y = snakePos[i - 1].y
            }
            when (direction) {
                MoveDirection.UP -> snakePos[0].y -= 1
                MoveDirection.DOWN -> snakePos[0].y += 1
                MoveDirection.LEFT -> snakePos[0].x -= 1
                MoveDirection.RIGHT -> snakePos[0].x += 1
            }
            if (snakePos[0].x > xMax || snakePos[0].x < 0 ||
                snakePos[0].y > yMax || snakePos[0].y < 0
            )
                snakeBeginDie()
            if (snakePos[0] == applePoint) {
                snakePos.add(lastPoint)
                generateNewApple()
                increaseScore()
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

    private final fun snakeBeginDie() {
        snakeAlive = false
        if (snakeHandler != null)
            snakeHandler!!.snakeDie()
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
        generateNewApple()
    }

    fun setPause(b: Boolean) {
        onPause = b
    }


}