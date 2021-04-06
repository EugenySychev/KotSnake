package com.sychev.kosnake

import android.graphics.Color
import android.graphics.Paint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var menu = MenuDrawer(this);
        val list = listOf<String>("Main", "Second", "Third");
        menu.setItemArray(list);
        setContentView(menu);

    }
}