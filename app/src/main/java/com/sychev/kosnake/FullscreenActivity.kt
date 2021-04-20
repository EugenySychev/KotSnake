package com.sychev.kosnake

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class FullscreenActivity : AppCompatActivity(), MenuDrawer.SelectedItem {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var menu = MenuDrawer(this)

        val list = listOf<String>("Main", "Second", "Third")
        menu.setSelectedItemHandler(this)
        menu.setItemArray(list)
        setContentView(menu)

    }

    override fun onSelectedItem(item: Int) {
        Log.d("Menu", "Selected item $item")
    }
}