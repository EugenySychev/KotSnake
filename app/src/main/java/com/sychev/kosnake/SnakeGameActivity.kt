package com.sychev.kosnake

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SnakeGameActivity : AppCompatActivity() {

    private var snakeLogic : SnakeLogic = SnakeLogic(20, 20, 7)
    private lateinit var view: SnakeDrawer

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