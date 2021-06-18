package com.sychev.kosnake

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.SwitchCompat

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var numCubeDescr: TextView
    private lateinit var seekBar: AppCompatSeekBar
    private lateinit var settingRef: SharedPreferences

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        settingRef = baseContext.getSharedPreferences("Snake", MODE_PRIVATE)
        numCubeDescr = findViewById(R.id.numbCubeDescr)
        seekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setCubeNumberValue(progress + 15)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.d("Setting", "Change value " + String.format("%d", seekBar?.progress))
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.d("Setting", "Change value " + String.format("%d", seekBar?.progress))
            }
        })

        val initialCubeNumber = settingRef.getInt("NumberOfCube", 30)
        numCubeDescr.text = resources.getText(R.string.num_of_cube_spinner)
            .toString() + initialCubeNumber.toString()
        seekBar.setProgress(initialCubeNumber - 15)

        val textBack: TextView = findViewById(R.id.backSign)
        textBack.setOnTouchListener { _, event ->
            Log.d("SETTINGS", "$event")
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
                true
            } else {
                false
            }
        }

        val soundSwitcher = findViewById<SwitchCompat>(R.id.soundSwitcher)
        soundSwitcher.isChecked = settingRef.getBoolean("SoundEnabled", true)
        soundSwitcher.setOnCheckedChangeListener { _, isChecked ->
            run {
                with(settingRef.edit()) {
                    putBoolean("SoundEnabled", isChecked)
                    apply()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCubeNumberValue(value: Int) {
        with(settingRef.edit()) {
            putInt("NumberOfCube", value)
            apply()
        }
        numCubeDescr.text =
            resources.getText(R.string.num_of_cube_spinner).toString() + value.toString()


    }


}