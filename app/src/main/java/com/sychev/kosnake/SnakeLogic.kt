package com.sychev.kosnake

import android.graphics.Point
import java.lang.Exception
import kotlin.random.Random

class SnakeLogic(
    private var xMax: Int,
    private var yMax: Int,
    private var length: Int
) {

    private lateinit var applePoint : Point
    private var snakePos: MutableList<Point> = mutableListOf()
    private var snakeHandler: EventHandler? = null
    var score : Int = 0

    private var direction: MoveDirection = MoveDirection.UP

    enum class MoveDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    interface EventHandler {
        fun snakeDie()
    }

    init {
        if (xMax < length + 2) {
            throw Exception("Very big length of snake, unable to locate it")
        } else {
            snakePos.clear()
            snakePos.add(Point((xMax - length) / 2, yMax / 2))

            for (i in 1 until length) {
                snakePos.add(Point(snakePos[i - 1].x - 1, snakePos[i - 1].y))
            }
        }
        generateNewApple()
    }

    fun generateNewApple() {
        applePoint = Point(Random.nextInt(xMax), Random.nextInt((yMax)))
    }

    fun changeDirection(newDirection: MoveDirection) {
        direction = newDirection
        makeStep()
    }

    fun getSnakePos(index: Int) =
        if (index < snakePos.count())
            snakePos[index]
        else
            Point(0, 0)


    fun makeStep() {
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
        ) {
            snakeHandler!!.snakeDie()
        }
        if (snakePos[0] == applePoint) {
            snakePos.add(lastPoint)
            generateNewApple()
            score++
        }
    }

    fun getLength(): Int {
        return snakePos.count()
    }

    fun getApplePoint(): Point {
        return applePoint
    }

}