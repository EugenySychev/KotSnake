package com.sychev.kosnake

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class FullscreenActivity : AppCompatActivity(), MenuDrawer.SelectedItem {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var menu = MenuDrawer(this)

        val list = listOf("Play", "Exit game")
        menu.setSelectedItemHandler(this)
        menu.setItemArray(list)
        setContentView(menu)

    }

    override fun onSelectedItem(item: Int) {
        when (item) {
            0 -> {
                val intent = Intent(this, SnakeGameActivity::class.java)
                startActivity(intent)
            }
//            1 -> {
//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)
//            }
            1 -> finish()
        }

    }
}