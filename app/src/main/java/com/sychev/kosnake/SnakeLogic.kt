package com.sychev.kosnake

class SnakeLogic(maxX: Int, maxY: Int, startLength: Int) {

    private var mLength : Int = 0;
    private var xPos : MutableList<Int> = mutableListOf();
    private var yPos : MutableList<Int> = mutableListOf();
    private var snakeHandler : EventHandler? = null;
    private final var xMax = 0;
    private final var yMax = 0;


    enum class MoveDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    interface EventHandler {
        fun snakeDie();
    }

    init {
        xMax = maxX;
        yMax = maxY;
        mLength = startLength;
        //TODO: add random begin position?
    }

    fun eatApple() {
        mLength ++;
    }

    fun makeStep(direction: MoveDirection) {
        for (i in mLength downTo 1) {
            xPos.add(0);
            xPos[i + 1] = xPos[i];
            yPos.add(0);
            yPos[i + 1] = yPos[i];
        }
        when(direction) {
            MoveDirection.UP ->
        }
    }

    fun getLength(): Int {
        return mLength;
    }
}