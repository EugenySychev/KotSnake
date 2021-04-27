package com.sychev.kosnake

import java.lang.Exception

class SnakeLogic(private final var xMax: Int, private final var yMax: Int, private final var length: Int) {

    private var xPos : MutableList<Int> = mutableListOf()
    private var yPos : MutableList<Int> = mutableListOf()
    private var snakeHandler : EventHandler? = null

    private final var direction: MoveDirection = MoveDirection.UP

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
        if (xMax > length + 2) {
            throw Exception("Very big length of snake, unable to locate it")
        } else {
            xPos.clear()
            yPos.clear()
            xPos.add((xMax - length) / 2)
            yPos.add(yMax / 2)

            for (i in 1 until length) {
                xPos.add(xPos[i - 1] - 1)
                yPos.add(yPos[i - 1])
            }
        }
    }

    fun eatApple() {
        length++
    }

    fun changeDirection(newDirection: MoveDirection) {
        direction = newDirection
        makeStep()
    }

    fun getX() = xPos
    fun getY() = yPos

    fun makeStep() {
        for (i in length downTo 1) {
            xPos.add(0)
            xPos[i + 1] = xPos[i]
            yPos.add(0)
            yPos[i + 1] = yPos[i]
        }
        when(direction) {
            MoveDirection.UP -> yPos[0] = yPos[1] - 1
            MoveDirection.DOWN -> yPos[0] = yPos[1] + 1
            MoveDirection.LEFT -> xPos[0] = xPos[1] - 1
            MoveDirection.RIGHT -> xPos[0] = xPos[1] + 1
        }
    }

    fun getLength(): Int {
        return length
    }
}