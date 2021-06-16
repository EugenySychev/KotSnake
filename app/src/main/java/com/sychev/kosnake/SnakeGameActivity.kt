package com.sychev.kosnake

import android.annotation.SuppressLint
import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SnakeGameActivity : AppCompatActivity(), SnakeDrawer.DrawerHandler, SnakeLogic.SoundHandler {

    private lateinit var snakeLogic: SnakeLogic
    private lateinit var view: SnakeDrawer
    private lateinit var stepPlayer: MediaPlayer
    private lateinit var eatPlayer: MediaPlayer
    private lateinit var gameOverPlayer: MediaPlayer
    private var soundEnabled = false

    @SuppressLint("ClickableViewAccessibility")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingRef = baseContext.getSharedPreferences("Snake", MODE_PRIVATE)
        val initialCubeNumber = settingRef.getInt("NumberOfCube", 30)
        soundEnabled = settingRef.getBoolean("SoundEnabled", true)
        stepPlayer = MediaPlayer.create(this, R.raw.tick)
        eatPlayer = MediaPlayer.create(this, R.raw.bite)
        gameOverPlayer = MediaPlayer.create(this, R.raw.game_over)
        stepPlayer.isLooping = false
        eatPlayer.isLooping = false
        gameOverPlayer.isLooping = false

        view = SnakeDrawer(this, initialCubeNumber)
        setContentView(view)

        snakeLogic = SnakeLogic(view.xMax, view.yMax, 7)
        view.setSnake(snakeLogic)
        view.setDrawerHandler(this)

        snakeLogic.setSoundHandler(this)

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
        view.setPauseState();
    }

    override fun exitGame() {
        finish()
    }

    override fun onStepSound() {
        if (soundEnabled) {
            if (eatPlayer.isPlaying)
                eatPlayer.stop()
            stepPlayer.start()
        }
    }

    override fun onEatSound() {
        if (soundEnabled) {
            if (stepPlayer.isPlaying)
                stepPlayer.stop()
            eatPlayer.start()
        }
    }

    override fun onDieSound() {
        if (soundEnabled) {
            if (stepPlayer.isPlaying)
                stepPlayer.stop()
            if (eatPlayer.isPlaying)
                eatPlayer.stop()
            gameOverPlayer.start()
        }
    }
}