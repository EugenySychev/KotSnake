package com.sychev.kosnake

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SnakeGameActivity : AppCompatActivity() {

    private var snakeLogic : SnakeLogic = SnakeLogic(20, 20, 7)
    private lateinit var view: SnakeDrawer

    @SuppressLint("ClickableViewAccessibility")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = SnakeDrawer(this)
        view.setSnake(snakeLogic)
        setContentView(view)
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                snakeLogic.makeStep();
                view.invalidate()
            }
        })

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
            fun performClick() : Boolean{
                return true;
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
}