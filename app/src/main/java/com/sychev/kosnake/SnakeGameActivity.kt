package com.sychev.kosnake

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SnakeGameActivity : AppCompatActivity(), SnakeDrawer.DrawerHandler {

    private lateinit var snakeLogic: SnakeLogic
    private lateinit var view: SnakeDrawer

    @SuppressLint("ClickableViewAccessibility")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = SnakeDrawer(this)
        setContentView(view)

        snakeLogic = SnakeLogic(view.xMax, view.yMax, 7)
        view.setSnake(snakeLogic)
        view.setDrawerHandler(this)

        view.setOnTouchListener(object : OnSwipeTouchListener(this@SnakeGameActivity) {
            override fun onSwipeUp() {
                super.onSwipeUp()
                snakeLogic.changeDirection(SnakeLogic.MoveDirection.UP)
                view.invalidate()
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                snakeLogic.changeDirection(SnakeLogic.MoveDirection.LEFT)
                view.invalidate()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                snakeLogic.changeDirection(SnakeLogic.MoveDirection.RIGHT)
                view.invalidate()
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                snakeLogic.changeDirection(SnakeLogic.MoveDirection.DOWN)
                view.invalidate()
            }

            override fun onClick(x: Float, y: Float) {
                view.click(x, y)
            }

        })
    }

    override fun onPause() {
        super.onPause()
        goToPauseState();
    }

    override fun onResume() {
        super.onResume()
    }

    private fun goToPauseState() {

    }

    override fun exitGame() {
        finish()
    }
}